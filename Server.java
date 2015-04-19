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
    static String login = "(login\\s(.*))";
    static String message = "((send)\\s+(\\S+)\\s+(.*))";

    static List<String> logs = new ArrayList<String>();

    

    //variables below do not have access
    public Server(int port) throws Exception {
        this.main = new ServerSocket(port);
      
    }

    static class User extends Thread {

        String input = null;
        String filePath = null;
        Pattern name = Pattern.compile(login);
        static DataInputStream dataIn; 
        static DataOutputStream dataOut;


        public User(DataInputStream di, DataOutputStream dout) throws IOException{

            this.dataIn = di;
            this.dataOut = dout;
           
        }


        public void run(){
            while (true){
                try{

                    System.out.println("waiting...");
                    //dataOut.writeUTF("reset connections");

                    input = dataIn.readUTF().toLowerCase();

                    Matcher matName = name.matcher(input);


                    while (matName.find()){
                        input = "login";
                       // currUser = (matName.group(2));
                    } 

                    switch (input) {
                        case "exit": 
                        System.exit(0);
                        break;

                        case "status":
                        break;

                        case "sendfile":
                        recieveFile();
                        dataOut.writeUTF("file saved successfully");
                        dataOut.flush();
                        break;

                        case "log":
                        
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
            
            int bytesRead;
            int current = 0;
            try{
                try{
                    String fileName = User.dataIn.readUTF();     
                    OutputStream output = new FileOutputStream("server_data/" + fileName);     
                    long size = User.dataIn.readLong();     
                    byte[] buffer = new byte[1024];     
                    while (size > 0 && (bytesRead = User.dataIn.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)     
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
            while(true){
                Socket socket = this.main.accept();
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                DataInputStream dataIn = new DataInputStream(in);
                DataOutputStream dataOut = new DataOutputStream(out);
                

                (new User(dataIn, dataOut)).start();
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