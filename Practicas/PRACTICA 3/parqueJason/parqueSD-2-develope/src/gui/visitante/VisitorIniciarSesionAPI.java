package gui.visitante;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import gui.mapa.MapWindow;
import kf.consumer.Consumer;
import kf.producer.Producer;
import sc.crypto.Crypto;
import sc.visitantes.API_Visitor;
import sc.visitantes.FWQ_Visitor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VisitorIniciarSesionAPI extends JFrame {

	private static final long serialVersionUID = 1L;
	public JFrame ventana;
	JLabel etiTitle, etiId, etiPassword, etiNombre, ErroMsg;
	JTextField txtId, txtPassword, txtNombre;
	JButton accept;
	API_Visitor visitante;
	/**
	 * Hora a la que se solicita iniciar sesion. Se utilizara para comprobar que no
	 * es una solicitud anterior.
	 */
	public String horaSolInicio;

	String ipBrooker, puertoBrooker, topic, groupID, clientID;

	public VisitorIniciarSesionAPI(String ipBrooker, String puertoBrooker, String topic, String groupID,
			String clientID, API_Visitor visitante) {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.visitante = visitante;

		this.setTitle("Registro en Dismaland");
		this.accept = new JButton("Aceptar");
		this.etiTitle = new JLabel("Entrar en el Parque:");
		this.etiId = new JLabel("Introduce tu ID:");
		this.etiPassword = new JLabel("Introduce tu contraseña:");
		this.ErroMsg = new JLabel("");

		this.ipBrooker = ipBrooker;
		this.puertoBrooker = puertoBrooker;
		this.topic = topic;
		this.groupID = groupID;
		this.clientID = clientID;

		this.txtId = new JTextField();
		this.txtPassword = new JTextField();

		// setContentPane(new JLabel(new ImageIcon("src/gui/resources/DisneyMal.jpg")));

		this.setLayout(null);
		this.setSize(450, 450);
		this.setVisible(true);
		this.getContentPane().add(accept);
		this.getContentPane().add(ErroMsg);

		this.getContentPane().add(etiTitle);
		this.getContentPane().add(etiId);
		this.getContentPane().add(etiPassword);

		this.getContentPane().add(txtId);
		this.getContentPane().add(txtPassword);

		accept.setBounds(200, 175, 140, 70);
		etiTitle.setBounds(0, 10, 140, 70);
		etiId.setBounds(20, 25, 140, 70);
		etiPassword.setBounds(200, 25, 140, 70);

		ErroMsg.setBounds(25, 175, 225, 70);
		txtId.setBounds(20, 75, 140, 70);
		txtPassword.setBounds(200, 75, 140, 70);

		Random rand = new Random();
		int numb = rand.nextInt(100000);
		Consumer consumer = new Consumer("TOKEN", ipBrooker, puertoBrooker, clientID, groupID + numb);
		Producer producer = new Producer(topic, ipBrooker, puertoBrooker, "proCli", "proCli");
		/**
		 * Evento de boton de registro
		 */
		ActionListener iniciarSesion = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtId.getText().isEmpty() || txtPassword.getText().isEmpty()) {
					ErroMsg.setText("Introduzca todos los datos.");
				} else {
					horaSolInicio = java.time.LocalDate.now().toString();
					ErroMsg.setText("");

					Crypto cry = new Crypto();
					cry.addKey("SDESGUAY");
					String mensaje = null;
					mensaje = cry.encriptar(txtId.getText() + " " + txtPassword.getText());

					producer.sendMsg(mensaje, horaSolInicio);

					String tokenResult = VisitorIniciarSesionAPI.consumerIniciarSesion(consumer, txtId.getText(),
							horaSolInicio);
					switch (tokenResult) {
					case "ERROR":
						setMensajeError();
						break;
					case "PARQUELLENO":
						setMensajeLleno();
						break;
					default:
						visitante.setIdVisitor(txtId.getText().toString());
						visitante.setTokenSesion(tokenResult);
						FWQ_Visitor borrarNose = new FWQ_Visitor();
						MapWindow mapWindow = new MapWindow(borrarNose);
						mapWindow.mostrarVentana(borrarNose);
						break;
					}
				}
			}
		};

		accept.addActionListener(iniciarSesion);
	}

	/**
	 * Setea el mensaje de error parque lleno
	 */
	public void setMensajeLleno() {
		this.ErroMsg.setText("Parque lleno.");
	}

	/**
	 * Setea el mensaje de Error
	 */
	public void setMensajeError() {
		this.ErroMsg.setText("Contraseña o ID no validos");
	}

	/**
	 * Comprueba si el token recibido es valido.
	 * 
	 * @param consumer Consumidor
	 * @return Si se ha iniciado correctamente sesion
	 */
	public static String consumerIniciarSesion(Consumer consumer, String idVisitante, String horaSolInicio) {

		String resultado = "ERROR";
		consumer.consumer.subscribe(Arrays.asList(consumer.topic));

		consumer.setRecords(consumer.getConsumer().poll(Duration.ofMillis(1000)));
		for (ConsumerRecord record : consumer.getRecords()) {
			consumer.getLogger()
					.info("Nuevo mensaje recibido:" + "Key: " + record.key() + "Valor: " + record.value() + "Topic: "
							+ record.topic() + "Partition: " + record.partition() + "Offset: " + record.offset()
							+ "\n");

			try {

				Crypto cry = new Crypto();
				cry.addKey("SDESGUAY");
				String mensaje = null;
				mensaje = cry.desencriptar(record.value().toString());

				// String[] lineSplit = record.value().toString().split(" ");
				String[] lineSplit = mensaje.split(" ");
				String idUsuario = lineSplit[0];
				String token = lineSplit[1];

				if (!idUsuario.equals(idVisitante) || !record.key().toString().equals(horaSolInicio)) {
					continue;
				}
				switch (token) {

				case "ERROR":
					resultado = "ERROR";
					return "ERROR";
				case "PARQUELLENO":
					resultado = "PARQUELLENO";
					return "PARQUELLENO";
				default:
					return token;
				}

			} catch (Exception e) {
			}
		}
		return resultado;

	}
}