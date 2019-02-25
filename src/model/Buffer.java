package model;

import java.util.*;

public class Buffer {

    private int capacidad;
    private int numClientes;
    private Queue<Mensaje> buff;

    public Buffer(int capacidad){
        this.capacidad = capacidad;
        this.buff = new LinkedList<>();

    }

    public synchronized void enviarMensaje(Mensaje pMensaje){
    	//Agrega a la lista el mensaje que el cliente mando
    	buff.add(pMensaje);
    	//Se reduce el espacio disponible en el buffer
    	capacidad--;

    }

    public synchronized void recibirMensaje(){
    	//El primer mensaje en ingresar a la lista es el primero en salir
    	buff.poll();
    	//Se aumenta el espacio disponible en el buffer
    	capacidad++;

    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getNumClientes(){
    	return numClientes;
    }

    public int getNumMensajes(){
    	return buff.size();
    }

    public static void main(String[] args){

    }


}
