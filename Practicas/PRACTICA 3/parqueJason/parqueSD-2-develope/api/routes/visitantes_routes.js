var dbconf = require('../databaseAccess/databaseAccess.js');
const routes = require('express').Router();
const mysql = require("mysql");

const bodyParser = require("body-parser");
routes.use(bodyParser.json()); // body en formato json

// Conexion a la BBDD
var connection = mysql.createConnection(dbconf);

//connection.connect(error => {
//    if (error) throw error;
//    console.log('Conexion a la base de datos  desde atracciones SD_MYSQL correcta');
//});
function handleDisconnect() {
  connection = mysql.createConnection(dbconf); // Recreate the connection, since
                                                  // the old one cannot be reused.

  connection.connect(function(err) {              // The server is either down
    if(err) {                                     // or restarting (takes a while sometimes).
      console.log('error when connecting to db:', err);
      setTimeout(handleDisconnect, 2000); // We introduce a delay before attempting to reconnect,
    }                                     // to avoid a hot loop, and to allow our node script to
  });                                     // process asynchronous requests in the meantime.
                                          // If you're also serving http, display a 503 error.
  connection.on('error', function(err) {
    console.log('db error', err);
    if(err.code === 'PROTOCOL_CONNECTION_LOST') { // Connection to the MySQL server is usually
      handleDisconnect();                         // lost due to either server restart, or a
    } else {                                      // connnection idle timeout (the wait_timeout
      throw err;                                  // server variable configures this)
    }
  });
}

handleDisconnect();

// Listado de todos los visitantes
routes.get("/visitantes",(request, response) => {
    console.log('Listado de todos los visitantes');
    
    const sql = 'SELECT * FROM Visitantes';
    connection.query(sql,(error,resultado)=>{
//        if (error) throw error;
        if (resultado.length > 0){
            response.json(resultado);
        } else {
            response.send('No hay resultados');
        }
    });
});


// Informacion de un usuario
routes.get("/visitantes/:id",(request, response) => {
    console.log('Informacion de un usuario');

    const {id} = request.params;
    const sql = `SELECT * FROM Visitantes WHERE ID='${id}'`;

    connection.query(sql,(error,resultado)=>{
//        if (error) throw error;
        if (resultado.length > 0){
            response.json(resultado);
        } else {
            response.send('No hay resultados');
        }
    });
});


// Posicion de un usuario
routes.get("/visitantes/:id/posicion",(request, response) => {
    console.log('Informacion de un usuario');

    const {id} = request.params;
    const sql = `SELECT ID, posFila, posColumna FROM Visitantes WHERE ID='${id}'`;

    connection.query(sql,(error,resultado)=>{
//        if (error) throw error;
        if (resultado.length > 0){
            response.json(resultado);
        } else {
            response.send('No hay resultados');
        }
    });
});

// Modificar posicion usuario
routes.put("/visitantes/:id/posicion",(request, response) => {
    console.log('Modificar posicion de un usuario');

    const {id} = request.params;
    const {posFila, posColumna} = request.body;
    const sql = `UPDATE Visitantes SET posFila=${posFila}, posColumna=${posColumna} WHERE ID='${id}'`;

    connection.query(sql,(error,resultado)=>{
//        if (error) throw error;
        if (resultado.length > 0){
            response.json(resultado);
        } else {
            response.send('Posicion actualizada');
        }
    });
});

// Modificamos la informacion de un usuario
routes.put("/visitantes/:id",(request, response) => {
    console.log('Modificar posicion de un usuario');

    const {id} = request.params;
    const {newId, Nombre, passwordOld, passwordNew} = request.body;
    const sql = `UPDATE Visitantes SET ID='${newId}', Nombre='${Nombre}', Password='${passwordNew}' WHERE ID='${id}' AND Password='${passwordOld}'`;

    connection.query(sql,(error,resultado)=>{
//        if (error) throw error;
        if (resultado.length > 0){
            response.json(resultado);
        } else {
            response.send('Posicion actualizada');
        }
    });
});

// Añade un visitante a la BBDD
routes.post("/visitantes/",(request, response) => {
    console.log('Añadir nuevo visitante');

    const sql = 'INSERT INTO Visitantes SET ?';
  
    const usuarioObj = {
        "ID": request.body.ID,
        "Nombre": request.body.Nombre,
        "Password":request.body.Password,
        "enParque": request.body.enParque,
        "posFila": request.body.posFila,
        "posColumna": request.body.posColumna,
        "color": request.body.color
    }    

    connection.query(sql,usuarioObj,error => {
//      if (error) throw error;
      response.send('Visitante creado');
    }); 
});

// Elimina un visitante a la BBDD
routes.delete("/visitantes/:id",(request, response) => {
    console.log('Eliminar visitante');

    const {id} = request.params;
    const sql = `DELETE FROM Visitantes WHERE ID='${id}'`;

    connection.query(sql,error => {
//      if (error) throw error;
      response.send('Visitante eliminado');
    }); 
});

// Color de un usuario
routes.get("/visitantes/:id/color",(request, response) => {
    console.log('Informacion de un usuario');

    const {id} = request.params;
    const sql = `SELECT ID, color FROM Visitantes WHERE ID='${id}'`;

    connection.query(sql,(error,resultado)=>{
//        if (error) throw error;
        if (resultado.length > 0){
            response.json(resultado);
        } else {
            response.send('No hay resultados');
        }
    });
});

module.exports = routes;
