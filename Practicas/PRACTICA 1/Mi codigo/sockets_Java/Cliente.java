import java.io.*;
import java.net.*;

public class Cliente {

	/*
	* Lee datos del socket. Supone que se le pasa un buffer con hueco 
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
	*/
	public String leeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			InputStream aux = p_sk.getInputStream();
			DataInputStream flujo = new DataInputStream( aux );
			p_Datos = flujo.readUTF();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
      return p_Datos;
	}

	/*
	* Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
	* o -1 si hay error.
	*/
	public void escribeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			OutputStream aux = p_sk.getOutputStream();
			DataOutputStream flujo= new DataOutputStream( aux );
			flujo.writeUTF(p_Datos);      
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
		return;
	}
	
	
	
	public String pedirNumeros(String p_operacion, String p_resultado, String p_Cadena, Socket p_Socket_Con_Servidor)
	{
		String analizar = "";
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader (isr);
		try
		{
			/*
			Pedimos una cadena para analizar
			*/
			while (analizar == "")
			{
				System.out.println("Introduzca la frase");
				analizar = br.readLine();
			}
	
			p_Cadena = p_operacion + "," + analizar; 
			escribeSocket (p_Socket_Con_Servidor, p_Cadena);
			p_Cadena = "";
			p_Cadena = leeSocket (p_Socket_Con_Servidor, p_Cadena);
		 	p_resultado = p_Cadena;
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
		return p_resultado;
	}
	
	public void pedirOperacion(String p_host, String p_puerto)
	{
		int operacion;
		int salir = 0;
		String resultado = "";
		char resp = 'x';
		/*
		* Descriptor del socket y buffer para datos
		*/
		String Cadena = "";
		String  op = "";
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader (isr);
			
		/*
		* Se abre la conexion con el servidor, pasando el nombre del ordenador
		* y el servicio solicitado.
		*/
		try
		{
			//Aqu?? podriamos poner un timeout por si la conexi??n falla
			Socket skCliente = new Socket(p_host, Integer.parseInt(p_puerto));
			while (salir == 0)
			{
				operacion = 0;
				while (operacion !=1 && operacion !=2)
				{
					System.out.println("[1] Contar letras");
					System.out.println("[2] Respuesta Graciosa");
					System.out.println("Indica la operacion a realizar: ");
					operacion = Integer.parseInt(br.readLine());
				}
				if (operacion == 1)
					op = "Contar";
				else
					op = "Bot";
				resultado = pedirNumeros(op, resultado, Cadena, skCliente);
				resp='x';
				while(resp != 's' && resp != 'n')
				{
					System.out.println("El resultado es: " + resultado);
					System.out.println("Desea realizar otra operacion? [s,n]: ");
					resp = br.readLine().charAt(0);					
				}
				if (resp != 's')
				{
					salir = 1;
					/*
					* Se cierra el socket con el servidor
					*/
					
					escribeSocket (skCliente, "fin");
					
					Cadena = leeSocket (skCliente, Cadena);
					resultado = Cadena;
		 			
					if (resultado == "-1")
					{
						skCliente.close();
						System.out.println("Conexion cerrada.");
						System.exit(0);	
					}
				}
				Cadena = "";
				op = "";
			}
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
		return;		
	}
	
	
	public void menu(String p_host, String p_puerto)
	{
		int opc = 0;
		try
		{
			
			while (opc!=1 && opc!=2)
			{
				System.out.println("Bienvenido a DUMMERBOT");
				System.out.println("[1] Decirle algo al bot\n");
				System.out.println("[2] Salir");
				System.out.println("Indique la opcion a realizar:");
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader (isr);
				opc = Integer.parseInt(br.readLine());
			}
			if (opc == 1)
			{
				pedirOperacion(p_host,p_puerto);
			}
			else
			{
				System.exit(0);
			}
		}catch(Exception e)
		{
			System.out.println("Error " + e.toString());
		}
		return;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Cliente cl = new Cliente();
		int i = 0;
		String host;
		String puerto;
		if (args.length < 2) {
			System.out.println ("Debe indicar la direccion del servidor y el puerto");
			System.out.println ("$./Cliente nombre_servidor puerto_servidor");
			System.exit(-1);
		}
		host = args[0];
		puerto = args[1];

		while(i==0)
		{
			cl.menu(host,puerto);
		}
	}

}
