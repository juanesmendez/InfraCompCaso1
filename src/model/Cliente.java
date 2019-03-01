package model;

import java.util.ArrayList;

/**
 * Clase que representa un cliente que envia mensajes a un servidor mediante un bbuffer y respera una respuesta del mismo
 */
public class Cliente extends Thread{
    /**
     * El identificador del cliente
     */
    private int id;
    /**
     * EL numero de mensajes que enviará el cliente a los servidores para esperar una respuesta
     */
    private int numMensajes;
    /**
     * El arreglo de mensajes a poner en el buffer
     */
    private ArrayList<Mensaje> mensajes;
    /**
     * Referencia al buffer que contiene todos los mensajes de los clientes
     */
    private Buffer buffer;

    /**
     * Método constructor de la clase
     * @param id el identificador del cliente
     * @param numMensajes
     * @param buffer
     */
    public Cliente(int id, int numMensajes, Buffer buffer){
        this.id = id;
        this.numMensajes = numMensajes;
        this.buffer = buffer;
        mensajes = new ArrayList<>();
        for(int i = 0; i < numMensajes; i++){
            int contenido = (int) Math.round(Math.random()*1000);
            mensajes.add(new Mensaje(id,contenido));
        }
    }

    /**
     * Envia un mensaje al buffer que será retirado por un Servidor
     * @param m
     */
    public void enviarMensaje(Mensaje m){
    	    
            buffer.enviarMensaje(m);
    }

    public int getIdentificador(){
    	return id;
    }

    /**
     * Método run del thread que se ejecutará cuando se llame al metodo start(). Recorre los mensajes del cliente y los envia.
     */
    @Override
    public void run() {
        // recorrido de los mensajes del cliente para ponerlos en el buffer
        for(Mensaje m:mensajes){
            enviarMensaje(m); // cliente envia todos los mensajes al buffer
        }
        buffer.reducirNumClientes();
    }


}
