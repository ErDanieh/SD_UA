# parqueSD
 La leche de parque

# EJECUTAR APP
## SERVIDORES
```bash
	zookeeper-server-start C:\kafka_2.13-2.8.0\config\zookeeper.properties
	kafka-server-start C:\kafka_2.13-2.8.0\config\server.properties
```
## TOPICS
```bash
	kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic nombre-topic
	kafka-topics --list --zookeeper localhost:2181
```
## PRODUCTOR - CONSUMIDOR
```bash
	kafka-console-producer --broker-list localhost:9092 -topic nombre-topic
	kafka-console-consumer --topic nombre-topic --from-beginning --bootstrap-server localhost:9092
```

## Sistema central
# Registry
Se encarga del registro.
Se conecta por sockets con visitante.
Registry lo añade a la BD

- Ambos tienen que tener leer y escribir socket.
- Si no se puede anñadir el usuario Escribir al visitante Error. Sino exito.


# Visitante
- Crear perfil: Se conecta a FWQ_Registry
- Editar perfil: Se conecta a FWQ_Registry
- Entrar al parque: Se solicita el usuario y la pasword. Se comprueba el aforo mediante FWQ_Engine. Cada usuario tendra un mapa. Se mostraran los mensajes (Mapa actualizado HORA, parque cerrado, cualquier otro mensaje necesario).
- Si se le da a abandonar el parque no aparecera en el mapa. Si cierra la conexion inesperadamente igual.
- Si se loggea se da por hecho que entra al parque. Si no hay hueco mostrar mensaje de error.


# TOPICS
- 
