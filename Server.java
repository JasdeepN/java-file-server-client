import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;


class Server{
    //server has access
    static ServerSocket main;  
    public static void main (String[] args ) throws IOException {   

        int bytesRead;
        int current = 0;

        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(3000);
        System.out.println("waiting...");
        while(true) {
            Socket clientSocket = null;
            clientSocket = serverSocket.accept();

            InputStream in = clientSocket.getInputStream();

            DataInputStream clientData = new DataInputStream(in); 

            String fileName = clientData.readUTF();   
            OutputStream output = new FileOutputStream("server_data/"+fileName);   
            long size = clientData.readLong();   
            byte[] buffer = new byte[1024];   
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)   
            {   
                output.write(buffer, 0, bytesRead);   
                size -= bytesRead;   
            }

        // Closing the FileOutputStream handle
            output.close();
        }
    }
}

