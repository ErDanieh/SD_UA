HOST = 'localhost'
PORT = 8010
#Se importa el módulo
import socket
 
#Creación de un objeto socket (lado cliente)
obj = socket.socket()
 
#Conexión con el servidor. Parametros: IP (puede ser del tipo 192.168.1.1 o localhost), Puerto
obj.connect((HOST, PORT))
print("Conectado al servidor")
mens= input()
msg=mens.encode('utf-8')
print("Enviando : ", mens)

#Con el método send, enviamos el mensaje
obj.send(msg)

#Cerramos la instancia del objeto servidor
respuesta=obj.recv(4096)
print(respuesta.decode('utf-8'))

obj.close()

print("Conexión cerrada")