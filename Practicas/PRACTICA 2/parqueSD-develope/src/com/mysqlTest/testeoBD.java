package com.mysqlTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;

public class testeoBD {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {


		Ventana v1 = new Ventana();
		v1.setVisible(true);
		//v1.colocarBoton();

		// Conexion local
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		String DB_URL = "jdbc:mysql://127.0.0.1:3306/parquetematico";

		String USER = "root";
		String PASS = "new-password";
		
		// Conexion remota
		String url = "jdbc:mysql://remotemysql.com:3306/UFD6yb2Mz3";
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection conn = DriverManager.getConnection(url, "UFD6yb2Mz3", "TcV9ViEEPN");
	    System.out.println("Database connection established");
	    conn.close();
	    System.out.println("Database connection terminated");
	    
	    System.out.println("Intentado abrir archivo");
	    try(BufferedReader br = new BufferedReader(new FileReader("src/com/mysqlTest/test"))) {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        String everything = sb.toString();
	        System.out.println(everything);

	    }
	    
	   
	    System.out.println("Archivo abierto correctamente");


	}

}
