package sc.registry;

import java.net.*;
import sc.registry.Hilo_Registry;

public class FWQ_Registry {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Error: argumentos invalidos <puerto registry> ");
			return;
		}
		/*
		 * Descriptores de socket servidor y de socket con el cliente
		 */
		String puerto = args[1];

		try {

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

				Thread t = new Hilo_Registry(skCliente);
				t.start();
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());

		}
	}
}
