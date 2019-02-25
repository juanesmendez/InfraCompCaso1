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

    @Override
    public void run() {
        for(Mensaje m:mensajes){
            enviarMensaje(m); // cliente envia todos los mensajes al buffer
        }

    }

    public void enviarMensaje(Mensaje m){

            while (buffer.getCapacidad() == 0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        
            buffer.enviarMensaje(m);



            synchronized (m){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
}
