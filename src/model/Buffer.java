package model;

import java.util.*;

public class Buffer {

    private int capacidad;
    private int numClientes;
    private Queue<Mensaje> buff;

    public Buffer(int capacidad, int numClientes){
        this.capacidad = capacidad;
        this.numClientes = numClientes;
        this.buff = new LinkedList<>();

    }

    public synchronized void enviarMensaje(Mensaje pMensaje){
    	//Agrega a la lista el mensaje que el cliente mando
    	buff.add(pMensaje);
    	//Se reduce el espacio disponible en el buffer
    	capacidad--;

    }

    public synchronized Mensaje recibirMensaje(){
    	//El primer mensaje en ingresar a la lista es el primero en salir
    	Mensaje mess = buff.poll();
    	//Se aumenta el espacio disponible en el buffer
    	capacidad++;
        return mess;
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

    public void reducirNumClientes(){
        numClientes--;
    }

    public static void main(String[] args){

    	BufferedReader br = new BufferedReader(new FileReader("data/config.txt"));

    	String[] data = br.readLine().split(" ");
    	int tamBuff = Integer.parseInt(data[0]);
    	int numCli = Integer.parseInt(data[1]);
    	int numServ = Integer.parseInt(data[2]);

    	Buffer buffer = new Buffer(tamBuff,  numCli);
    	System.out.println("La capacidad del buffer es: " + tamBuff);
    	//Inicializar los clientes
    	for(int i = 0; i < numCli; i++){
    		int numMens = Integer.parseInt(br.readLine());
    		Cliente cli = new Cliente(i, numMens, buffer);
    		System.out.println("Se creo el cliente: " + cli.getId() + " con un numero de mensajes: " + numMens);
    		cli.start();
    	}

    	//Inicializar los servidores
    	for(int i = 0; i < numServ; i++){
    		Servidor serv = new Servidor(buffer);
    		serv.start();
    	}


    }


}
