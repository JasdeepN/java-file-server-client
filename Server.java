import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;


class Server{
    //server has access

    static ServerSocket main; 
    static Socket socket; 
    static Map<String, List<String>> serverLog = new HashMap<String, List<String>>();
    static String login = "(login\\s(.*))";
    static String message = "((send)\\s+(\\S+)\\s+(.*))";
    static String file = "(sendfile)\\s+(.*)";

    static List<String> logs = new ArrayList<String>();



    //variables below do not have access
    public Server(int port) throws Exception {
        this.main = new ServerSocket(port);
        logs.add("---server started---");
        serverLog.put("server", logs);
    }

    static class User extends Thread {

        String input = null;
        String filePath = null;
        Pattern name = Pattern.compile(login);
        Pattern mess = Pattern.compile(message);
        Pattern filePat = Pattern.compile(file);


        public User(Socket socket) {

            Server.socket = socket;
            logs.add("\nserver accessed");
            serverLog.put("server", logs);            
        }

        synchronized private void printServerStatus (){
            Iterator it = serverLog.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String reciever = pair.getKey().toString();
                if(reciever.compareTo("server") == 0){
                    String message = pair.getValue().toString();
                    System.out.println(message);
                }
            }
        }

        public void run(){
            while (true){
                try{
                System.out.println("reset connections");
                 InputStream in = Server.socket.getInputStream();

                 OutputStream out = Server.socket.getOutputStream();

                 DataInputStream dataIn = new DataInputStream(in);

                 DataOutputStream dataOut = new DataOutputStream(out);

                 input = dataIn.readUTF().toLowerCase();

                 Matcher matName = name.matcher(input);
                 Matcher matMsg = mess.matcher(input);
                 Matcher matFile = filePat.matcher(input);


                 while (matName.find()){
                    input = "login";
                       // currUser = (matName.group(2));
                } 

                while (matMsg.find()){
                    input = "send";
                } 


                while (matFile.find()){
                    input = "sendfile";
                    filePath = matFile.group(2);

                } 

                switch (input) {
                    case "exit": 
                    System.exit(0);
                    break;

                    case "status":
                    printServerStatus();
                    break;

                    case "sendfile":
                    recieveFile();
                    break;

                    case "log":
                    printServerStatus();
                    break;

                    case "cmd":
                    dataOut.writeUTF(">> COMMAND LIST <<");
                    dataOut.writeUTF("\"exit\" - exits the server (WARNING: THIS WILL DISCONNECT ALL CONNECTED USERS");
                        dataOut.writeUTF("\"cmd\" - shows all the commands available");
                        dataOut.flush();
                        break;

                        default:
                        System.out.println(input);
                        dataOut.writeUTF("Invalid Input use <cmd> to see a list of commands");
                        dataOut.flush();
                        break;
                    }
                } catch (IOException ex){
                    System.err.println("Unhandled IOException (all clients disconected most likely)");
                    System.exit(0);
                }
            }
        }
    }

    static synchronized public void recieveFile() throws IOException{
     InputStream in = Server.socket.getInputStream();

     OutputStream out = Server.socket.getOutputStream();

     DataInputStream dataIn = new DataInputStream(in);

     DataOutputStream dataOut = new DataOutputStream(out);
     int bytesRead;
     int current = 0;
     try{
        try{
            String fileName = dataIn.readUTF();     
            OutputStream output = new FileOutputStream("server_data/" + fileName);     
            long size = dataIn.readLong();     
            byte[] buffer = new byte[1024];     
            while (size > 0 && (bytesRead = dataIn.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)     
            {     
                output.write(buffer, 0, bytesRead);     
                size -= bytesRead;     
            } 
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
    int index = 1;
    while(true){

        Socket socket = this.main.accept();

        (new User(socket)).start();
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