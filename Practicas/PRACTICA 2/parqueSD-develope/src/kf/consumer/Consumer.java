package kf.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kf.producer.Producer;

public class Consumer {

	private final static String CONF_FILE = "C:\\kafka_2.13-2.8.0\\config\\log4j.properties";
	public String topic = "";
	private String bootstrap_server = "";
	private String bootstrap_server_port = "";
	private Logger logger = LoggerFactory.getLogger(Producer.class);
	public Properties props;
	private String client_id = "";
	private String group_id = "";
	public KafkaConsumer<String, String> consumer;
	private ConsumerRecords<String, String> records;
	private Long key_record = 1L;

	public Consumer(String topic, String bootstrap_server, String bootstrap_server_port, String client_id,
			String group_id) {
		this.topic = topic;
		this.bootstrap_server = bootstrap_server;
		this.bootstrap_server_port = bootstrap_server_port;
		this.client_id = client_id;
		this.group_id = group_id;

		PropertyConfigurator.configure(Consumer.CONF_FILE);
		this.props = new Properties();

		props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				this.bootstrap_server + ":" + this.bootstrap_server_port);
		props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.group_id);
		props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.setProperty("enable.auto.commit", Boolean.toString(true));
		props.setProperty("auto.commit.interval.ms", Long.toString(100));

		PropertyConfigurator.configure(Consumer.CONF_FILE);

		this.consumer = new KafkaConsumer<String, String>(this.props);
		// this.consumer.subscribe(Arrays.asList(this.topic));
	}

	public Consumer(String topic, String bootstrap_server, String bootstrap_server_port, String client_id,
			String group_id, boolean latest) {
		this.topic = topic;
		this.bootstrap_server = bootstrap_server;
		this.bootstrap_server_port = bootstrap_server_port;
		this.client_id = client_id;
		this.group_id = group_id;

		PropertyConfigurator.configure(Consumer.CONF_FILE);

		this.props = new Properties();
		props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				this.bootstrap_server + ":" + this.bootstrap_server_port);
		props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.group_id);
		props.setProperty("enable.auto.commit", Boolean.toString(true));
		props.setProperty("auto.commit.interval.ms", Long.toString(100));
		if (latest) {
			props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		} else
			props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		PropertyConfigurator.configure(Consumer.CONF_FILE);

		this.consumer = new KafkaConsumer<String, String>(this.props);
		// this.consumer.subscribe(Arrays.asList(this.topic));
	}

	public void runConsumer() {

		while (true) {
			// records
			this.records = consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord record : this.records) {
				logger.info("Nuevo mensaje recibido:" + "Key: " + record.key() + "Valor: " + record.value() + "Topic: "
						+ record.topic() + "Partition: " + record.partition() + "Offset: " + record.offset() + "\n");
			}
		}
	}

	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the bootstrap_server
	 */
	public String getBootstrap_server() {
		return bootstrap_server;
	}

	/**
	 * @param bootstrap_server the bootstrap_server to set
	 */
	public void setBootstrap_server(String bootstrap_server) {
		this.bootstrap_server = bootstrap_server;
	}

	/**
	 * @return the bootstrap_server_port
	 */
	public String getBootstrap_server_port() {
		return bootstrap_server_port;
	}

	/**
	 * @param bootstrap_server_port the bootstrap_server_port to set
	 */
	public void setBootstrap_server_port(String bootstrap_server_port) {
		this.bootstrap_server_port = bootstrap_server_port;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @return the props
	 */
	public Properties getProps() {
		return props;
	}

	/**
	 * @param props the props to set
	 */
	public void setProps(Properties props) {
		this.props = props;
	}

	/**
	 * @return the client_id
	 */
	public String getClient_id() {
		return client_id;
	}

	/**
	 * @param client_id the client_id to set
	 */
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	/**
	 * @return the group_id
	 */
	public String getGroup_id() {
		return group_id;
	}

	/**
	 * @param group_id the group_id to set
	 */
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	/**
	 * @return the consumer
	 */
	public KafkaConsumer<String, String> getConsumer() {
		return consumer;
	}

	/**
	 * @param consumer the consumer to set
	 */
	public void setConsumer(KafkaConsumer<String, String> consumer) {
		this.consumer = consumer;
	}

	/**
	 * @return the records
	 */
	public ConsumerRecords<String, String> getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(ConsumerRecords<String, String> records) {
		this.records = records;
	}

	/**
	 * @return the key_record
	 */
	public Long getKey_record() {
		return key_record;
	}

	/**
	 * @param key_record the key_record to set
	 */
	public void setKey_record(Long key_record) {
		this.key_record = key_record;
	}

	/**
	 * @return the confFile
	 */
	public static String getConfFile() {
		return CONF_FILE;
	}

}
