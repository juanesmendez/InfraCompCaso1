package model;

import java.util.NoSuchElementException;

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
			yield();
		}


		mensaje = buff.recibirMensaje();
		//TODO: Despierta el wait() del cliente que espera una respuesta a su mensaje, hay que añadir synchronized?
		synchronized (mensaje){
			try{

				System.out.println("El servidor esta procesando el mensaje: "+ mensaje.getContenido());
				mensaje.responder();
				System.out.println("La respuesta del servidor fue: "+ mensaje.getContenido());
				mensaje.notify();
				System.out.println("El servidor notifico al cliente de su respuesta.");
			}catch (NoSuchElementException e){
				System.out.println("El buffer esta vacio");
			}

		}

	}

	public int getIdentificador(){
		return id;
	}


	public void run(){

		if(buff.getNumClientes() > 0){
			recibirMensaje();
		}
	}

}
