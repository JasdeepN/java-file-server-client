import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;


class Server{
    //server has access
    static ServerSocket main;  
    static DataInputStream serverDataIn;


    //variables below do not have access
    public Server(int port) throws Exception {
        this.main = new ServerSocket(port);
    }

    static class Job extends Thread {

        String input;
        DataInputStream dataIn;
        DataOutputStream dataOut; 

        public Job(DataInputStream in, DataOutputStream out) {
            this.dataIn = in;
            this.dataOut = out;
            serverDataIn = in;
            System.out.println("Client connected waiting for login...");
        }

        public void run(){
            while (true){
                try{
                    input = dataIn.readUTF().toLowerCase();
                    System.out.println(input);
                } catch (IOException ex){

                }

            }
        }
        
        static synchronized public void recieveFile(){
            int bytesRead;
            int current = 0;
            try{
                try{
                    String fileName = "recieved_" + serverDataIn.readUTF();
                    OutputStream output = new FileOutputStream("server_data/"+fileName);
                    long size = serverDataIn.readLong();
                    byte[] buffer = new byte[1024];
                    while (size > 0 && (bytesRead = serverDataIn.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)
                    {
                        output.write(buffer, 0, bytesRead);
                        size -= bytesRead;
                    }
                }catch (FileNotFoundException ex){
                    System.err.println("file not found");
                }
            }catch(IOException ex2){
                System.err.println("IOException at recieve file");
            }
            System.out.println("finished adding file");
        }
    }

    public void serve() throws Exception {
        int index = 1;
        while(true){

            Socket socket = this.main.accept();
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            DataInputStream dataIn = new DataInputStream(in);
            DataOutputStream dataOut = new DataOutputStream(out);

            (new Job(dataIn, dataOut)).start();
            index = index + 1;
        } 
    }

    public static void main(String[] args) throws Exception {
        try{
            Server s = new Server(Integer.valueOf(args[0]));

            System.out.println("Waiting for input...");
            s.serve();
        }catch (Exception e){
            System.err.println("clients have disconected");
            System.exit(0);

        }
    }
}
