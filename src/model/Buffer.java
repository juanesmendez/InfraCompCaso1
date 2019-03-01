package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Clase que representa un buffer con capacidad limitada donde se almacenaran mensajes que envia un Cliente y que recibe un Servidor
 */
public class Buffer {
    /**
     * Capacidad del buffer, la cual va a ir cambiando
     */
    private int capacidad;
    /**
     * Numero total de clientes que envian mensajes al buffer
     */
    private int numClientes;
    /**
     * El arreglo flexible que contiene los mensajes
     */
    private ArrayList<Mensaje> buff;
    /**
     * Bolsa que se utiliza para guardar hilos
     */
    Object lleno;

    /**
     * Constructor de la clase Buffer
     * @param capacidad El espacio disponible del buffer en un momento dado
     * @param numClientes El numero total de clientes que enviarán mensajes al buffer
     */
    public Buffer(int capacidad, int numClientes){
        this.capacidad = capacidad;
        this.numClientes = numClientes;
        this.buff = new ArrayList<>();
        lleno = new Object();
    }

    /**
     * Pone un mensaje en el buffer y se reduce su capacidad. El mensaje queda esperando la respuesta del servidor
     * @param pMensaje El mensaje a poner en el buffer
     */
    public void enviarMensaje(Mensaje pMensaje){
        // si el buffer no tiene capacidad
        synchronized (lleno){ // sincronizo el objeto que tendrá la bolsa de clientes que estan esperando para enviar un mensaje
            while (capacidad == 0){
                try {
                    System.out.println("El buffer esta lleno, el cliente con id " + pMensaje.getIdCliente() + " esta esperando");
                    lleno.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //Agrega a la lista el mensaje que el cliente mando
        synchronized (this){
            buff.add(pMensaje);
        }
        //Se reduce el espacio disponible en el buffer
        reducirCapacidad();

        synchronized (pMensaje){
            try {
                System.out.println("El cliente con id " + pMensaje.getIdCliente() + " esta esperando la respuesta del servidor.");
                pMensaje.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saca un mensaje del buffer. Se le notifica al cliente que esta esperando una respuesta por parte del servidor.
     * @return Retorna el mensaje
     */
    public Mensaje recibirMensaje(){
        Mensaje mess;
        //El primer mensaje en ingresar a la lista es el primero en salir
        synchronized (this){
            mess = buff.remove(0);
        }
        System.out.println("El mensaje del cliente " + mess.getIdCliente() + " FUE RETIRADO DEL BUFFER");
        //Se aumenta el espacio disponible en el buffer
        incrementarCapacidad();
        // notifico a los clientes que estan esperando debido a que ya hay capacidad en el buffer
        synchronized (lleno){
            System.out.println("Despierto a los clientes que estan dormidos.");
            lleno.notifyAll();
        }
        synchronized (mess){
            try{
                System.out.println("El servidor esta procesando el mensaje del cliente " + mess.getIdCliente() + " con contenido: "+ mess.getContenido());
                mess.responder();
                System.out.println("La respuesta del servidor fue: "+ mess.getContenido());
                // notifico al cliente que esta esperando la respuesta
                mess.notify();
                System.out.println("El servidor notifico al cliente de su respuesta.");
            }catch (NoSuchElementException e){
                System.out.println("El buffer esta vacio");
            }

        }
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

    public synchronized void reducirCapacidad(){
        capacidad--;
    }

    public synchronized void incrementarCapacidad(){
        capacidad++;
    }

    public synchronized void reducirNumClientes(){
        numClientes--;
    }

    public Object getBolsaLleno(){
        return lleno;
    }

    public static void main(String[] args) throws IOException {
        Cliente[] clientes;
        Servidor[] servidores;

        BufferedReader br = new BufferedReader(new FileReader("data/config.txt"));

        String[] data = br.readLine().split(" ");
        int tamBuff = Integer.parseInt(data[0]);
        int numCli = Integer.parseInt(data[1]);
        int numServ = Integer.parseInt(data[2]);

        Buffer buffer = new Buffer(tamBuff,  numCli);
        clientes = new Cliente[numCli];
        servidores = new Servidor[numServ];

        System.out.println("NUMERO DE CLIENTES: " + clientes.length);
        System.out.println("NUMERO DE SERVIDORES: " + servidores.length);
        System.out.println("TAMAÑO DEL BUFFER: " + tamBuff);

        //Inicializar los clientes
        for(int i = 0; i < clientes.length; i++){
            int numMens = Integer.parseInt(br.readLine());
            clientes[i] = new Cliente(i, numMens, buffer);
            System.out.println("Se creo el cliente: " + clientes[i].getIdentificador() + " con un numero de mensajes: " + numMens);
            /*
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            clientes[i].start();
            System.out.println("La capacidad del buffer es: " + buffer.getCapacidad());
        }

        //Inicializar los servidores
        for(int i = 0; i < servidores.length; i++){
            servidores[i] = new Servidor(buffer, i);
            System.out.println("Se creo el servidor: " + servidores[i].getIdentificador());
            /*
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            servidores[i].start();
        }
        // sincronizo para que todos los threads se encuentren despues de los sigueintes dos ciclos
        for(int i = 0; i < clientes.length; i++){
            try {
                clientes[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        System.out.println("ACABARON LOS CLIENTES");
        System.out.println("Numero de clientes: " + buffer.getNumClientes());

        for(int i = 0; i < servidores.length; i++){
            try {
                servidores[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ACABARON LOS SERVIDORES");
        System.out.println("Todos los threads terminaron su ejecución.");
    }

}
