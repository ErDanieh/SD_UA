package gui.visitante;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.*;
import javax.swing.plaf.basic.BasicButtonListener;

import sc.visitantes.API_Visitor;
import sc.visitantes.FWQ_Visitor;

public class visitorActionWindowAPI extends JFrame
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
	private API_Visitor visitante;

	public visitorActionWindowAPI( String ipBrooker, String puertoBrooker,
			String topic, String groupID, String clientID, API_Visitor visitante, String url)
	{
		this.visitante = visitante;
		// Conexión localhost y puerto del socket



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
				VisitorRegisterAPI ventanaRegister = new VisitorRegisterAPI(visitante.url);
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
				VisitorIniciarSesionAPI ventanaSesion = new VisitorIniciarSesionAPI(ipBrooker, puertoBrooker, topic, groupID,
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
				VisitorEditAPI ventanaEditor = new VisitorEditAPI(url);

			}
		};

		inicSession.addActionListener(iniciosesion);
		register.addActionListener(registro);
		editar.addActionListener(editarInfo);

	}

}
