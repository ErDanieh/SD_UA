package sc.registry;

import java.lang.Exception;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.io.*;

public class Hilo_Registry extends Thread {

	private Socket skCliente;

	String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	String DB_URL = "jdbc:mysql://remotemysql.com:3306/UFD6yb2Mz3";
	String USER = "root";
	String PASS = "new-password";

	public Hilo_Registry(Socket p_cliente) {
		this.skCliente = p_cliente;
	}

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

	public String registrar(String p_id, String p_password, String p_nombre) throws ClassNotFoundException {

		Random obj = new Random();
		int rand_num = obj.nextInt(0xffffff + 1);
		// format it as hexadecimal string and print
		// String colorCode = String.format("#%06x", rand_num);

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(DB_URL, "UFD6yb2Mz3", "TcV9ViEEPN");
			Statement stmt = conn.createStatement();

			// System.out.println("Inserting records into the table...");
			String sql = "INSERT INTO Visitantes (ID,Nombre,Password,Color) values ('" + p_id + "', '" + p_nombre
					+ "', '" + p_password + "','" + "F" + "')";

			stmt.executeUpdate(sql);

			System.out.println("Exito! Conectado a la base de datos.");

			return "all done";

		} catch (SQLException e) {
			System.out.println("Nooooozzz!");
			e.printStackTrace();
		}
		return "nop";
	}

	public String editarInfo(String p_id, String p_nombre, String p_password, String nuevoId, String nuevoNombre,
			String nuevoPasswd) throws ClassNotFoundException {

		try {
			System.out.println("id ahora: " + p_id + "nombre ahora: " + p_nombre + "password ahora: " + p_password
					+ "nuevo id ahora: " + nuevoId);

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(DB_URL, "UFD6yb2Mz3", "TcV9ViEEPN");
			Statement stmt = conn.createStatement();

			String sql = "UPDATE Visitantes SET ID = '" + nuevoId + "', Nombre = '" + nuevoNombre + "', Password = '"
					+ nuevoPasswd + "' WHERE ID = '" + p_id + "' AND Nombre = '" + p_nombre + "' AND Password = '"
					+ p_password + "'";

			stmt.executeUpdate(sql);

			return "all done";
		} catch (SQLException e) {
			System.out.println("Nooooozzz!");
			e.printStackTrace();
		}
		return "nop";
	}

	public String realizarOperacion(String p_Cadena) throws ClassNotFoundException {
		String[] operacion = p_Cadena.split(",");
		String res = null;

		for (int i = 0; i < operacion.length; i++) {
			System.out.println(operacion[i]);
		}

		if (operacion.length != 1) {

			if (operacion[0].compareTo("regi") == 0) {
				res = registrar(operacion[1], operacion[2], operacion[3]);
			} else if (operacion[0].compareTo("edit") == 0) {
				res = editarInfo(operacion[1], operacion[2], operacion[3], operacion[4], operacion[5], operacion[6]);
			}

			System.out.println(res);
		} else {
			res = null;
		}
		return (res);
	}

	public void run() {
		String resultado = "";
		String Cadena = "";

		try {

			Cadena = this.leeSocket(skCliente, Cadena);
			System.out.println(Cadena);
			/*
			 * Se escribe en pantalla la informacion que se ha recibido del cliente
			 */
			resultado = this.realizarOperacion(Cadena);
			Cadena = "" + resultado;
			this.escribeSocket(skCliente, Cadena);

			skCliente.close();
			System.out.println("Hilo Cerrado");
			// System.exit(0); No se debe poner esta sentencia, porque en ese caso el primer
			// cliente que cierra rompe el socket
			// y desconecta a todos
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}
}
