/**
 * 
 */
package com.dbQueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author Ander
 *
 */
public class deployBD {

	private String url = "jdbc:mysql://remotemysql.com:3306/UFD6yb2Mz3";
	private String user = "UFD6yb2Mz3";
	private String pass = "TcV9ViEEPN";
	private String driver = "com.mysql.cj.jdbc.Driver";
	
	public void dropTablas(Connection conn) {
		try {
			Statement stm = conn.createStatement();
		
			stm.addBatch("DROP TABLE Sensores;");
			stm.addBatch("DROP TABLE Atracciones;");
			stm.addBatch("DROP TABLE Visitantes;");
			stm.addBatch("DROP TABLE Casillas;");
			stm.addBatch("DROP TABLE Mapa;");

			stm.executeBatch();
			
			System.out.println("[DeployBD] Limpiando BD...");
		} catch (Exception e) {
			System.out.println("No he añadido una mierda");
		}
	}
	
	public void crearTablas(Connection conn) {
		try {
			Statement stm = conn.createStatement();
			stm.addBatch("CREATE TABLE `Mapa` (\r\n"
					+ "	`ID` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "	`Nombre` VARCHAR(20) NOT NULL,\r\n"
					+ "	`AforoMax` INT NOT NULL DEFAULT '10',\r\n"
					+ "	`abierto` BOOLEAN NOT NULL DEFAULT false,\r\n"
					+ "	PRIMARY KEY (`ID`)\r\n"
					+ ");");
			stm.addBatch("CREATE TABLE `Casillas` (\r\n"
					+ "	`fila` INT,\r\n"
					+ "	`columna` INT,\r\n"
					+ "	`idMapa` INT,\r\n"
					+ "	PRIMARY KEY (`fila`, `columna`, `idMapa`),\r\n"
					+ "	FOREIGN KEY (`idMapa`) REFERENCES Mapa(ID)\r\n"
					+ ");");
			stm.addBatch("CREATE TABLE `Atracciones` (\r\n"
					+ "	`ID` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "	`Nombre` VARCHAR(20) DEFAULT 'Atraccion',\r\n"
					+ "	`nTurno` INT DEFAULT '5',\r\n"
					+ "	`nCola` INT DEFAULT '0',\r\n"
					+ "	`tTurno` INT,\r\n"
					+ "	`tEspera` INT,\r\n"
					+ "	`posFila` INT,\r\n"
					+ "	`posColumna` INT,\r\n"
					+ "	PRIMARY KEY (`ID`),\r\n"
					+ "	FOREIGN KEY (`posFila`, `posColumna`) REFERENCES Casillas(fila, columna)\r\n"
					+ ");");
			stm.addBatch("CREATE TABLE `Sensores` (\r\n"
					+ "	`ID` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "	`idAtraccion` INT,\r\n"
					+ "	PRIMARY KEY (`ID`),\r\n"
					+ "	FOREIGN KEY (`idAtraccion`) REFERENCES Atracciones(ID)\r\n"
					+ ");");
			stm.addBatch("\r\n"
					+ "CREATE TABLE `Visitantes` (\r\n"
					+ "	`ID` VARCHAR(20) NOT NULL,\r\n"
					+ "	`Nombre` VARCHAR(20) NOT NULL,\r\n"
					+ "	`Password` VARCHAR(20) NOT NULL,\r\n"
					+ "	`enParque` BOOLEAN NOT NULL DEFAULT false,\r\n"
					+ "	`posFila` INT,\r\n"
					+ "	`posColumna` INT,\r\n"
					+ "	`color` CHAR(6),\r\n"
					+ "	PRIMARY KEY (`ID`),\r\n"
					+ "	FOREIGN KEY (`posFila`, `posColumna`) REFERENCES Casillas(fila, columna)\r\n"
					+ "\r\n"
					+ ");");
			stm.executeBatch();
			
			System.out.println("[DeployBD] Tablas creadas.");
		} catch (Exception e) {
			System.out.println("[DeployBD] Error al crear las BD");
		}
	}
	
	public void rellenarTablas(Connection conn) {
		try {
			Statement stm = conn.createStatement();
			
			stm.addBatch("INSERT INTO Mapa VALUES(1, 'DisneyMal', 50, false);");

		
			for (int i=1; i<=20; i++) {
				for (int j=1; j<=20; j++) {
					stm.addBatch("INSERT INTO Casillas VALUES(" + i + ", " + j + "," + 1 + ")");
				}
			}
			
			stm.addBatch("INSERT INTO Atracciones VALUES(1, 'TioVivo', 3, 0, 2, 0, 2, 2);");
			stm.addBatch("INSERT INTO Atracciones VALUES(2, 'Pendulo', 3, 0, 2, 0, 5, 3);");
			stm.addBatch("INSERT INTO Atracciones VALUES(3, 'Tren Bruja', 3, 0, 2, 0, 8, 13);");
			stm.addBatch("INSERT INTO Atracciones VALUES(4, 'Tazas', 3, 0, 2, 0, 14, 12);");
			stm.addBatch("INSERT INTO Atracciones VALUES(5, 'Sillas', 3, 0, 2, 0, 2, 15);");

			stm.addBatch("INSERT INTO Visitantes VALUES('juan01', 'Juan Sainz', 'caballo', false, NULL, NULL, 'FF0000');");
			stm.addBatch("INSERT INTO Visitantes VALUES('anderdb', 'Ander Dorado', 'new-password', false, NULL, NULL, 'FFA500');");
			stm.addBatch("INSERT INTO Visitantes VALUES('erDanih', 'Daniel Asensi', 'new_password', false, NULL,NULL, 'FFFF00');");
			
			for (int i=1; i<=5; i++) {
				stm.addBatch("INSERT INTO Sensores VALUES(" + i + ", " + i + ")");
			}
			stm.executeBatch();
			
			System.out.println("[DeployBD] Tablas rellenadas correctamente");
		} catch (Exception e) {
			System.out.println("[DeployBD] Error al rellenar la BD");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		deployBD dBD = new deployBD();
		
		try {
			Class.forName(dBD.driver);
			Connection conn = DriverManager.getConnection(dBD.url, dBD.user, dBD.pass);
			System.out.println("[DeployBD] Conexion con la BD establecida.");
			dBD.dropTablas(conn);
			dBD.crearTablas(conn);
			dBD.rellenarTablas(conn);
			
		} catch (Exception e) {
			System.out.println("[DeployBD] Error con la conexion de la BD.");
		}
	}
}
