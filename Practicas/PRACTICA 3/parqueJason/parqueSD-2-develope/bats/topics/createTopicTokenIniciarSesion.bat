@ECHO ON
ECHO Voy a crear todos lso topics...
call kafka-topics --create --zookeeper 127.0.0.1:2181 --replication-factor 1 --partitions 1 --topic TOKEN
