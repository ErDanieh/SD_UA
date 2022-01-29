package gui.sensor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import kf.producer.Producer;
import sc.sensores.FWQ_Sensor;

public class SensorManageWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	public JFrame ventana;
	JLabel etiTitle, etiId;
	JTextField txtNumber;
	JButton accept, start, stop;

	public SensorManageWindow(FWQ_Sensor sensor, Producer productor) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setTitle("Sensor Manager");
		this.accept = new JButton("Enviar dato");
		this.start = new JButton("Start");
		this.stop = new JButton("Stop");

		this.etiId = new JLabel("Id de atracción: " + sensor.getIDAtraccion());
		this.txtNumber = new JTextField();

		this.setLayout(null);
		this.setSize(450, 450);
		this.setVisible(true);

		this.getContentPane().add(etiId);
		this.getContentPane().add(txtNumber);
		this.getContentPane().add(accept);
		this.getContentPane().add(start);
		this.getContentPane().add(stop);

		etiId.setBounds(100, 10, 140, 70);
		accept.setBounds(200, 95, 140, 70);
		txtNumber.setBounds(50, 95, 140, 70);

		start.setBounds(50, 175, 140, 70);
		stop.setBounds(200, 175, 140, 70);

		int numPersManual = 10;

		String msg = "13";
		Thread threadSendData = new Thread() {

			public void run() {
				sensor.sendProducerMsg(productor, "" + msg);

			}
		};
		Thread threadStartSensor = new Thread() {

			public void run() {
				sensor.startProducer(productor);

			}
		};

		ActionListener Aceptar = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int num = Integer.parseInt(txtNumber.getText());
					sensor.sendProducerMsg(productor, "" + num);
				} catch (Exception e2) {
					System.out.println("[SENSOR-WINDOW] El valor introducido no es un numero valido.");
				}

			}
		};

		ActionListener stopper = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				threadStartSensor.suspend();

			}
		};

		ActionListener starter = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (threadStartSensor.isAlive()) {
					threadStartSensor.resume();
				} else {

					threadStartSensor.start();
				}

			}
		};

		start.addActionListener(starter);

		stop.addActionListener(stopper);

		accept.addActionListener(Aceptar);

	}

}
