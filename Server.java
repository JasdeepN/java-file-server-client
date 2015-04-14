import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;


class Server{
    //server has access
    static ServerSocket main; 
    static Socket clientSocket;

    static FileInputStream fis;  
    static BufferedInputStream bis;

    static OutputStream os;  
    static InputStream in;

    static DataOutputStream dos; 
    static DataInputStream dis;    

    //variables below do not have access
    public Server(int port) throws Exception {
        this.main = new ServerSocket(port);

    }

    static class Job extends Thread {

        String input;


        public Job() {
           // dis = in;
            //dos = out;

            System.out.println("Client connected");
        }

        public void run(){
            while (true){
                try{
                    input = dis.readUTF().toLowerCase();
                    System.out.println(input);

                    switch (input){
                        case "sendfile":
                        recieveFile();
                        break;

                        case "exit":
                        System.exit(0);
                        break;
                    }

                } catch (IOException ex){

                }
            }
        }
    }

    static synchronized public void recieveFile(){
        int bytesRead;
        int current = 0;
        try{
            try{
                String fileName = dis.readUTF();     
                OutputStream output = new FileOutputStream("server_data/"+fileName);     
                long size = dis.readLong();     
                byte[] buffer = new byte[1024];     
                while (size > 0 && (bytesRead = dis.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)     
                {     
                    output.write(buffer, 0, bytesRead);     
                    size -= bytesRead;     
                }  

        // Closing the FileOutputStream handle
                output.close();  
            }catch (FileNotFoundException ex){
                System.err.println("file not found");
            }
        }catch(IOException ex2){
            System.err.println("IOException at recieve file");
        }
        System.out.println("finished adding file");
    }


    public void serve() throws Exception {

        while(true){

            clientSocket = this.main.accept();
            in = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();
            dis = new DataInputStream(in);
            dos = new DataOutputStream(os);

            (new Job()).start();
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
