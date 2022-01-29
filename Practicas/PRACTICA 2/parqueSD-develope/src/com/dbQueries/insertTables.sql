drop procedure if exists rellenarCasillas;

DELIMITER //  
CREATE PROCEDURE rellenarCasillas()   
BEGIN
DECLARE i INT DEFAULT 1; 
DECLARE j INT DEFAULT 1;
WHILE (i <= 20) DO
	WHILE (j <= 20) DO
		INSERT INTO Casillas VALUES(i, j, 1);
		SET j = j+1;
	END WHILE;
	SET j = 1;
    SET i = i+1;
END WHILE;
END;
//
DELIMITER ;

DELIMITER //  
drop procedure if exists rellenarSensores;
CREATE PROCEDURE rellenarSensores()   
BEGIN
DECLARE i INT DEFAULT 1; 
WHILE (i <= 5) DO
	INSERT INTO Sensores VALUES(i, i);
    SET i = i+1;
END WHILE;
END;
//
DELIMITER ;

INSERT INTO Mapa VALUES(1, 'DisneyMal', 50, false); 

CALL rellenarCasillas(); 

INSERT INTO Atracciones VALUES(1, 'TioVivo', 3, 0, 2, 0, 2, 2);
INSERT INTO Atracciones VALUES(2, 'Pendulo', 3, 0, 2, 0, 5, 3);
INSERT INTO Atracciones VALUES(3, 'Tren Bruja', 3, 0, 2, 0, 8, 13);
INSERT INTO Atracciones VALUES(4, 'Tazas', 3, 0, 2, 0, 14, 12);
INSERT INTO Atracciones VALUES(5, 'Sillas', 3, 0, 2, 0, 2, 15);

CALL rellenarSensores(); 

INSERT INTO Visitantes VALUES('juan01', 'Juan Sainz', 'caballo', false, NULL, NULL, 'FF0000');
INSERT INTO Visitantes VALUES('anderdb', 'Ander Dorado', 'new-password', false, NULL, NULL, 'FFA500');
INSERT INTO Visitantes VALUES('erDanih', 'Daniel Asensi', 'new_password', false, NULL,NULL, 'FFFF00');
