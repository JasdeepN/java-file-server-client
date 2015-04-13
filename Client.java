import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.Graphics;
import java.util.*;
import java.awt.GridLayout;
import javax.swing.JFrame;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.nio.file.Paths;


class Client extends JPanel{
    static Socket socket;
    static String data;
    static Scanner scanInput = new Scanner(System.in);
    static String username;

    static class Sender extends Thread {
        static DataOutputStream dataOut;


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

        }

        static synchronized public void sendData() {        
            try {
                dataOut.writeUTF(data);
                dataOut.flush();
                data = null;

            } catch(Exception e) {
                System.err.println("Sender Error");
            }
            
        }
    }

    static class Receiver extends Thread {
        static DataInputStream dataIn;
        static String returnData = null;

        public Receiver(DataInputStream dataIn) {
            this.dataIn = dataIn;
        }

        public void run() {
            while (true){
                reciveData();
            }
        }

        static synchronized public void reciveData(){
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
        //BufferedReader dataIn = new BufferedReader(new InputStreamReader(in));

        // Handle the connection
        Thread sender = new Sender(dataOut);
        Thread receiver = new Receiver(dataIn);

        sender.start();
        receiver.start();

        sender.join();
        //receiver.join(); //not joining the reviever 
    }

    public static void main(String[] args) throws Exception {
        MainWindow main = new MainWindow();
        //move into window
       // Client c = new Client(args[0], Integer.valueOf(args[1]), args[2]);
       // c.connect(); 

    }


    static class MainWindow extends JPanel {
        int windowWidth = 500;
        int windowHeight = 350;

       /* JFrame loading = new JFrame("loading My.java...please wait");
        JLabel load = new JLabel("loading data...please wait");
        JLabel blank = new JLabel(" ");
        JPanel loadPanel = new JPanel(new GridLayout(0, 3));
        loadPanel.setBackground(Color.GRAY);

        loading.setLayout(new BorderLayout(windowWidth, windowHeight));
        loading.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loading.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
        loading.setLocationRelativeTo(null);
        loading.setResizable(false);

        loadPanel.add(blank);
        loadPanel.add(load);
        loading.add(loadPanel, BorderLayout.CENTER);
        loading.setVisible(true); */
        JFrame frame = new JFrame("TEST BUILD");
        
        JPanel mainPanel = new JPanel(new BorderLayout());
       // JPanel panel2 = new JPanel(new GridLayout(1, 3));
        JPanel serverPanel = new JPanel(new FlowLayout());
        JPanel sendPanel = new JPanel (new FlowLayout());
        JPanel exitPanel = new JPanel(new FlowLayout());
        JPanel secondaryPanel = new JPanel(new BorderLayout());
        JPanel dataPanel = new JPanel(new FlowLayout());
        JPanel sendFilePanel = new JPanel(new FlowLayout());


        MainWindow(){
            setLayout(new BorderLayout());

            frame.setLayout(new BorderLayout(windowWidth, windowHeight));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            JButton exitButton = new JButton("QUIT PROGRAM"); 
            JButton serverConnect = new JButton("CONNECT TO SERVER");
            JButton sendButton = new JButton("SEND");
            JButton showResults = new JButton("TEMP RESULT");
            JButton sendFileButton = new JButton("SEND FILE");


            JTextField sendField = new JTextField("", 20);
            JTextField ipBox = new JTextField("localhost", 10);
            JTextField portBox = new JTextField("3000", 5);
            JTextField fileField = new JTextField("test.txt", 25);


            mainPanel.setBackground(Color.GRAY);
            serverPanel.setBackground(Color.GRAY);
            sendPanel.setBackground(Color.GRAY);
            exitPanel.setBackground(Color.GRAY);
            sendFilePanel.setBackground(Color.GRAY);


            JLabel reserved = new JLabel("reserved");

            dataPanel.add(reserved);

            sendPanel.add(sendButton);
            sendPanel.add(sendField);
            sendPanel.add(showResults);
            exitPanel.add(exitButton);
            serverPanel.add(serverConnect);
            serverPanel.add(ipBox);
            serverPanel.add(portBox);
            secondaryPanel.add(sendPanel, BorderLayout.NORTH);
            secondaryPanel.add(dataPanel, BorderLayout.CENTER);
            sendFilePanel.add(fileField);
            sendFilePanel.add(sendFileButton);


            secondaryPanel.add(sendFilePanel, BorderLayout.SOUTH);


            mainPanel.add(secondaryPanel, BorderLayout.CENTER);
            mainPanel.add(exitPanel, BorderLayout.SOUTH);
            mainPanel.add(serverPanel, BorderLayout.NORTH);

            frame.add(mainPanel, BorderLayout.CENTER);

        //frame.pack();
            frame.setVisible(true);
        //loading.setVisible(false);

            sendButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    data = (sendField.getText());
                    Sender.sendData();
                }          
            }); 

            showResults.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    resultWindow r = new resultWindow();
                }          
            }); 


            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
               // System.out.println("exit button clicked");
                    data = "exit";
                    Sender.sendData();
                    System.exit(0);
                }          
            }); 

            sendFileButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("send file button clicked");
                    data = "sendfile";
                    Sender.sendData();
                    sendFile(fileField.getText());
                }          
            }); 

            serverConnect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
               // System.out.println("SERVER CONNECT");
                    String ip = ipBox.getText();
                    String strPort = portBox.getText();
                    int intPort = Integer.parseInt(strPort);
                    try {
                        Client c = new Client(ip, intPort, "test client");
                        c.connect(); 
                    } catch (Exception x){
                        System.err.println("error connecting to server");
                    }
                }          
            });

        }

        synchronized public void sendFile(String filePath) {
            try{
                try{
                    File myFile = new File(filePath);
                    byte[] mybytearray = new byte[(int) myFile.length()];

                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    DataInputStream dis = new DataInputStream(bis);   
                    dis.readFully(mybytearray, 0, mybytearray.length);

                    OutputStream os = socket.getOutputStream();

                    DataOutputStream dos = new DataOutputStream(os);   
                    dos.writeUTF(myFile.getName());   
                    dos.writeLong(mybytearray.length);   
                    dos.write(mybytearray, 0, mybytearray.length);   
                    dos.flush();

                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();

                }catch (FileNotFoundException ex){
                    System.err.println("file not found");
                }
            }catch(IOException ex2){
                System.err.println("IOException at recieve file");
            }            
        }



        static class resultWindow extends JPanel {
            int windowWidth = 500;
            int windowHeight = 350;
            JFrame frame = new JFrame("RESULT");

            JPanel mainPanel = new JPanel(new BorderLayout());

            resultWindow(){
                setLayout(new BorderLayout());

                frame.setLayout(new BorderLayout(windowWidth, windowHeight));
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);

                mainPanel.setBackground(Color.GRAY);
                frame.add(mainPanel, BorderLayout.CENTER);

                frame.setVisible(true);
            }
        }
    }
}
