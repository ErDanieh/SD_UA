#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>

int s; /* socket */

void
finalizar (int senyal)
{
	printf("Recibida la señal de fin (cntr-C)\n\r");
	close(s); /* cerrar para que accept termine con un error y salir del bucle principal */
}

int
main (int argc, char *argv[])
{
	char *servidor_puerto;
	char mensaje[1024], respuesta[]="Gracias por tu mensaje";
	struct sockaddr_in dir_servidor, dir_cliente;
	unsigned int long_dir_cliente;
	int s2;
	int n, enviados, recibidos;
	int proceso;
	int contador=0;

	/* Comprobar los argumentos */
	if (argc != 2)
	{
		fprintf(stderr, "Error. Debe indicar el puerto del servidor\r\n");
		fprintf(stderr, "Sintaxis: %s <puerto>\n\r", argv[0]);
		fprintf(stderr, "Ejemplo : %s 8574\"\n\r", argv[0]);
		return 1;
	}

	/* Tomar los argumentos */		
	servidor_puerto = argv[1];

	/**** Paso 1: Abrir el socket ****/

	s = socket(AF_INET, SOCK_STREAM, 0); /* creo el socket */
	if (s == -1)
	{
		fprintf(stderr, "Error. No se puede abrir el socket\n\r");
		return 1;
	}
	printf("Socket abierto\n\r");

	/**** Paso 2: Establecer la dirección (puerto) de escucha ****/

	dir_servidor.sin_family = AF_INET;
	dir_servidor.sin_port = htons(atoi(servidor_puerto));
	dir_servidor.sin_addr.s_addr = INADDR_ANY; /* cualquier IP del servidor */
	if (bind(s, (struct sockaddr *)&dir_servidor, sizeof(dir_servidor)) == -1)
	{
		fprintf(stderr, "Error. No se puede asociar el puerto al servidor\n\r");
		close(s);
		return 1;
	}
	printf("Puerto de escucha establecido\n\r");

	/**** Paso 3: Preparar el servidor para escuchar ****/

	if (listen(s, 4) == -1)
	{
		fprintf(stderr, "Error preparando servidor\n\r");
		close(s);
		return 1;
	}
	printf("Socket preparado\n\r");

	/**** Paso 4: Esperar conexiones ****/

	signal(SIGINT, finalizar);

	while (1)
	{
		fprintf(stderr, "Esperando conexión en el puerto %s...\n\r", servidor_puerto);
		long_dir_cliente = sizeof (dir_cliente);
		s2 = accept (s, (struct sockaddr *)&dir_cliente, &long_dir_cliente);
		contador++;
		/* s2 es el socket para comunicarse con el cliente */
		/* s puede seguir siendo usado para comunicarse con otros clientes */
		if (s2 == -1)
		{
			break; /* salir del bucle */
		}
		/* crear un nuevo proceso para que se pueda atender varias peticiones en paralelo */
		proceso = fork();
		if (proceso == -1) exit(1);
		if (proceso == 0) /* soy el hijo */
		{
			close(s); /* el hijo no necesita el socket general */

			/**** Paso 5: Leer el mensaje ****/

			n = sizeof(mensaje);
			recibidos = read(s2, mensaje, n);
			if (recibidos == -1)
			{
				fprintf(stderr, "Error leyendo el mensaje\n\r");
				exit(1);
			}
			mensaje[recibidos] = '\0'; /* pongo el final de cadena */
			printf("Mensaje recibido [%d]: %s\n\r", recibidos, mensaje);


			/**** Paso 6: Enviar respuesta ****/

			n = strlen(respuesta);
			printf("Enviar respuesta ( %d ) de [ %d bytes ]: %s\n\r", contador, n, respuesta);
			enviados = write(s2, respuesta, n);
			if (enviados == -1 || enviados < n)
			{
				fprintf(stderr, "Error enviando la respuesta (%d)\n\r",enviados);
				close(s);
				return 1;
			}

			printf("Respuesta enviada\n\r");

			close(s2);
			exit(0); /* el hijo ya no tiene que hacer nada */
		}
		else /* soy el padre */
		{
			close(s2); /* el padre no usa esta conexión */
		}

	}

	/**** Paso 7: Cerrar el socket ****/
	close(s);
	printf("Socket cerrado\n\r");
	return 0;
}

