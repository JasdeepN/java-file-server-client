import java.io.*;
import java.net.*;
import java.util.Scanner;


class Client {
    Socket socket;
    static String data;
    static boolean exit = false;
    static Scanner scanInput = new Scanner(System.in);
    static String username;

    static class Sender extends Thread {
        DataOutputStream dataOut;

        
        public Sender(DataOutputStream dataOut) {
            this.dataOut = dataOut;
            try {
                dataOut.writeUTF("login " + username);
                dataOut.flush();
            } catch(Exception e) {
                System.err.println("login error");
                System.exit(0);
            }
        }

        public void run() {
            while (true){
                sendData(); 
            }
        }

        synchronized public void sendData() {
            data = scanInput.nextLine();
            try {
                dataOut.writeUTF(data);
                dataOut.flush();

            } catch(Exception e) {
                System.err.println("Sender Error");
                System.exit(0);
            }
        }
    }

    static class Receiver extends Thread {
        DataInputStream dataIn;
        String returnData = null;

        public Receiver(DataInputStream dataIn) {
            this.dataIn = dataIn;
        }

        public void run() {
            while (exit == false){
                reciveData(); 
            }
        }

        synchronized public void reciveData(){
            try {
                returnData = dataIn.readUTF();
                System.out.println(returnData);
            } catch(Exception e) {
                System.err.println("server has disconnected");
                System.exit(0);
            }
        }
    }


    public Client(String hostname, int port, String user) throws Exception {
        this.socket = new Socket(hostname, port);
        this.username = user;
    }

    public void connect() throws Exception {
        InputStream in = this.socket.getInputStream();
        OutputStream out = this.socket.getOutputStream();
        DataInputStream dataIn = new DataInputStream(in);
        DataOutputStream dataOut = new DataOutputStream(out);

        // Handle the connection
        Thread sender = new Sender(dataOut);
        Thread receiver = new Receiver(dataIn);

        sender.start();
        receiver.start();

        sender.join();
        receiver.join();
    }

    public static void main(String[] args) throws Exception {

        Client c = new Client(args[0], Integer.valueOf(args[1]), args[2]);
        c.connect(); 

    }
}
