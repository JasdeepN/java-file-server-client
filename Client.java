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

    static FileInputStream fis;  
    static BufferedInputStream bis;
    static DataInputStream dis;    

    static OutputStream os;  
    static InputStream in;

    static DataOutputStream dos; 
    static DataInputStream din;


    static class Receiver extends Thread {
        static DataInputStream dataIn;
        static String returnData;

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
    


    public Client(String hostname, int port) throws Exception {
        this.socket = new Socket(hostname, port);
    }

    public void connect() throws Exception {
        in = this.socket.getInputStream();
        os = this.socket.getOutputStream();
        din = new DataInputStream(in);
        dos = new DataOutputStream(os);


        Thread receiver = new Receiver(din);


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
        File myFile;

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
            JTextField ipBox = new JTextField("127.0.0.1", 10);
            JTextField portBox = new JTextField("3000", 5);
            JTextField fileField = new JTextField("user_data/", 25);


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

            frame.setVisible(true);

            sendButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    try{
                        dos.writeUTF(sendField.getText()); 
                        dos.flush();  
                        System.out.println("Send button");
                    }catch(IOException ex){
                        System.err.println("IOException at send button");
                    } 
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
                    try{
                        dos.writeUTF("sendfile");
                        dos.flush();
                        System.exit(0);
                    } catch (Exception m){
                        System.err.println("exit error");
                    }
                }          
            }); 

            sendFileButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("send file button clicked");
                    try{
                        dos.writeUTF("sendfile");
                        dos.flush();

                        sendFile(fileField.getText());
                    }
                    catch (Exception m){
                        System.err.println("error send file");
                    }
                }          
            }); 

            serverConnect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
               // System.out.println("SERVER CONNECT");
                    String ip = ipBox.getText();
                    String strPort = portBox.getText();
                    int intPort = Integer.parseInt(strPort);
                    try {
                        Client c = new Client(ip, intPort);
                        c.connect(); 
                        System.out.println("connect");
                    } catch (Exception x){
                        System.err.println("error connecting to server");
                    }
                }          
            });

        }

        synchronized public void sendFile(String filePath) {
            try{
                try{
                    myFile = new File(filePath);

                    byte[] mybytearray = new byte[(int) myFile.length()];  

                    fis = new FileInputStream(myFile);  
                    bis = new BufferedInputStream(fis);  

                    DataInputStream tempdis = new DataInputStream(bis);     
                    tempdis.readFully(mybytearray, 0, mybytearray.length);                         

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
                System.err.println("IOException at send file");
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
