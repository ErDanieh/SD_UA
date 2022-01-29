package sc.engine;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.ColorUIResource;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import dt.atracciones.Atracciones;
import kf.consumer.Consumer;
import kf.producer.Producer;
import sc.visitantes.Position;

/**
 * La clase FWQ_Engine representa el motor logico de la aplicacion.
 * 
 * @version SimbaLion 0.9
 */
public class FWQ_Engine {

	/**
	 * Datos de las atracciones
	 */
	private ArrayList<Atracciones> dtAtracciones;

	/**
	 * Array del mapa
	 */
	private String[][] map;

	/**
	 * Cache con los tokens de sesiones
	 */
	// private ArrayList<String> cacheToken;

	private Map<String, String> idVisitanteTokens;

	/**
	 * TOPIC Kafka iniciar sesion
	 */
	public static String TOPIC_INICIARSESION = "INICIARSESION";

	/**
	 * TOPIC de kafka enviar tokens
	 */
	public static String TOPIC_TOKEN = "TOKEN";

	/**
	 * TOPIC de kafka envia el mapa
	 */
	public static String TOPIC_MAP = "MAPA";

	/**
	 * TOPIC de kafka movimientos visitantes
	 */
	public static String TOPIC_MOVIMIENTOS = "MOVIMIENTOS";

	/**
	 * Siguiente token
	 */
	private int nextToken = 1;

	/**
	 * Maximo de personas en el parque
	 */
	private int maxPersonasParque;

	/**
	 * Personas que hay en el aprque
	 */
	private int personasEnParque;

