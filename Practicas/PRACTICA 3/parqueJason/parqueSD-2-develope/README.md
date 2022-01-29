# API_ENGINE
- Crear una API Rest que será la utilizada por los usuarios
para consumir los datos

## Visitantes
- Listar todos los visitantes localhost:3000/visitantes
- Datos de un visitante localhost:3000/visitantes/:id
- Nombre del visitante localhost:3000/visitantes/:id/nombre
- Posicion de un visitante localhost:3000/visitantes/:id/pos
- Visitante en parque localhost:3000/visitantes/:id/enparque

## ATRACCIONES
- Listar todos las atracciones localhost:3000/atracciones
- Informacion de una atraccion localhost:3000/atracciones/:id
- Posicion de una atraccion localhost:3000/atracciones/:id/posicion

## SENSORES
- Listar todos las sensores localhost:3000/sensores
- Informacion de un sensor localhost:3000/sensores/:id
- ID de la atraccion del sensor localhost:3000/sensores/:id/atraccion

## MAPAS
- Listar todos las mapas localhost:3000/mapas
- Informacion de un mapas localhost:3000/mapas/:id

## CASILLAS
- Listar todos las casillas localhost:3000/casillas


# BBDD
- Almacenar los movimientos en la BBDD. Se deberan almacenar 
los datos cada 1 segundo
- Encriptar las contraseñas. Por ejemplo por salt.

# ENGINE

# Registry
- Los usuarios se podran conectar tanto por API como por Socket.
- Encriptar el canal de comunicacion con SSL.

# FRONT WEB
- Crear una pagina web que leera los datos de la API_ENGINE
y los mostrara por pantalla


# VISITANTE
- Un nuevo tipo de visitante que se podrá conectar tanto
por API como por sockets al registry.

- PREGUNTAR COMO SE ELIGE EL METODO DE CONEXION.



# TODO
- Modificar movimientos por BBDD, no por caché.
- Cerrar sectores del mapa.
- Sacar temperatura de las ciudades del archivo.
- Archivo de ciudades para openWeather (Engine). Tiene que estar
constantemente leeyendolo
- Crear el visitante que se conecta por API.
- FrontEnd.

- Certificado autofirmado, https. Seguridad entre visitante y Registry.


# DOCKER - FRONT

docker build -t ps-container:dev .

docker run -it --rm -v ${PWD}:/app -v /app/node_modules -p 3001:3000 -e CHOKIDAR_USEPOLLING=true ps-container:dev
