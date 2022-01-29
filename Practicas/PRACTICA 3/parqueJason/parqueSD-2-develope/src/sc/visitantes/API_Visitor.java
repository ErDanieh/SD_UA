package sc.visitantes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.google.common.hash.Hashing;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import dt.atracciones.Atracciones;
import gui.visitante.visitorActionWindowAPI;
import kf.consumer.Consumer;
import kf.producer.Producer;

public class API_Visitor {

	/**
	 * Url de la api
	 */
	public String url;

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
	public API_Visitor() {
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

	public String pasarServidorEdicion(String oldId, String newID, String newNombre, String oldPassword,
			String newPassword, String url) {
		Unirest.setTimeouts(0, 0);

		String salt = "ParqueSDsalt";
		String hashedOldPassword = Hashing.sha256().hashString(oldPassword + salt, StandardCharsets.UTF_8).toString();
		String hashedNewPassword = Hashing.sha256().hashString(newPassword + salt, StandardCharsets.UTF_8).toString();

		String bodyReq = "{\r\n    \"newId\": \"" + newID + "\",\r\n    \"Nombre\": \"" + newNombre
				+ "\",\r\n    \"passwordOld\": \"" + hashedOldPassword + "\",\r\n    \"passwordNew\": \""
				+ hashedNewPassword + "\"\r\n}    \r\n";

		try {
			HttpResponse<String> response = Unirest.post(url + "/visitantes/" + oldId)
					.header("Content-Type", "application/json").body(bodyReq).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}

		return "All done! From API!";
	}

	public String pasarServidorRegistro(String id, String Password, String Name, String url) {

		Unirest.setTimeouts(0, 0);

		String salt = "ParqueSDsalt";
		String hashedPassword = Hashing.sha256().hashString(Password + salt, StandardCharsets.UTF_8).toString();

		String bodyReq = "{\r\n        \"ID\": \"" + id + "\",\r\n        \"Nombre\": \"" + Name
				+ "\",\r\n        \"Password\": \"" + hashedPassword
				+ "\",\r\n        \"enParque\": 0,\r\n        \"posFila\": null,\r\n        \"posColumna\": null,\r\n        \"color\": \"FFFFF1\"\r\n    }    \r\n";
		try {
			HttpResponse<String> response = Unirest.post(url + "/visitantes/")
					.header("Content-Type", "application/json").body(bodyReq).asString();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "All done! From API!";

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

		if (args.length != 3) {
			System.out.println("Error: argumentos invalidos   <IPBrooker> <PuertoBroker> <httpAPI>");
			return;
		}

		String ipBrooker = args[0];
		String puertoBrooker = args[1];
		String urlApi = args[2];

		String topicIniciarSesion = "INICIARSESION";
		String groupIDIniciarSesion = "VISITOR";
		String clientIDIniciarSesion = "VISITOR";

		Random rand = new Random();
		int numb = rand.nextInt(200000);

		API_Visitor visitante = new API_Visitor();
		visitante.setIpBroker(ipBrooker);
		visitante.setPuertoBroker(puertoBrooker);
		visitante.setUrl(urlApi);

		visitorActionWindowAPI ventana = new visitorActionWindowAPI(ipBrooker, puertoBrooker, topicIniciarSesion,
				groupIDIniciarSesion, clientIDIniciarSesion, visitante, urlApi);

		Consumer consumerMAPA = new Consumer(API_Visitor.TOPIC_MAPA, ipBrooker, puertoBrooker, "VISITORMAP",
				"VISITORMAP" + numb);
		Producer productorMovimientos = new Producer(API_Visitor.TOPIC_MOVIMIENTOS, ipBrooker, puertoBrooker,
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