	/**
	 * Constructor por defecto de FWQ_Engine
	 */
	public FWQ_Engine(int maxPersonas) {
		dtAtracciones = new ArrayList<Atracciones>();
		// cacheToken = new ArrayList<String>();
		this.idVisitanteTokens = new HashMap<String, String>();
		this.maxPersonasParque = maxPersonas;
		this.map = new String[20][20];

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				this.map[i][j] = "-";
			}
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
			p_Datos = flujo.readUTF();
		} catch (Exception e) {
			System.out.println("ErrorLerrEngine: " + e.toString());
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
	 * Solicita al servidor de tiempos las actualizaciones de los calculos de
	 * tiempos.
	 * 
	 * @param skClient Socket del cliente
	 */
	public void solicitarActTiempos(Socket skClient) {
		System.out.println("[Engine] Solicitando las actualizaciones de tiempos de espera...");
		String dt_datos = "actualizacionDeTiempos";
		escribeSocket(skClient, dt_datos);
		dt_datos = leeSocket(skClient, dt_datos);

		String filas[] = dt_datos.split(";");

		for (int i = 0; i < filas.length; i++) {
			String datos[] = filas[i].split(",");

			Atracciones atr = new Atracciones(Integer.parseInt(datos[0]), datos[1], Integer.parseInt(datos[2]),
					Integer.parseInt(datos[3]), Integer.parseInt(datos[4]), Integer.parseInt(datos[5]),
					Integer.parseInt(datos[6]), Integer.parseInt(datos[7]));

			if (!dtAtracciones.contains(atr)) {
				System.out.println(
						"[Engine] WARNING! El servidor de tiempos envia informacion de una atraccion que no pertenece a la base de datos.");
			}

			try {
				this.dtAtracciones.set(dtAtracciones.indexOf(atr), atr); // Reemplazamos los valores leidos
				this.map[atr.getPosFila()][atr.getPosCol()] = "" + atr.gettEspera();
				System.out.println(this.mapToString());

			} catch (Exception e) {
				System.out.println("[ENGINE] Error al setear el valor");
			}
		}
	}

	/**
	 * Actualiza la tabla Atracciones con los valores de dtAtracciones
	 * 
	 * @param con Conexion con la base de datos
	 */
	public void updateAtracciones(Connection conn) {
		String sql = "";
		for (Atracciones atr : this.dtAtracciones) {
			sql = "UPDATE ATRACCIONES SET nCola=" + atr.getnCola() + "," + " tTurno=" + atr.getnTurno() + ","
					+ " tEspera=" + atr.gettEspera() + " where ID=" + atr.getID() + ";";
			try {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("[Engine] Error: No se han podido actualizar todos los valores.");
			}
		}
	}

	/**
	 * Envia la informacion necesaria para el servidor de tiempos mediante sockets.
	 * 
	 * @param skCliente Socket del cliente
	 */
	public void enviarAtraccionesDT(Socket skCliente) {
		System.out.println("[Engine] Escribiendo en el socket la tabla Atracciones...");

		String dt_datos = "";
		String rc_datos = "";

		for (Atracciones atrac : this.dtAtracciones) {
			dt_datos += atrac.getID() + "," + atrac.getNombre() + "," + atrac.getnTurno() + "," + atrac.getnCola() + ","
					+ atrac.getnTurno() + "," + atrac.gettEspera() + "," + atrac.getPosFila() + "," + atrac.getPosCol()
					+ ";";
		}

		// Escribir mientras TiemposServidor no lo haya leido
		do {
			escribeSocket(skCliente, dt_datos);
			rc_datos = leeSocket(skCliente, rc_datos);
			System.out.println("[Engine] Atracciones leidas correctamente");
		} while (rc_datos == "");

	}

	/**
	 * Realiza la conexion con la base de datos.
	 * 
	 * @param dbUrl URL de la base de datos
	 * @param user  Usuario
	 * @param pass  Contrasena
	 * @param con   Conexion de la base de datos
	 * @return Devuelve si se ha establecido la conexion con exito.
	 */
	private Connection connectDB(String dbUrl, String user, String pass) {

		try {
			Connection con = DriverManager.getConnection(dbUrl, user, pass);
			System.out.println("Exito! Conectado a la base de datos.");
			return con;

		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println("Error al conectarse a la base de datos: " + e.getMessage());

		}

		return null;
	}

	/**
	 * Obtiene toda la informacion de la tabla atracciones
	 * 
	 * @param con           Conexion a la BD
	 * @param dtAtracciones Filas de la tabla Atracciones
	 * @return Si se han cargado los datos correctamente
	 */
	private boolean cargarDatosAtracciones(Connection con, ArrayList<Atracciones> dtAtracciones) {
		try {
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("select * from Atracciones");
			System.out.println("He hecho bien el select");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + "  \t" + rs.getInt(3) + "\t" + rs.getInt(4)
						+ "\t" + rs.getInt(5) + "\t" + rs.getInt(6) + "\t(" + rs.getInt(7) + ", " + rs.getInt(8) + ")");
				dtAtracciones.add(new Atracciones(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4),
						rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8)));

			}

			return true;

		} catch (SQLException e) {
			System.out.println("[ENGINE] Error al cargar los datos.");
		}

		return false;
	}

	/**
	 * Escucha las solicitudes de iniciar sesion <IDUsuario> <Contraseña>. Devuelve
	 * <IDUsuario> <Token>. En caso de que no sean las credenciales correctas
	 * Token=ERROR. Parque lleno = PARQUELLENO
	 * 
	 * @param consumer Consumidor
	 */
	public void consumerKafkaCredenciales(Consumer consumer, Producer producer, Connection conn, String ipBrooker,
			String puertoBrooker, String clientID, String groupID) {

		consumer.consumer.subscribe(Arrays.asList(consumer.topic));
		while (true) {
			consumer.setRecords(consumer.getConsumer().poll(Duration.ofMillis(1000)));

			for (ConsumerRecord record : consumer.getRecords()) {
				consumer.getLogger()
						.info("Nuevo mensaje recibido:" + "Key: " + record.key() + "Valor: " + record.value()
								+ "Topic: " + record.topic() + "Partition: " + record.partition() + "Offset: "
								+ record.offset() + "\n");
				try {

					String[] lineSplit = record.value().toString().split(" ");
					String IDUsuario = lineSplit[0];
					String password = lineSplit[1];

					if (this.idVisitanteTokens.keySet().contains(IDUsuario)
							|| !comprobarCredenciales(IDUsuario, password, conn)) {
						producer.sendMsg(IDUsuario + " ERROR", record.key().toString());
					} else if (this.maxPersonasParque < this.personasEnParque + 1) {
						producer.sendMsg(IDUsuario + " PARQUELLENO", record.key().toString());
					} else {
						String token = generateNewToken();
						this.idVisitanteTokens.put(IDUsuario, token);
						// this.cacheToken.add(token);
						this.personasEnParque++;
						producer.sendMsg(IDUsuario + " " + token, record.key().toString());
					}

				} catch (Exception e) {
					System.out.println("[ENGINE] Error con el consumidor de token");
				}
			}
		}
	}

	/**
	 * Genera un token
	 * 
	 * @return Un token unico nuevo
	 */
	public String generateNewToken() {
		String token = "" + this.nextToken;
		this.nextToken++;
		return token;
	}

	/**
	 * Elimina un token del cache.
	 * 
	 * @param token Token a eliminar
	 */
	public void removeTokenCache(String idVisitante) {
		// this.cacheToken.remove(token);
		this.idVisitanteTokens.remove(idVisitante);
	}

	/**
	 * Comprueba si las credenciales son validas en la base de datos.
	 * 
	 * @param IDUsuario ID del usuario
	 * @param password  Contrasña
	 * @return Si las credenciales son validas
	 */
	public boolean comprobarCredenciales(String IDUsuario, String password, Connection conn) {

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(
					"SELECT ID FROM Visitantes WHERE ID='" + IDUsuario + "' AND Password='" + password + "'");

			if (!rs.next())
				return false;
		} catch (Exception e) {
			System.out.println("[ENGINE] Error al comprobar las credenciales");
		}

		return true;
	}

	/**
	 * Conexion socket entre el Engine y el Servidor tiempo de espera
	 * 
	 * @param IPTimeServer     IP del servidor de tiempos de espera
	 * @param puertoTimeServer Puerto del servidor de tiempos de espera
	 */
	public void socketTiemposEspera(String IPTimeServer, String puertoTimeServer) {
		Socket skCliente;
		for (;;) {
			try {
				TimeUnit.SECONDS.sleep(3);
				skCliente = new Socket(IPTimeServer, Integer.parseInt(puertoTimeServer));
			} catch (Exception e) {
				System.out.println("[Engine] Error al conectarse al socke.");
				continue;
			}
			try {
				this.solicitarActTiempos(skCliente); // Solicitamos los nuevos tiempos
			} catch (Exception e) {
				System.out.println("[Engine] Error al solicitar ActTiempos");
			}
		}
	}

	// TODO: Implementar que las posiciones de cada usuario se guardan tambien en la
	// base de datos. (consumer kafka movimientos)

	/**
	 * Escucha las solicitudes de iniciar sesion <IDUsuario> <Contraseña>. Devuelve
	 * <IDUsuario> <Token>. En caso de que no sean las credenciales correctas
	 * Token=ERROR. Parque lleno = PARQUELLENO
	 * 
	 * @param consumer Consumidor
	 */
	public void consumerKafkaMovimientos(Consumer consumer, String ipBrooker, String puertoBrooker, String clientID,
			String groupID) {

		consumer.consumer.subscribe(Arrays.asList(consumer.topic));

		while (true) {
			consumer.setRecords(consumer.getConsumer().poll(Duration.ofMillis(100)));

			for (ConsumerRecord record : consumer.getRecords()) {
				consumer.getLogger()
						.info("Nuevo mensaje recibido:" + "Key: " + record.key() + "Valor: " + record.value()
								+ "Topic: " + record.topic() + "Partition: " + record.partition() + "Offset: "
								+ record.offset() + "\n");
				try {

					String[] lineSplit = record.value().toString().split(" ");
					String token = lineSplit[0];
					String IDUsuario = lineSplit[1];
					String strColor = lineSplit[2];
					Position posAnterior = new Position(Integer.parseInt(lineSplit[3]), Integer.parseInt(lineSplit[4]));
					Position posAct = new Position(Integer.parseInt(lineSplit[5]), Integer.parseInt(lineSplit[6]));

					if (this.idVisitanteTokens.containsKey(IDUsuario)
							&& this.idVisitanteTokens.get(IDUsuario).equals(token)) {
						this.map[posAnterior.getFila()][posAnterior.getCol()] = "-";
						this.map[posAct.getFila()][posAct.getCol()] = strColor;
					}

				} catch (Exception e) {
					System.out.println("[ENGINE] Error con el consumidor de token");
				}
			}
		}
	}

	/**
	 * Consume las solicitudes para salir del mapa
	 * 
	 * @param consumer      Consumidor
	 * @param ipBrooker     IP del brooker
	 * @param puertoBrooker Puerto del broker
	 */
	public void consumerKafkaSalirParque(Consumer consumer, String ipBrooker, String puertoBrooker) {

		consumer.consumer.subscribe(Arrays.asList(consumer.topic));

		while (true) {
			consumer.setRecords(consumer.getConsumer().poll(Duration.ofMillis(100)));

			for (ConsumerRecord record : consumer.getRecords()) {
				consumer.getLogger()
						.info("Nuevo mensaje recibido:" + "Key: " + record.key() + "Valor: " + record.value()
								+ "Topic: " + record.topic() + "Partition: " + record.partition() + "Offset: "
								+ record.offset() + "\n");
				try {

					String[] lineSplit = record.value().toString().split(" ");

					String idUsuario = lineSplit[0];
					String token = lineSplit[1];

					if (!this.idVisitanteTokens.keySet().contains(idUsuario)) {
						continue;
					}

					this.personasEnParque--;
					for (int i = 0; i < 20; i++) {
						for (int j = 0; j < 20; j++) {
							if (this.map[i][j].equals(idUsuario)) {
								this.map[i][j] = "-";
							}
						}
					}

				} catch (Exception e) {
					System.out.println("[ENGINE] Error con el consumidor de token");
				}
			}
		}
	}

	/**
	 * Cada segundo produce un mensaje con la informacion del mapa
	 */
	public void producerKafkaMap(Producer producer) {

		for (;;) {
			try {
				TimeUnit.SECONDS.sleep(1);
				producer.sendMsg(this.mapToString());
			} catch (Exception e) {
				System.out.println("[ENGINE] Error al enviar mensaje o calcular los segundos");

			}
		}

	}

	/**
	 * Devuelve map de engine en formato estandarizado. -,-,-,-,| -,-,-,-,|
	 * -,7,-,-,|
	 * 
	 * @return Mapa en String
	 */
	private String mapToString() {
		String res = "";
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				res += this.map[i][j] + ",";
			}
			res += "/";
		}

		return res;
	}

	/**
	 * Inicia el ENGINE. Host y puerto suelen ser 127.0.0.1 1236
	 * 
	 * @param args <IP-TimeServer> <Puerto-TimeServer> <Numero maximo de visitantes>
	 *             <IP-broker> <Puerto-Broker>
	 */
	public static void main(String[] args) {

		if (args.length != 5) {
			System.out.println(
					"Numero de parametros invalidos: <IP-TimeServer> <Puerto-TimeServer> <Numero maximo de visitantes> <IP-broker> <Puerto-Broker>");
			return;
		}

		String IPtimeServer = args[0];
		String puertoTimeServer = args[1];
		int maxPersonas = Integer.parseInt(args[2]);

		String ipBrooker = args[3];
		String puertoBrooker = args[4];

		String clientID = "engine";
		String groupID = "engine";

		String DB_URL = "jdbc:mysql://remotemysql.com:3306/UFD6yb2Mz3";
		String USER = "UFD6yb2Mz3";
		String PASS = "TcV9ViEEPN";
		String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

		FWQ_Engine engine = new FWQ_Engine(maxPersonas);

		Connection conBD = engine.connectDB(DB_URL, USER, PASS);

		engine.cargarDatosAtracciones(conBD, engine.dtAtracciones); // Cargamos los datos de las atracciones en
																	// las variables de Engine

		for (int i = 0; i < engine.dtAtracciones.size(); i++) {
			System.out.println(engine.dtAtracciones.get(i).getID());
		}

		Consumer consumerIniciarSesion = new Consumer(FWQ_Engine.TOPIC_INICIARSESION, ipBrooker, puertoBrooker,
				clientID, groupID, true);

		Consumer consumerMovimientos = new Consumer(FWQ_Engine.TOPIC_MOVIMIENTOS, ipBrooker, puertoBrooker,
				clientID + "3", groupID + "3", true);

		Consumer consumerSalirParque = new Consumer("SALIRPARQUE", ipBrooker, puertoBrooker, clientID + 1, groupID + 1,
				true);

		Producer producerToken = new Producer(FWQ_Engine.TOPIC_TOKEN, ipBrooker, puertoBrooker, "engineUno1",
				"engineUno1");
		Producer producerMap = new Producer(FWQ_Engine.TOPIC_MAP, ipBrooker, puertoBrooker, "engineUno2", "engineUno2");

		Thread threadKafkaToken = new Thread() {
			public void run() {
				engine.consumerKafkaCredenciales(consumerIniciarSesion, producerToken, conBD, ipBrooker, puertoBrooker,
						clientID, groupID);
			}
		};

		Thread threadKafkaMovimientos = new Thread() {
			public void run() {
				engine.consumerKafkaMovimientos(consumerMovimientos, ipBrooker, puertoBrooker, clientID, groupID);
			}
		};

		Thread threadKafkaMap = new Thread() {
			public void run() {
				engine.producerKafkaMap(producerMap);
			}
		};
		Thread threadTimeServer = new Thread() {
			public void run() {
				engine.socketTiemposEspera(IPtimeServer, puertoTimeServer);
			}
		};

		Thread threadKafkaSalirParque = new Thread() {
			public void run() {
				engine.consumerKafkaSalirParque(consumerSalirParque, ipBrooker, puertoBrooker);
			}
		};

		threadKafkaToken.start();
		threadTimeServer.start();
		threadKafkaMovimientos.start();
		threadKafkaMap.start();
		threadKafkaSalirParque.start();

	}
}
