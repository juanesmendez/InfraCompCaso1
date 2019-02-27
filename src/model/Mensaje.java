package model;

public class Mensaje {

	/**
	*Contenido del mensaje
	*/
    private int contenido;

	/**
	*Identificador del cliente que envio el mensaje
	*/
    private int idCliente;

    /**
    * Metodo constructor del mensaje 
    */
    public Mensaje(int idCliente, int contenido){
        this.idCliente = idCliente;
        this.contenido = contenido;
    }

    public void responder(){
    	contenido++;
    }

   public int getContenido(){
   	return contenido;
   }

   public int getIdCliente(){
        return idCliente;
   }

}
