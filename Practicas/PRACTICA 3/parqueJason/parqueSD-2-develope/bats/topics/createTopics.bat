@ECHO ON
ECHO Voy a crear todos lso topics...
call kafka-topics --create --zookeeper 127.0.0.1:2181 --replication-factor 1 --partitions 1 --topic MOVIMIENTOS
call kafka-topics --create --zookeeper 127.0.0.1:2181 --replication-factor 1 --partitions 1 --topic MAPA
call kafka-topics --create --zookeeper 127.0.0.1:2181 --replication-factor 1 --partitions 1 --topic INICIARSESION
call kafka-topics --create --zookeeper 127.0.0.1:2181 --replication-factor 1 --partitions 1 --topic SENSORES
call kafka-topics --create --zookeeper 127.0.0.1:2181 --replication-factor 1 --partitions 1 --topic TOKEN
call kafka-topics --create --zookeeper 127.0.0.1:2181 --replication-factor 1 --partitions 1 --topic SALIRPARQUE
ECHO Topics creados! Voy a listarllos:
call kafka-topics --list --zookeeper 127.0.0.1:2181
