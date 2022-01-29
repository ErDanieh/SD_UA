package sc.tiemposespera;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import dt.atracciones.Atracciones;
import kf.consumer.Consumer;
import sc.crypto.Crypto;

/**
 * La clase FWQ_WaitingTimeServer representa el Servidor de Tiempos de espera.
 * El servidor sera el encargado de conectarse a kafka y crear los hilos para
 * las peticiones del Engine.
 * 
 */
public class FWQ_WaitingTimeServer {

	/**
	 * Atracciones DataTable
	 */
	private Map<Integer, Atracciones> sensoresAtracciones;

	/**
	 * Constructor por defecto. Los datos de la BD se cargan automaticamente al
	 * crear el objeto.
	 */
	public FWQ_WaitingTimeServer() {
		sensoresAtracciones = new HashMap<>();
	}

	/**
	 * Carga los valores dtAtracciones desde el archivo tiemposEsperaDB.txt
	 */
	public void cargarDTAtracciones() {

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("src/sc/tiemposespera/tiemposEsperaDB.txt"));

			try {
				try {

					String line = br.readLine();

					while (line != null) {
						String[] lineSplit = line.split(",");

						try {
							Atracciones atrac = new Atracciones(Integer.parseInt(lineSplit[1]), lineSplit[2],
									Integer.parseInt(lineSplit[3]), Integer.parseInt(lineSplit[4]),
									Integer.parseInt(lineSplit[5]), Integer.parseInt(lineSplit[6]),
									Integer.parseInt(lineSplit[7]), Integer.parseInt(lineSplit[8]));
							this.sensoresAtracciones.put(Integer.parseInt(lineSplit[1]), atrac);

						} catch (Exception e) {
							System.out.println("Error: No soy capaz de hacer un put :(");
						}

						line = br.readLine();
					}

				} finally {
					br.close();
				}
			} catch (Exception e) {
				System.out.println("[ENGINE]  Error al leer los datos del archivo de texto.");
			}

		} catch (FileNotFoundException e1) {
			System.out.println("[ENGINE] Error al abrir el archivo de texto.");
		}

		System.out.println("[ENGINE] Datos cargados correctamente");

		return;
	}

	/**
	 * Escucha en el puerto especificado
	 * 
	 * @param puerto Puerto de escucha
	 */
	public void serverStart(String puerto, String topic, String IPBroker, String puertoBroker, String groupID,
			String clientID) {

		try {
			ServerSocket skServidor = new ServerSocket(Integer.parseInt(puerto));
			System.out.println("Escucho el puerto " + puerto);

			for (;;) {
				Socket skCliente = skServidor.accept(); // Crea objeto
				System.out.println("Sirviendo cliente...");

				Thread t = new HILO_WaitingTimeServer(skCliente, this.sensoresAtracciones);
				t.start();

			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}

	/*
	 * Lee datos del socket. Supone que se le pasa un buffer con hueco suficiente
	 * para los datos. Devuelve el numero de bytes leidos o 0 si se cierra fichero o
	 * -1 si hay error.
	 */
	public String leeSocket(Socket p_sk, String p_Datos) {
		try {
			InputStream aux = p_sk.getInputStream();
			DataInputStream flujo = new DataInputStream(aux);
			p_Datos = new String();
			p_Datos = flujo.readUTF();
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
		return p_Datos;
	}

	/*
	 * Escribe dato en el socket cliente. Devuelve numero de bytes escritos, o -1 si
	 * hay error.
	 */
	public void escribeSocket(Socket p_sk, String p_Datos) {
		try {
			OutputStream aux = p_sk.getOutputStream();
			DataOutputStream flujo = new DataOutputStream(aux);
			flujo.writeUTF(p_Datos);
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
		return;
	}

	/**
	 * Actualiza los datos consumidos de kafka. Calcula los nuevos tiempos de espera
	 * y los almacena en cache.
	 * 
	 * @param consumer El consumidor
	 */
	@SuppressWarnings("rawtypes")
	public void consumKafkaData(Consumer consumer) {

		consumer.consumer.subscribe(Arrays.asList(consumer.topic));
		while (true) {
			consumer.setRecords(consumer.getConsumer().poll(Duration.ofMillis(1000)));

			for (ConsumerRecord record : consumer.getRecords()) {
				consumer.getLogger()
						.info("Nuevo mensaje recibido:" + "Key: " + record.key() + "Valor: " + record.value()
								+ "Topic: " + record.topic() + "Partition: " + record.partition() + "Offset: "
								+ record.offset() + "\n");
				try {
					Crypto cry = new Crypto();
					cry.addKey("SDESGUAY");
					String mensaje = null;
					mensaje = cry.desencriptar(record.value().toString());
					String[] lineSplit = mensaje.split(" ");
					//String[] lineSplit = record.value().toString().split(" ");

					int idSensor = Integer.parseInt(lineSplit[0]);
					int nCola = Integer.parseInt(lineSplit[1]);
					Atracciones atrac = sensoresAtracciones.get(idSensor);
					atrac.setnCola(nCola);
					atrac.settEspera((atrac.getnCola() / atrac.getnTurno()) * atrac.gettTurno());
					System.out.println(atrac);

					this.sensoresAtracciones.put(idSensor, atrac);
				} catch (Exception e) {
					System.out.println("[TIME-SERVER] Error al calcular los tiempos de espera.");
				}
			}
		}
	}

	/**
	 * Inicia el servidor de tiempos de espera. Puerto 9092
	 * 
	 * @param args <puertoEscucha> <IPBrooker> <PuertoBroker>
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {

		if (args.length != 3) {
			System.out.println("Error: argumentos invalidos <puertoEscucha> <IPBrooker> <PuertoBroker>");
			return;
		}

		String puertoEscucha = args[0];
		String IPBrooker = args[1];
		String puertoBrooker = args[2];

		String groupID = "timeServer";
		String clientID = "timeServer";

		String TOPIC = "SENSORES";

		FWQ_WaitingTimeServer waitTimeServer = new FWQ_WaitingTimeServer();
		waitTimeServer.cargarDTAtracciones();

		Consumer consumer = new Consumer(TOPIC, IPBrooker, puertoBrooker, clientID, groupID, true);

		Thread threadKafka = new Thread() {
			public void run() {
				waitTimeServer.consumKafkaData(consumer);
			}
		};

		threadKafka.start();

		waitTimeServer.serverStart(puertoEscucha, TOPIC, IPBrooker, puertoBrooker, groupID, clientID);
	}
}
