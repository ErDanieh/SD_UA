@ECHO ON
ECHO Voy a crear todos lso topics...
call kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic MOVIMIENTOS
call kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic MAPA
call kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic INICIARSESION
call kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic SENSORES
call kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic TOKEN
ECHO Topics creados! Voy a listarllos:
call kafka-topics --list --zookeeper localhost:2181
