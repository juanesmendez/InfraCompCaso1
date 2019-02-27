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
		this.mensaje = null;
	}

	public void recibirMensaje(){

		// chequer información de numero de cientes para acabar
		outerLoop:
		while(buff.getNumClientes() > 0){
			//mientras no haya mensajes
			while(buff.getNumMensajes() == 0 ){ // revisar este loop
				if(buff.getNumClientes() == 0){ // caso de salida para que el servidor no se quede en el loop infititamente cuando quedaba 1 cliente y otro servidor se le adelanta y le quita el ultimo mensaje que queda por atender.
					break outerLoop; // label para que se salga de ambas loops y que el thread acabe su ejecución
				}
				yield();
			}

			mensaje = buff.recibirMensaje();
		}

	}

	public int getIdentificador(){
		return id;
	}

	public void run() {
	/*
		if(buff.getNumClientes() > 0){
			recibirMensaje();
		}
	*/
		recibirMensaje();
	}
}
