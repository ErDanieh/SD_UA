package gui.visitante;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.*;
import javax.swing.plaf.basic.BasicButtonListener;

import sc.visitantes.FWQ_Visitor;

public class visitorActionWindow extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JFrame ventana;
	JLabel etiTitle;
	JButton inicSession, register, editar;
	private String puertoHosto;// localhost
	private String puertoConn;// 1235
	private String ipBrooker, puertoBrooker, topic, groupID, clientID;
	private FWQ_Visitor visitante;

	public visitorActionWindow(String puertoHost, String puertoSocket, String ipBrooker, String puertoBrooker,
			String topic, String groupID, String clientID, FWQ_Visitor visitante)
	{
		this.visitante = visitante;
		// Conexión localhost y puerto del socket

		this.puertoHosto = puertoHost;
		this.puertoConn = puertoSocket;

		this.ipBrooker = ipBrooker;
		this.puertoBrooker = puertoBrooker;
		this.topic = topic;
		this.groupID = groupID;
		this.clientID = clientID;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setTitle("Welcome To Dismaland");
		this.inicSession = new JButton("Iniciar Sesión");
		this.register = new JButton("Registrar Nuevo Usuario");
		this.editar = new JButton("Editar Usuario");
		this.setLayout(null);
		this.setSize(350, 350);
		this.setVisible(true);
		this.getContentPane().add(inicSession);
		this.getContentPane().add(register);
		this.getContentPane().add(editar);

		inicSession.setBounds(20, 100, 140, 70);
		register.setBounds(170, 100, 140, 70);
		editar.setBounds(170, 175, 140, 70);

		/**
		 * Evento de boton inicio de sesión
		 */
		ActionListener registro = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Crear nueva ventana
				VisitorRegister ventanaRegister = new VisitorRegister(puertoHosto, puertoConn);
			}
		};

		/**
		 * Evento de boton de registro
		 */
		ActionListener iniciosesion = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Crear nueva ventana
				VisitorIniciarSesion ventanaSesion = new VisitorIniciarSesion(ipBrooker, puertoBrooker, topic, groupID,
						clientID, visitante);

			}
		};

		/**
		 * Evento de boton de Edicion de información
		 */
		ActionListener editarInfo = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				VisitorEdit ventanaEditor = new VisitorEdit(puertoConn, puertoHosto);

			}
		};

		inicSession.addActionListener(iniciosesion);
		register.addActionListener(registro);
		editar.addActionListener(editarInfo);

	}

}