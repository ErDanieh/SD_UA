package com.kafkaTest;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class testConsumer {

	private final static String TOPIC = "SD";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092";

	public static void main(String[] args) {
		String configFile = "C:\\kafka_2.13-2.8.0\\config\\log4j.properties";
		// Create logger for class
		final Logger logger = LoggerFactory.getLogger(testConsumer.class);
		// Create variables for strings
		final String bootstrapServers = "127.0.0.1:9092";
		final String consumerGroupID = "consumidor";
		// Create and populate properties object
		PropertyConfigurator.configure(configFile);
		
		Properties p = new Properties();
		
		p.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		p.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		p.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		p.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupID);
		p.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		
		PropertyConfigurator.configure(configFile);

		// Create consumer
		final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(p);
		// Subscribe to topic(s)
		consumer.subscribe(Arrays.asList("INICIARSESION"));
		// Poll and Consume records
		while (true) {
			// records
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
			for(ConsumerRecord record: records) {
				logger.info("Nuevo mensaje recibido:" +
						"Key: " + record.key() +
						"Valor: " + record.value() +
						"Topic: " + record.topic() +
						"Partition: " + record.partition() +
						"Offset: " + record.offset() +
						"\n"
			);
			}
		}

	}

}
