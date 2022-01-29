package gui.visitante;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import sc.visitantes.FWQ_Visitor;

public class VisitorRegister extends JFrame
{
	private static final long serialVersionUID = 1L;
	public JFrame ventana;
	JLabel etiTitle,etiId,etiPassword,etiNombre;
	JTextField txtId,txtPassword,txtNombre;
	JButton accept;
	private String puertoHosto;//localhost
	private String puertoConn;//1235

	public VisitorRegister(String puertoHost, String puertoSocket)
	{
		//Conexión localhost y puerto del socket
		this.puertoConn=puertoSocket;
		this.puertoHosto = puertoHost;

		FWQ_Visitor visitante = new FWQ_Visitor();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		this.setTitle("Registro en Dismaland");
		this.accept = new JButton("Aceptar");
		this.etiTitle = new JLabel("Registro en el Parque:");
		this.etiId = new JLabel("Introduce tu ID:");
		this.etiPassword = new JLabel("Introduce tu contraseña:");
		this.etiNombre = new JLabel("Introduce tu Nombre");
		this.txtId = new JTextField();
		this.txtPassword = new JTextField();
		this.txtNombre = new JTextField();
		//setContentPane(new JLabel(new ImageIcon("src/gui/resources/DisneyMal.jpg")));

		this.setLayout(null);
		this.setSize(450, 450);
		this.setVisible(true);
		this.getContentPane().add(accept);
		
		this.getContentPane().add(etiTitle);
		this.getContentPane().add(etiId);
		this.getContentPane().add(etiPassword);
		this.getContentPane().add(etiNombre);
		
		this.getContentPane().add(txtNombre);
		this.getContentPane().add(txtId);
		this.getContentPane().add(txtPassword);
		
		
		
		accept.setBounds(200, 175, 140, 70);
		etiTitle.setBounds(0, 10, 140, 70);
		etiId.setBounds(20, 25, 140, 70);
		etiPassword.setBounds(200, 25, 140, 70);
		etiNombre.setBounds(20, 120, 140, 70);
		
		txtId.setBounds(20, 75, 140, 70);
		txtPassword.setBounds(200, 75, 140, 70);
		txtNombre.setBounds(20, 175, 140, 70);
		
		
		/**
		 * Evento de boton de registro
		 */
		ActionListener registro = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Crear nueva ventana
				visitante.pasarServidorRegistro(puertoHosto,puertoConn,txtId.getText(),txtPassword.getText(),txtNombre.getText());
				
			}
		};
		
		accept.addActionListener(registro);
		
		

	}

}
