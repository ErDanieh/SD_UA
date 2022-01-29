package sc.sensores;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import gui.sensor.SensorManageWindow;
import kf.producer.Producer;
import sc.crypto.Crypto;

/**
 * La clase Sensor representa un sensor del parque.
 * 
 * @author Ander
 *
 */
public class FWQ_Sensor {

	/**
	 * Topic de kafka
	 */
	private static String TOPIC = "SENSORES";

	/**
	 * ID del grupo
	 */
	private static String GROUP_ID = "SENSORES";

	/**
	 * Numero de personas en la fila
	 */
	private int numPerFila = 0;
	/**
	 * ID de la atraccion
	 */
	private int IDAtraccion;

	/**
	 * Productor de kafka
	 */
	private Producer productor;

	/**
	 * IP de kafka
	 */
	private String bootstrap_server;

	/**
	 * Puerto de kafka
	 */
	private String bootstrap_server_port;

	/**
	 * Constructor del Sensor con el numero de personas en la fila preestablecido
	 * 
	 * @param IDAtraccion           ID de la traccion
	 * @param bootstrap_server      IP del servidor
	 * @param bootstrap_server_port Puerto del servidor
	 * @param numPerFila            Numero de personas en la fila
	 */
	public FWQ_Sensor(int IDAtraccion, String bootstrap_server, String bootstrap_server_port, int numPerFila) {

		this.IDAtraccion = IDAtraccion;
		this.bootstrap_server = bootstrap_server;
		this.bootstrap_server_port = bootstrap_server_port;
		this.numPerFila = numPerFila;
	}

	/**
	 * Constructor del Sensor con el numero de personas en la fila a 0
	 * 
	 * @param IDAtraccion           ID de la traccion
	 * @param bootstrap_server      IP del servidor
	 * @param bootstrap_server_port Puerto del servidor
	 * @param numPerFila            Numero de personas en la fila
	 */
	public FWQ_Sensor(int IDAtraccion, String bootstrap_server, String bootstrap_server_port) {

		this.IDAtraccion = IDAtraccion;
		this.bootstrap_server = bootstrap_server;
		this.bootstrap_server_port = bootstrap_server_port;
	}

	/**
	 * Envia data a kafka
	 * 
	 * @param productor Productor
	 */
	public void startProducer(Producer productor) {

		int seed = 24;
		Random rand = new Random(seed);
		int maxPersonas = 250; // Maximo de personas en la fila

		Random randTiempo = new Random();
		int maxTiemp = 3;

		for (;;) {
			try {
				int pruba = randTiempo.nextInt(maxTiemp) + 1;
				TimeUnit.SECONDS.sleep(pruba);
				this.numPerFila = rand.nextInt(maxPersonas);
				String sensor_data = "" + this.IDAtraccion + " " + this.numPerFila;
				Crypto cry = new Crypto();
				cry.addKey("SDESGUAY");
				String mensaje = null;
				mensaje = cry.encriptar(sensor_data);
				productor.sendMsg(mensaje);
				//productor.sendMsg(sensor_data);

			} catch (Exception e) {
				System.out.println("[SENSOR] Error al enviar el mensaje por kafka.");
				e.printStackTrace();
			}

		}
	}

	/**
	 * Envia un valor especifico de personas en la cola.
	 * 
	 * @param data     Datos a enviar
	 * @param producor Productor
	 */
	public void sendProducerMsg(Producer productor, String data) {

		try {
			String sensor_data = "" + this.IDAtraccion + " " + data;
			Crypto cry = new Crypto();
			cry.addKey("SDESGUAY");
			String mensaje = null;
			mensaje = cry.encriptar(sensor_data);
			//productor.sendMsg(sensor_data);
			productor.sendMsg(mensaje);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Puerto 9092 Inicia el sensor
	 * 
	 * @param args <IP Broker/Bootstrap-servers> <Puerto Broker/Bootstrap-servers>
	 *             <ID Atraccion>
	 */
	public static void main(String[] args) {

		if (args.length != 3) {
			System.out.println(
					"Error: argumentos invalidos <IP Broker/Bootstrap-servers> <Puerto Broker/Bootstrap-servers> <ID Atraccion>");
			return;
		}

		String bootstrap_server = args[0];
		String bootstrap_server_port = args[1];
		int idAtraccion = Integer.parseInt(args[2]);

		FWQ_Sensor sensor = new FWQ_Sensor(idAtraccion, bootstrap_server, bootstrap_server_port, 0);
		Producer productor = new Producer(FWQ_Sensor.TOPIC, bootstrap_server, bootstrap_server_port,
				"sensor" + idAtraccion, FWQ_Sensor.GROUP_ID);

		new SensorManageWindow(sensor, productor);

	}

	/**
	 * @return the tOPIC
	 */
	public static String getTOPIC() {
		return TOPIC;
	}

	/**
	 * @param tOPIC the tOPIC to set
	 */
	public static void setTOPIC(String tOPIC) {
		TOPIC = tOPIC;
	}

	/**
	 * @return the numPerFila
	 */
	public int getNumPerFila() {
		return numPerFila;
	}

	/**
	 * @param numPerFila the numPerFila to set
	 */
	public void setNumPerFila(int numPerFila) {
		this.numPerFila = numPerFila;
	}

	/**
	 * @return the iDAtraccion
	 */
	public int getIDAtraccion() {
		return IDAtraccion;
	}

	/**
	 * @param iDAtraccion the iDAtraccion to set
	 */
	public void setIDAtraccion(int iDAtraccion) {
		IDAtraccion = iDAtraccion;
	}

	/**
	 * @return the productor
	 */
	public Producer getProductor() {
		return productor;
	}

	/**
	 * @param productor the productor to set
	 */
	public void setProductor(Producer productor) {
		this.productor = productor;
	}

	/**
	 * @return the bootstrap_server
	 */
	public String getBootstrap_server() {
		return bootstrap_server;
	}

	/**
	 * @param bootstrap_server the bootstrap_server to set
	 */
	public void setBootstrap_server(String bootstrap_server) {
		this.bootstrap_server = bootstrap_server;
	}

	/**
	 * @return the bootstrap_server_port
	 */
	public String getBootstrap_server_port() {
		return bootstrap_server_port;
	}

	/**
	 * @param bootstrap_server_port the bootstrap_server_port to set
	 */
	public void setBootstrap_server_port(String bootstrap_server_port) {
		this.bootstrap_server_port = bootstrap_server_port;
	}

}
