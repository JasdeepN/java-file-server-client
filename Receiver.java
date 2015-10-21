import java.io.*;
import java.net.*;
class Receiver extends Thread {
    static String returnData = null;
    static boolean server = true;
    static DataInputStream x;
    public Receiver(DataInputStream y) {
        this.x = y;
    }

    public void run() {
        while (server){
            reciveData();
        }
    }

    static synchronized public void reciveData(){
        try {
            returnData = x.readUTF();
            popupMsg x = new popupMsg(returnData, "placeholder", 300, 400);
        } catch(Exception e) {
            System.out.println("connection to server terminated");
            server = false;
        } 
    }
}