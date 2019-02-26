package model;

import java.util.ArrayList;

public class Cliente extends Thread{
    private int id;
    private int numMensajes;
    private ArrayList<Mensaje> mensajes;
    private Buffer buffer;

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

    public void enviarMensaje(Mensaje m){
    	    //TODO: Revisar espera pasiva, notify while 
            while (buffer.getCapacidad() == 0){
                System.out.println("El buffer esta ocupado, el cliente con id " + id + " esta esperando");
                //yield();

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        
            buffer.enviarMensaje(m);

            //TODO: asi se sincroniza el mensaje?
            synchronized (m){
                try {
                    System.out.println("El cliente con id " + id + " esta esperando la respuesta del servidor.");
                    m.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

    public int getIdentificador(){
    	return id;
    }


    @Override
    public void run() {
        for(Mensaje m:mensajes){
            enviarMensaje(m); // cliente envia todos los mensajes al buffer
        }
        buffer.reducirNumClientes();

    }


}
