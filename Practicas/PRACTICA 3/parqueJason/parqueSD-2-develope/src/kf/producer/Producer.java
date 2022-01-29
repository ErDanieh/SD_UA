package kf.producer;

import java.util.Properties;

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

import sc.crypto.Crypto;

public class Producer {
	private String topic = "";
	private String bootstrap_server = "";
	private String bootstrap_server_port = "";
	private Logger logger = LoggerFactory.getLogger(Producer.class);
	private Properties props;
	private String client_id = "";
	private String group_id = "";
	private KafkaProducer<String, String> producer;
	private ProducerRecord<String, String> record;
	private Long key_record = 1L;

	/**
	 * Constructor sobrecargado
	 *
	 * @param topic                 topic
	 * @param bootstrap_server      ip servidor
	 * @param bootstrap_server_port puerto del servidor [9092]?
	 * @param client_id             nombre identificativo del cliente
	 * @param group_id              nombre del grupo
	 */
	public Producer(String topic, String bootstrap_server, String bootstrap_server_port, String client_id,
			String group_id) {
		this.topic = topic;
		this.bootstrap_server = bootstrap_server;
		this.bootstrap_server_port = bootstrap_server_port;
		this.client_id = client_id;
		this.group_id = group_id;
		BasicConfigurator.configure();
		this.props = new Properties();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrap_server + ":" + this.bootstrap_server_port);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, this.client_id);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, this.group_id);

		this.producer = new KafkaProducer<>(props);

	}

	/**
	 * Mandador de mensajes. Muestra por consola la información del mensaje enviado
	 *
	 * @param mensaje mensaje
	 */
	public void sendMsg(String mensaje) {
		
		//Crypto cry = new Crypto();
		//cry.addKey("SDESGUAY");
		//mensaje = cry.encriptar(mensaje);		
		this.record = new ProducerRecord<>(this.topic, this.key_record.toString(), mensaje);

		this.producer.send(record);// , new Callback()
		this.producer.flush();
		/*
		 * {
		 * 
		 * @Override public void onCompletion(RecordMetadata recordMetadata, Exception
		 * e) {
		 * 
		 * if (e == null) { logger.info("\nMensaje recibido. \n" + "Topic: " +
		 * recordMetadata.topic() + ". Particion: " + recordMetadata.partition() +
		 * ". Offset: " + recordMetadata.offset() + " @ Timestamp: " +
		 * recordMetadata.timestamp()); } else { logger.error("Error Ocurred: ", e);
		 * sendMsg(mensaje); } } });
		 */
	}

	/**
	 * Envia un mensake con una key especifica
	 * 
	 * @param mensaje Mensaje a enviar
	 * @param key     Key del mensaje
	 */
	public void sendMsg(String mensaje, String key) {
		
		//Crypto cry = new Crypto();
		//cry.addKey("SDESGUAY");
		//mensaje = cry.encriptar(mensaje);	
		this.record = new ProducerRecord<>(this.topic, key, mensaje);

		this.producer.send(record);// , new Callback()
		this.producer.flush();
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getBootstrap_server() {
		return bootstrap_server;
	}

	public void setBootstrap_server(String bootstrap_server) {
		this.bootstrap_server = bootstrap_server;
	}

	public String getBootstrap_server_port() {
		return bootstrap_server_port;
	}

	public void setBootstrap_server_port(String bootstrap_server_port) {
		this.bootstrap_server_port = bootstrap_server_port;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public KafkaProducer<String, String> getProducer() {
		return producer;
	}

	public void setProducer(KafkaProducer<String, String> producer) {
		this.producer = producer;
	}

	public ProducerRecord<String, String> getRecord() {
		return record;
	}

	public void setRecord(ProducerRecord<String, String> record) {
		this.record = record;
	}

	public Long getKey_record() {
		return key_record;
	}

	public void setKey_record(Long key_record) {
		this.key_record = key_record;
	}

	public Logger getLogger() {
		return logger;
	}
}
