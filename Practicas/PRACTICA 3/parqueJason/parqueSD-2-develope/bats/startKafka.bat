start cmd /k zookeeper-server-start C:\kafka_2.13-2.8.0\config\zookeeper.properties
timeout 10
start cmd /k kafka-server-start C:\kafka_2.13-2.8.0\config\server.properties 
