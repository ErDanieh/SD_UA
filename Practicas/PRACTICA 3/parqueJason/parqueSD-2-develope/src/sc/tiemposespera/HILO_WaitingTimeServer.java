package sc.tiemposespera;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import dt.atracciones.Atracciones;

/**
 * La clase HILO_WaitingTimeServer es la encargada de atender a las peticiones
 * 
 */
public class HILO_WaitingTimeServer extends Thread {

	/**
	 * Socket del cliente
	 */
	private Socket skCliente;

	/**
	 * Cache de los datos de sensores y atraccioens
	 */
	private Map<Integer, Atracciones> sensoresAtracciones; // ID Sensor, Atracciones

	public HILO_WaitingTimeServer(Socket p_cliente, Map<Integer, Atracciones> sensoresAtracciones) {
		this.skCliente = p_cliente;
		this.sensoresAtracciones = new HashMap<Integer, Atracciones>(sensoresAtracciones);

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
	 * Calcula los nuevos tiempos
	 * 
	 * @return String con los nuevos tiempos
	 */
	public String enviarDatosTiempos() {

		System.out.println("[HILO] Realizando actualizacionDeTiempos...");

		String dt_datos = "";

		for (Integer idSensor : this.sensoresAtracciones.keySet()) {
			Atracciones atrac = this.sensoresAtracciones.get(idSensor);

			dt_datos += atrac.getID() + "," + atrac.getNombre() + "," + atrac.getnTurno() + "," + atrac.getnCola() + ","
					+ atrac.getnTurno() + "," + atrac.gettEspera() + "," + atrac.getPosFila() + "," + atrac.getPosCol()
					+ ";";
		}
		System.out.println("[HILO] Actualizacion de tiempos realizada con exito...");
		return dt_datos;
	}

	/**
	 * Devuelve una respuesta en funcion a la operacion seleccionada.
	 * 
	 * @param p_Cadena Operacion a realizar
	 * @return Resultado de la operacion
	 */
	public String seleccionarOperacion(String p_Cadena) {
		String operacion = p_Cadena;
		String res = "";

		switch (operacion) {
		case "actualizacionDeTiempos": // Envia la informacion de tiempos
			res = enviarDatosTiempos();
			break;

		default: // Si no existe la opcion devuelve mensje de error.
			res = "[HILO] Opcion no valida";
			break;
		}

		return res;
	}

	public void run() {
		String resultado = "";
		String Cadena = "";

		try {

			Cadena = this.leeSocket(skCliente, Cadena);
			resultado = this.seleccionarOperacion(Cadena);
			Cadena = "" + resultado;
			this.escribeSocket(skCliente, Cadena);

			skCliente.close();
			// System.exit(0); No se debe poner esta sentencia, porque en ese caso el primer
			// cliente que cierra rompe el socket
			// y desconecta a todos
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}
}
