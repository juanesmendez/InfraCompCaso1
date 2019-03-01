package model;

import java.util.NoSuchElementException;

/**
 * Clase que representa un Servidor que responde mensajes de diferentes clientes, retirandolos de un buffer
 */
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

	/**
	 * Llama al buffer para retirar un mensaje puesto previamente por un cliente.
	 */
	public void recibirMensaje(){
		//mientras no haya mensajes ceder el procesador (yield)
		while(buff.getNumMensajes() == 0 ){
			// caso de salida para que el servidor no se quede en el loop infititamente cuando queda 1 cliente
			// y otro servidor se le adelanta y le quita el ultimo mensaje que queda por atender.
			if(buff.getNumClientes() == 0){
				return;
			}
			yield();
		}
		mensaje = buff.recibirMensaje();
	}

	public int getIdentificador(){
		return id;
	}

	/**
	 * Metodo run, que se ejecutará apenas se llame al metodo start del thread. Loop que hace que el Servidor siempre
	 * este a la espera de mensajes por retirar hasta que ya no haya más clientes solicitando una respuesta.
	 */
	public void run() {
		// chequer información de numero de cientes para acabar
		while(buff.getNumClientes() > 0){
			recibirMensaje();
		}
	}
}
