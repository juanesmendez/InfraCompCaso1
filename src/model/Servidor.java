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




}
