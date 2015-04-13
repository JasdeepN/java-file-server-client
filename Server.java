import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;


class Server{
    //server has access
    static ServerSocket main;  
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
        private String Username;

        String input = null, msg = null, user = null, currUser = null;
        String filePath = null;
        Pattern name = Pattern.compile(login);
        Pattern mess = Pattern.compile(message);
        Pattern filePat = Pattern.compile(file);

        DataInputStream dataIn;
        DataOutputStream dataOut; 

        public User(DataInputStream in, DataOutputStream out) {
            this.dataIn = in;
            this.dataOut = out;
            logs.add("\nnew user created");
            System.out.println("Client connected waiting for login...");
            serverLog.put("server", logs);            
        }

        synchronized public void setUserName(String username){
            this.Username = username;
            try {
                dataOut.writeUTF("logged in as " + this.Username + " type \"cmd\" for a list of commands");
                dataOut.flush();
                System.out.println(this.Username + " has logged in");
            } catch(IOException e){
                System.out.println("IOException at set user");
            }
            logs.add("\n" + username + " has logged in a new mailbox has been added to the server");
            serverLog.put("server", logs);
        }

        synchronized public String getUser(){
            return this.Username;
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
                    input = dataIn.readUTF().toLowerCase();

                    Matcher matName = name.matcher(input);
                    Matcher matMsg = mess.matcher(input);
                    Matcher matFile = filePat.matcher(input);


                    while (matName.find()){
                        input = "login";
                        currUser = (matName.group(2));
                    } 

                    while (matMsg.find()){
                        input = "send";
                        user = matMsg.group(3);
                        msg = matMsg.group(4);  
                    } 


                    while (matFile.find()){
                        input = "sendfile";
                        filePath = matFile.group(2);

                    } 

                    switch (input) {
                        case "exit": 
                        System.exit(0);
                        break;

                        case "login":
                        setUserName(currUser);                            
                        break;

                        case "send":
                        break;

                        case "sendfile":
                        try {
                            dataOut.writeUTF(filePath);
                            dataOut.flush();
                        } catch(IOException e){
                            System.out.println("IOException at sendfile on server");
                        }
                        break;

                        case "log":
                        printServerStatus();
                        break;

                        case "cmd":
                        dataOut.writeUTF(">> COMMAND LIST <<");
                        dataOut.writeUTF("\"exit\" - exits the server (WARNING: THIS WILL DISCONNECT ALL CONNECTED USERS");
                            dataOut.writeUTF("\"login\" <username> - changes your username");
                            dataOut.writeUTF("\"send\" <recipient> <message> - sends a message to the recipients mailbox");
                            dataOut.writeUTF("\"fetch\" - retrieves messages from your mailbox on the server");
                            dataOut.writeUTF("\"log\" - prints the server logs to the server terminal");
                            dataOut.writeUTF("\"cmd\" - shows all the commands available");
                            dataOut.flush();
                            break;

                            default:
                            dataOut.writeUTF("Invalid Input");
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

        public void serve() throws Exception {
            int index = 1;
            while(true){

                Socket socket = this.main.accept();
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                DataInputStream dataIn = new DataInputStream(in);
                DataOutputStream dataOut = new DataOutputStream(out);

                (new User(dataIn, dataOut)).start();
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