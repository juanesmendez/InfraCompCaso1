package model;

public class Servidor extends Thread{

	/**
	* Mensaje que va a ser procesado por el servidor
	*/
	private Mensaje mensaje;

	/**
	* Buffer que contiene la informacion de los mensajes de la aplicacion
	*/
	private Buffer buff;

	/**
	*Identificador del servidor
	*/

	private int id;


	/**
	* Método constructor de la clase
	*/
	public Servidor(Buffer pBuff, int pId){
		this.buff = pBuff;
		this.id = pId;
	}

	public void recibirMensaje(){
		//Hay mensajes
		while(buff.getNumMensajes() == 0 && buff.getNumClientes() > 0){
			Thread.yield();
		}

		mensaje = buff.recibirMensaje();
		System.out.println("El servidor esta procesando el mensaje: "+ mensaje.getContenido());
		mensaje.responder();
		System.out.println("La respuesta del servidor fue: "+ mensaje.getContenido());

		//TODO: Despierta el wait() del cliente que espera una respuesta a su mensaje, hay que añadir synchronized?
		mensaje.notify();
	}

	public int getId(){
		return id;
	}


	public void run(){

		if(buff.getNumClientes() > 0){
			recibirMensaje();
		}
	}

}
