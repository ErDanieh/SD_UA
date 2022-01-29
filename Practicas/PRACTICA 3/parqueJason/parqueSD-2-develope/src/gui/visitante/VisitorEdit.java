package gui.visitante;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import sc.visitantes.FWQ_Visitor;

public class VisitorEdit extends JFrame
{

	private static final long serialVersionUID = 1L;
	public JFrame ventana;
	JLabel etiTitle, etiId, etiPassword, etiNombre;
	JTextField txtId, txtPassword, txtNombre;
	
	JLabel etiIdNew, etiPasswordNew, etiNombreNew;
	JTextField txtIdNew, txtPasswordNew, txtNombreNew;
	
	JButton accept;
	private String puertoHosto;// localhost
	private String puertoConn;// 1235
	
	
	public VisitorEdit(String puertoSocket, String puertoHost) {
		//Conexiones de los puertos
		this.puertoConn=puertoSocket;
		this.puertoHosto = puertoHost;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		FWQ_Visitor visitante = new FWQ_Visitor();
		//Datos Viejos
		this.setTitle("Editar Perfil");
		this.accept = new JButton("Aceptar");
		this.etiTitle = new JLabel("Editar Credenciales:");
		this.etiId = new JLabel("ID Actual:");
		this.etiPassword = new JLabel("Contraseña actual:");
		this.etiNombre = new JLabel("Nombre Actual:");
		this.txtId = new JTextField();
		this.txtPassword = new JTextField();
		this.txtNombre = new JTextField();
		
		//Para nuevos datos
		this.etiIdNew = new JLabel("ID Nuevo:");
		this.etiPasswordNew = new JLabel("Contraseña Nueva:");
		this.etiNombreNew = new JLabel("Nombre Nuevo:");
		this.txtIdNew = new JTextField();
		this.txtPasswordNew = new JTextField();
		this.txtNombreNew = new JTextField();
		//setContentPane(new JLabel(new ImageIcon("src/gui/resources/DisneyMal.jpg")));

		this.setLayout(null);
		this.setSize(450, 560);
		this.setVisible(true);
		this.getContentPane().add(accept);
		
		//Datos Actuales
		this.getContentPane().add(etiTitle);
		this.getContentPane().add(etiId);
		this.getContentPane().add(etiPassword);
		this.getContentPane().add(etiNombre);
		
		this.getContentPane().add(txtNombre);
		this.getContentPane().add(txtId);
		this.getContentPane().add(txtPassword);
		
		//Datos Viejos
		this.getContentPane().add(etiIdNew);
		this.getContentPane().add(etiPasswordNew);
		this.getContentPane().add(etiNombreNew);
		
		this.getContentPane().add(txtNombreNew);
		this.getContentPane().add(txtIdNew);
		this.getContentPane().add(txtPasswordNew);
		
		
		//Datos Actuales
		accept.setBounds(150, 400, 120, 70);
		etiTitle.setBounds(0, 10, 180, 70);
		etiId.setBounds(20, 25, 180, 70);
		etiPassword.setBounds(20, 220, 180, 70);
		etiNombre.setBounds(20, 120, 140, 70);
		txtId.setBounds(20, 75, 140, 70);
		txtPassword.setBounds(20, 280, 140, 70);
		txtNombre.setBounds(20, 175, 140, 70);
		
		//Datos Nuevos
		etiIdNew.setBounds(200, 25, 180, 70);
		etiPasswordNew.setBounds(200, 220, 180, 70);
		etiNombreNew.setBounds(200, 120, 140, 70);
		txtIdNew.setBounds(200, 75, 140, 70);
		txtPasswordNew.setBounds(200, 280, 140, 70);
		txtNombreNew.setBounds(200, 175, 140, 70);
		
		
		/**
		 * Evento de boton de registro
		 */
		ActionListener registro = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Crear nueva ventana
				String cadenaInfoActual = txtId.getText() + "," + txtNombre.getText() + "," + txtPassword.getText(); 
				String cadenaInfoNueva = txtIdNew.getText() + "," + txtNombreNew.getText() + "," + txtPasswordNew.getText();
				String cadenaToda = cadenaInfoActual +","+ cadenaInfoNueva;
				visitante.pasarServidorEdicion(puertoHosto,puertoConn,cadenaToda);
				
			}
		};
		
		accept.addActionListener(registro);
		
	}
}