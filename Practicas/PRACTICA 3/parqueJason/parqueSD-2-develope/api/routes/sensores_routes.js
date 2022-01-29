var dbconf = require('../databaseAccess/databaseAccess.js');
const routes = require('express').Router();
const mysql = require("mysql");

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

// Listado de todos los sensores
routes.get("/sensores",(request, response) => {
    console.log('Listado de todos los sensores');
    
    const sql = 'SELECT * FROM Sensores';
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
routes.get("/sensores/:id",(request, response) => {
    console.log('Informacion de un sensor');

    const {id} = request.params;
    const sql = `SELECT * FROM Sensores WHERE ID=${id}`;

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
