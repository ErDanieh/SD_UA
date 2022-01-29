package com.kafkaTest;
import java.io.File;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.log4j.BasicConfigurator;

public class KafkaTest {

	private final static String TOPIC = "SD";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092";

	public static void main(String[] args){
		 
		BasicConfigurator.configure();
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		producer.send(new ProducerRecord<String, String>(TOPIC, "pepe", "coño loco")); // handle the exception
	}

}
