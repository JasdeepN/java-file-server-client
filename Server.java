import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;
import java.nio.*;



class Server{
    //server has access
    static Server s;
    static ServerSocket main; 
    static Socket socket; 
    static String load = "(load\\s(.*))";

    static List<String> logs = new ArrayList<String>();

    

    //variables below do not have access
    public Server(int port) throws Exception {
        this.main = new ServerSocket(port);

    }

    static class User extends Thread {

        String input = null;
        String filePath = null;
        Pattern loadfile = Pattern.compile(load);
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

                    Matcher matLoad = loadfile.matcher(input);


                    while (matLoad.find()){
                        input = "load";
                        filePath = (matLoad.group(2));
                    } 

                    switch (input) {
                        case "exit": 
                        System.exit(0);
                        break;

                        case "load":
                        ParseFile parser = new ParseFile();
                        parser.load(filePath);
                        break;

                        case "sendfile":
                        recieveFile();
                        break;

                        case "files":
                        final File folder = new File("server_data/");
                        listFilesForFolder(folder);
                        break;

                        case "cmd":
                        List<String> outS = new ArrayList<String>();
                        outS.add("<html> TEXT COMMAND LIST <BR><BR>");
                        outS.add("exit - closes all connections and <BR>shuts down the server<BR><BR>");
                        outS.add("load - loads a file onto the server for parsing<BR><BR>");
                        outS.add("sendFile - the next thing sent will <BR>go to the file reciever<BR><BR>");
                        outS.add("files - shows the current list of accessible<BR> files for the server</html>");
                        
                        popupMsg commands = new popupMsg(outS);
                        break;

                        default:
                            //System.out.println(input);
                        dataOut.writeUTF("Invalid Input use <cmd> to see a list of commands");
                        dataOut.flush();
                        break;
                    }
                } catch (IOException ex){
                    System.err.println("Unhandled IOException (all clients disconected most likely)");
                    //System.exit(0);
                    try{
                        s.serve();
                    }catch (Exception ex2){
                        popupMsg msg = new popupMsg(ex2 + " in server");
                    }

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
                User.dataOut.writeUTF("finished adding file");
                User.dataOut.flush(); 
            }catch (FileNotFoundException ex){
                System.err.println("file not found");
            }
        }catch(IOException ex2){
            System.err.println("IOException at recieve file");
        }
    }

    static public void listFilesForFolder(final File folder) {
        List<String> x = new ArrayList<String>();
        x.add("<html>files currently on the server:<br>");
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                x.add(fileEntry.getName()+"<br>");
            }
        }
        x.add("</html>");
        popupMsg files = new popupMsg(x);
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
            s = new Server(Integer.valueOf(args[0]));
            System.out.println("Waiting for input...");
            s.serve();
        }catch (Exception e){
            System.err.println("clients have disconected");
            System.exit(0);

        }
    }
}