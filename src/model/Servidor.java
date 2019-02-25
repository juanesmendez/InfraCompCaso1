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
	* Método constructor de la clase
	*/
	public Servidor(Mensaje pMensaje, Buffer pBuff){

		this.mensaje = pMensaje;
		this.buff = pBuff;
	}

	public void recibirMensaje(){
		//Hay mensajes
		while(buff.getNumMensajes == 0 && buff.getNumClientes() > 0){
			Thread.yield();
		}

		Mensaje mess = buff.recibirMensaje();
		System.out.println("El servidor esta procesando el mensaje: "+ mensaje.getMensaje());
		mess.responder();
		System.out.println("La respuesta del servidor fue: "+ mensaje.getMensaje());

		//TODO: Despierta el wait() del cliente que espera una respuesta a su mensaje, hay que añadir synchronized?
		mess.notify();
	}


	public void run(){

		if(buff.getNumClientes > 0){
			recibirMensaje();
		}
	}

}
