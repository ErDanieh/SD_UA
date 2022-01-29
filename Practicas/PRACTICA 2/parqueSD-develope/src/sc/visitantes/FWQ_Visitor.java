package sc.visitantes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import dt.atracciones.Atracciones;
import gui.visitante.visitorActionWindow;
import kf.consumer.Consumer;
import kf.producer.Producer;

public class FWQ_Visitor {

	/**
	 * ID del visitante
	 */
	private String idVisitor;

	/**
	 * @return the idVisitor
	 */
	public String getIdVisitor() {
		return idVisitor;
	}

	/**
	 * @param idVisitor the idVisitor to set
	 */
	public void setIdVisitor(String idVisitor) {
		this.idVisitor = idVisitor;
		this.color = idVisitor;
	}

	public Thread pointer;

	/**
	 * Token de sesion iniciada
	 */
	private String tokenSesion;

	/**
	 * Posicion actual del visitante
	 */
	private Position posAct;

	/**
	 * Posicion destino del visitante
	 */
	private Position posDest;

	/**
	 * Matriz del mapa
	 */
	private String[][] map;

	/**
	 * Color identificativo del usuario
	 */
	private String color;

	/**
	 * IP del Broker
	 */
	private String ipBroker;

	/**
	 * Puerto del broker
	 */
	private String puertoBroker;
	/**
	 * Topic kafka movimientos
	 */
	public static String TOPIC_MOVIMIENTOS = "MOVIMIENTOS";

	public static String TOPIC_MAPA = "MAPA";

	public static String TOPIC_INICIARSESION = "INICIARSESION";

	/**
	 * Lista con todos los colores validos
	 */
	private List<String> colores = Arrays.asList("BLUE", "GREEN", "RED", "PURPLE", "YELLOW", "BROWN");

	/**
	 * Datos de las atracciones
	 */

	private Map<Position, Atracciones> mapaDatosAtracciones;

