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
    static OutputStream out;
    static FileInputStream fis;  
    static BufferedInputStream bis;
    static DataInputStream dis;    

    static OutputStream os;  
    static DataOutputStream dos; 





    public Client(String hostname, int port) throws Exception {
        this.socket = new Socket(hostname, port);
    }

    public void connect() throws Exception {
        InputStream in = this.socket.getInputStream();
        OutputStream out = this.socket.getOutputStream();
        DataInputStream dataIn = new DataInputStream(in);
        dos = new DataOutputStream(out);
        
    }

    public static void main(String[] args) throws Exception {
        MainWindow main = new MainWindow();
    }

    static class MainWindow extends JPanel {
        int windowWidth = 500;
        int windowHeight = 350;
        File myFile;

        JFrame frame = new JFrame("TEST BUILD");
        
        JPanel mainPanel = new JPanel(new BorderLayout());
       // JPanel panel2 = new JPanel(new GridLayout(1, 3));
        JPanel serverPanel = new JPanel(new FlowLayout());
        JPanel sendPanel = new JPanel (new FlowLayout());
        JPanel exitPanel = new JPanel(new FlowLayout());
        JPanel secondaryPanel = new JPanel(new BorderLayout());
        JPanel dataPanel = new JPanel(new FlowLayout());
        JPanel sendFilePanel = new JPanel(new FlowLayout());

        JTextField sendField = new JTextField("", 20);
        JTextField ipBox = new JTextField("127.0.0.1", 10);
        JTextField portBox = new JTextField("3000", 5);
        JTextField fileField = new JTextField("user_data/", 25);

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
                public void actionPerformed(ActionEvent e){
                    reconnect();
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

                    System.exit(0);
                }          
            }); 

            sendFileButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //System.out.println("send file button clicked");
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
                    reconnect(); 
                }     
            }); 

        }

        public void reconnect(){
            String ip = ipBox.getText();
            String strPort = portBox.getText();
            int intPort = Integer.parseInt(strPort);
            try {
                //System.out.println("reconnect");
                Client c = new Client(ip, intPort);
                c.connect(); 
                //System.out.println("connect");
            } catch (Exception x){
                System.err.println("error connecting to server");
            }
        }

        synchronized public void sendFile(String filePath) {
            try{
                try{
                    reconnect();
                    myFile = new File(filePath);

                    byte[] mybytearray = new byte[(int) myFile.length()];  

                    fis = new FileInputStream(myFile);  
                    bis = new BufferedInputStream(fis);  

                    dis = new DataInputStream(bis);     
                    dis.readFully(mybytearray, 0, mybytearray.length);  

                    os = socket.getOutputStream();  
                    dos = new DataOutputStream(os);     

                    dos.writeUTF(myFile.getName());     
                    dos.writeLong(mybytearray.length);     
                    dos.write(mybytearray, 0, mybytearray.length);     
                    dos.flush();  

        //Sending file data to the server  
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
