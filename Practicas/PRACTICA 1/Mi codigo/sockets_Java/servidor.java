import java.io.*;
import java.net.*;

public class servidor {

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

	public int contadorLetras(String p_b) {
		return p_b.length();
	}

	public String FraseIngeniosa(String p_b) {
		int cant = p_b.length();
		String resp = "";

		if (cant == 0)
			resp = " Suelo cocinar con vino, a veces incluso se lo agrego a la comida";
		else {
			if (cant < 10)
				resp = "Más vale tarde, porque por la mañana duermo";
			else {
				if (cant == 13) {
					resp = "Me puedo resistir a todo menos a la tentación";
				}

				if (cant < 20)
					resp = "Señor profesor deje de hacer pruebas exsaustivas";
				else {
					if (cant == 25)
						resp = "El que nace pobre y feo, tiene grandes posibilidades de que al crecer... se le desarrollen ambas condiciones";
					resp = "No soy un completo inútil, por lo menos sirvo de mal ejemplo";
				}
			}
		}

		return resp;
	}

	public String realizarOperacion(String p_Cadena) {
		String[] operacion = p_Cadena.split(",");
		String res = "";

		System.out.println("DUMMERBOT: La operacion es: " + operacion[0]);
		if (operacion.length != 1) {
			System.out.println("DUMMERBOT: La frase es: " + operacion[1]);
			if (operacion[0].compareTo("Contar") == 0) {
				res = Integer.toString(contadorLetras(operacion[1]));
			} else {
				if (operacion[0].compareTo("Bot") == 0) {
					res = FraseIngeniosa(operacion[1]);
				} else {

					res = "-1";
				}
			}
			System.out.println("DUMMERBOT: El resultado es: " + res);
		} else {
			res = "-1";
		}

		return (res);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String resultado = "";
		String Cadena = "";
		String puerto = "";
		try {
			servidor sr = new servidor();
			if (args.length < 1) {
				System.out.println("Debe indicar el puerto de escucha del servidor");
				System.out.println("$./Servidor puerto_servidor");
				System.exit(1);
			}
			puerto = args[0];
			/*
			 * Creas el socket es decir el puerto donde vas a leer
			 * 
			 */
			ServerSocket skServidor = new ServerSocket(Integer.parseInt(puerto));
			System.out.println("Escucho el puerto " + puerto);

			/*
			 * Mantenemos la comunicacion con el cliente
			 */
			for (;;) {
				/*
				 * Se espera un cliente que quiera conectarse
				 */
				Socket skCliente = skServidor.accept(); // Crea objeto
				System.out.println("Sirviendo cliente...");
				while (resultado != "-1") {
					Cadena = sr.leeSocket(skCliente, Cadena);
					/*
					 * Se escribe en pantalla la informacion que se ha recibido del cliente
					 */
					resultado = sr.realizarOperacion(Cadena);
					Cadena = resultado;
					sr.escribeSocket(skCliente, Cadena);
				}

				skCliente.close();
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}

}