	/**
	 * Constructor por defecto de4 visitor
	 */
	public FWQ_Visitor() {
		this.map = new String[20][20];

		Random rand = new Random();
		this.posAct = new Position(rand.nextInt(20), rand.nextInt(20));

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				this.map[i][j] = "-";
			}
		}

		mapaDatosAtracciones = new HashMap<Position, Atracciones>();

	}

	public String leeSocket(Socket p_sk, String p_Datos) {
		try {
			InputStream aux = p_sk.getInputStream();
			DataInputStream flujo = new DataInputStream(aux);
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

	public String pasarServidorEdicion(String p_host, String p_puerto, String cadena) {
		String p_Cadena = null;

		try {

			Socket skCliente = new Socket(p_host, Integer.parseInt(p_puerto));
			p_Cadena = "edit," + cadena;

			System.out.println(p_Cadena);
			escribeSocket(skCliente, p_Cadena);

			p_Cadena = "";

			p_Cadena = leeSocket(skCliente, p_Cadena);
			System.out.println(p_Cadena);

		} catch (Exception e) {
			e.getStackTrace();
		}
		return p_Cadena;

	}

	public String pasarServidorRegistro(String p_host, String p_puerto, String id, String Password, String Name) {

		String p_Cadena = null;

		try {
			Socket skCliente = new Socket(p_host, Integer.parseInt(p_puerto));

			p_Cadena = "regi" + "," + id + "," + Password + "," + Name;
			escribeSocket(skCliente, p_Cadena);

			p_Cadena = "";

			p_Cadena = leeSocket(skCliente, p_Cadena);
			System.out.println(p_Cadena);

		} catch (Exception e) {

		}
		return p_Cadena;
	}

	public void producerMovimientos(Producer producer) {

		for (;;) {

			try {
				TimeUnit.SECONDS.sleep(1);

				if (this.tokenSesion != null) {

					Position posAnt = this.posAct;

					this.posAct = nextMove();
					producer.sendMsg(this.tokenSesion + " " + this.idVisitor + " " + this.color + " " + posAnt + " "
							+ this.posAct);

					if (this.posAct == posDest) {
						elegirNuevoDestino();
					}
				}

			} catch (Exception e) {
				System.out.println("[ENGINE] Error al enviar mensaje o calcular los segundos");

			}
		}

	}

	/**
	 * Recibe el mapa en string y devuelve la matriz del string
	 * 
	 * @param str String que recibe
	 * @return Matriz de strigns
	 */
	public String[][] stringToMap(String str) {

		String[][] res = new String[20][20];
		String linesSplits[] = str.split("/");
		int fila = 0;
		int col = 0;

		// System.out.println(str);

		for (String line : linesSplits) {
			String[] lineSplit = line.split(",");

			for (String e : lineSplit) {
				res[fila][col] = e;
				col++;
			}
			fila++;
			col = 0;
		}

		/***
		 * Para comprobar que se ejecuta correctamente
		 * 
		 * for (int i = 0; i < 20; i++) { for (int j = 0; j < 20; j++) {
		 * System.out.print(res[i][j]); } System.out.println(""); }
		 * 
		 */

		return res;
	}

	/**
	 * Consumidor de la informacion para visualizar el mapa
	 * 
	 * @param consumer      Consumidor
	 * @param ipBrooker     IP del broker
	 * @param puertoBrooker Puerto del broker
	 * @param clientID      clientID
	 * @param groupID       groupID
	 */
	public void consumerKafkaMapa(Consumer consumer, String ipBrooker, String puertoBrooker, String clientID,
			String groupID) {

		consumer.consumer.subscribe(Arrays.asList(consumer.topic));
		while (true) {
			consumer.setRecords(consumer.getConsumer().poll(Duration.ofMillis(1000)));

			for (ConsumerRecord record : consumer.getRecords()) {
				consumer.getLogger()
						.info("Nuevo mensaje recibido:" + "Key: " + record.key() + "Valor: " + record.value()
								+ "Topic: " + record.topic() + "Partition: " + record.partition() + "Offset: "
								+ record.offset() + "\n");
				try {

					String newMap[][] = stringToMap("" + record.value().toString());
					this.map = newMap;
					actualizarDatosAtracciones(record.value().toString());

				} catch (Exception e) {
					System.out.println("[ENGINE] Error con el consumidor de token");
				}
			}
		}
	}

	/**
	 * Guarda los datos de las atracciones
	 * 
	 * @param mapa Strign del mapa
	 */
	public void actualizarDatosAtracciones(String mapa) {

		String linesSplits[] = mapa.split("/");
		int fila = 0;
		int col = 0;
		int id = 0;

		// System.out.println(str);

		for (String line : linesSplits) {
			String[] lineSplit = line.split(",");

			for (String e : lineSplit) {
				try {
					int tiempoEspera = Integer.parseInt(e);
					Position pos = new Position(fila, col);

					Atracciones atr = new Atracciones(id, "atraccion" + id, 0, 0, 0, tiempoEspera, fila, col);
					this.mapaDatosAtracciones.put(pos, atr);
					id++;
				} catch (Exception e2) {
				}

				col++;
			}
			fila++;
			col = 0;
		}
	}

	/**
	 * Mueve al visitante a newPos. Si newPos == posDest calcula una nueva posicion.
	 * Produce el mensaje a kfka.
	 * 
	 * @param producer Productor
	 * @param newPos   Nueva posicion
	 */
	public void moverPosAct(Producer producer, Position newPos) {
		String movimiento = "" + this.tokenSesion + " " + this.color + " " + this.posAct.getFila() + " "
				+ this.posAct.getCol();
		producer.sendMsg(movimiento);
		this.posAct = newPos;
		// TODO: Si hemos alcanzado la casilla destino actualizar casilla destino a la
		// casilla de otra atraccion

		if (newPos == posDest) {
			elegirNuevoDestino();

		}
	}

	/**
	 * Elige un nuevo destino
	 */
	public void elegirNuevoDestino() {

		int minTmp = 100000;
		Position res = null;

		for (Position pos : this.mapaDatosAtracciones.keySet()) {
			if (this.mapaDatosAtracciones.get(pos).gettEspera() < minTmp
					&& (pos.getFila() != this.posAct.getFila() && pos.getCol() != this.posAct.getCol())) {
				minTmp = this.mapaDatosAtracciones.get(pos).gettEspera();
				res = pos;
			}
		}

		this.posDest = res;
	}

	/**
	 * Elige cual va a ser el siguiente movimiento en base a la posicion actual y la
	 * posicion destino
	 * 
	 * @return La siguiente posicion a moverse
	 */
	public Position nextMove() {

		Position newPos = new Position(this.posAct.getFila(), this.posAct.getCol());

		if (this.posDest == null || this.mapaDatosAtracciones.get(this.posDest).gettEspera() > 60
				|| (this.posAct.getFila() == this.posDest.getFila() && this.posAct.getCol() == this.posDest.getCol())) {
			elegirNuevoDestino();
		}

		if (this.posAct.getFila() > this.posDest.getFila()) {
			newPos.setFila(newPos.getFila() - 1);
		}
		if (this.posAct.getFila() < this.posDest.getFila()) {
			newPos.setFila(newPos.getFila() + 1);
		}
		if (this.posAct.getCol() < this.posDest.getCol()) {
			newPos.setCol(newPos.getCol() + 1);
		}
		if (this.posAct.getCol() > this.posDest.getCol()) {
			newPos.setCol(newPos.getCol() - 1);
		}

		return newPos;
	}

	/**
	 * Produce el mensaje a kafka para salir del parque
	 */
	public void salirParque() {

		Producer productorSalirParque = new Producer("SALIRPARQUE", this.ipBroker, this.puertoBroker,
				"salirParque" + this.idVisitor, "salirParqueGroup" + this.idVisitor);

		productorSalirParque.sendMsg(this.idVisitor + " " + this.tokenSesion);

		this.pointer.suspend();
	}

	/**
	 * @return the puertoBroker
	 */
	public String getPuertoBroker() {
		return puertoBroker;
	}

	/**
	 * @param puertoBroker the puertoBroker to set
	 */
	public void setPuertoBroker(String puertoBroker) {
		this.puertoBroker = puertoBroker;
	}

	/**
	 * @return the ipBroker
	 */
	public String getIpBroker() {
		return ipBroker;
	}

	/**
	 * @param ipBroker the ipBroker to set
	 */
	public void setIpBroker(String ipBroker) {
		this.ipBroker = ipBroker;
	}

	/**
	 * @return the tokenSesion
	 */
	public String getTokenSesion() {
		return tokenSesion;
	}

	/**
	 * @param tokenSesion the tokenSesion to set
	 */
	public void setTokenSesion(String tokenSesion) {
		this.tokenSesion = tokenSesion;
	}

	/**
	 * @return the posAct
	 */
	public Position getPosAct() {
		return posAct;
	}

	/**
	 * @param posAct the posAct to set
	 */
	public void setPosAct(Position posAct) {
		this.posAct = posAct;
	}

	/**
	 * @return the posDest
	 */
	public Position getPosDest() {
		return posDest;
	}

	/**
	 * @param posDest the posDest to set
	 */
	public void setPosDest(Position posDest) {
		this.posDest = posDest;
	}

	public String[][] getMap() {
		return this.map;
	}

	/**
	 * <IP Registry> <Puerto Registry> <IP Bootstrap> <Puerto Bootstrap>
	 * 
	 * @param args <IP Registry> <Puerto Registry> <IP Bootstrap> <Puerto Bootstrap>
	 */
	public static void main(String[] args) {

		if (args.length != 4) {
			System.out.println("Error: argumentos invalidos <puertoEscucha> <IPBrooker> <PuertoBroker>");
			return;
		}

		String host = args[0];
		String puerto = args[1];
		String ipBrooker = args[2];
		String puertoBrooker = args[3];

		String topicIniciarSesion = "INICIARSESION";
		String groupIDIniciarSesion = "VISITOR";
		String clientIDIniciarSesion = "VISITOR";

		Random rand = new Random();
		int numb = rand.nextInt(200000);

		FWQ_Visitor visitante = new FWQ_Visitor();
		visitante.setIpBroker(ipBrooker);
		visitante.setPuertoBroker(puertoBrooker);

		visitorActionWindow ventana = new visitorActionWindow(host, puerto, ipBrooker, puertoBrooker,
				topicIniciarSesion, groupIDIniciarSesion, clientIDIniciarSesion, visitante);

		Consumer consumerMAPA = new Consumer(FWQ_Visitor.TOPIC_MAPA, ipBrooker, puertoBrooker, "VISITORMAP",
				"VISITORMAP" + numb);
		Producer productorMovimientos = new Producer(FWQ_Visitor.TOPIC_MOVIMIENTOS, ipBrooker, puertoBrooker,
				"visitorMov", "VISITORMAP");

		Thread threadKafkaMap = new Thread() {
			public void run() {
				visitante.consumerKafkaMapa(consumerMAPA, ipBrooker, puertoBrooker, "VISITORMAP", "VISITORMAP");
			}
		};

		Thread threadKafkaMovimientos = new Thread() {
			public void run() {
				visitante.producerMovimientos(productorMovimientos);
			}
		};

		visitante.pointer = threadKafkaMovimientos;
		threadKafkaMap.start();
		threadKafkaMovimientos.start();
	}
}
