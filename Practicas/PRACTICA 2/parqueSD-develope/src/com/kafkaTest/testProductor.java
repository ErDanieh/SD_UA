package com.kafkaTest;

import java.sql.Time;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kf.producer.Producer;
import sc.sensores.FWQ_Sensor;

public class testProductor {

	private final static String TOPIC = "SENSORES";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092";

	public static void main(String[] args) throws InterruptedException {

		Producer productor = new Producer(TOPIC, "127.0.0.1", "9092", "test1", TOPIC);
		Producer productorMovimientos = new Producer("MOVIMIENTOS", "127.0.0.1", "9092", "visitorMov", "visitorMov2");
		Producer productorMovimientos1 = new Producer("MOVIMIENTOS", "127.0.0.1", "9092", "visitorMov", "visitorMov2");

		Thread threadKafkaToken = new Thread() {
			public void run() {
				try {
					enviarMnsg(productorMovimientos);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Thread threadKafkaToken2 = new Thread() {
			public void run() {
				try {
					enviarMnsg2(productorMovimientos1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		threadKafkaToken2.start();

	}

	public static void enviarMnsg2(Producer productor) throws InterruptedException {

		for (;;) {

			TimeUnit.SECONDS.sleep(10);
			productor.sendMsg("22222123412341234123412341234132");
		}

		// System.out.println("Mensjae supuestamente enviado");
	}

	public static void enviarMnsg(Producer productor) throws InterruptedException {

		productor.sendMsg("1111111111111");

		// System.out.println("Mensjae supuestamente enviado");
	}

}
