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
    String data;
    FileInputStream fis;  
    BufferedInputStream bis;
    InputStream in;      
    OutputStream out;
    static DataInputStream dataIn;
    DataOutputStream dataOut; 

    public Client(String hostname, int port) throws Exception {
        this.socket = new Socket(hostname, port);
        connect();
    }

    public void connect() throws Exception {
        this.in = this.socket.getInputStream();
        this.out = this.socket.getOutputStream();
        this.dataIn = new DataInputStream(in);
        this.dataOut = new DataOutputStream(out);
        Receiver getData = new Receiver(this.dataIn);
        getData.start();
    }

    public static void main(String[] args) throws Exception {
        MainWindow main = new MainWindow();
    }

    static class MainWindow extends JPanel {

        static Client c;
        int windowWidth = 500;
        int windowHeight = 250;
        File myFile;

        static JFrame frame = new JFrame("Final Build");


        MainWindow(){


            setLayout(new BorderLayout());


            JPanel mainPanel = new JPanel(new BorderLayout());
            JPanel buttonpanel = new JPanel(new FlowLayout());
            JPanel tertiaryPanel = new JPanel(new BorderLayout());

            String[] sort = new String[] {"sector", "employer", "position", "name"};
            JComboBox<String> comboSort = new JComboBox<String>(sort);


       // JPanel panel2 = new JPanel(new GridLayout(1, 3));
            JPanel serverPanel = new JPanel(new FlowLayout());
            JPanel fileServer = new JPanel(new FlowLayout());

            JPanel sendPanel = new JPanel (new FlowLayout());
            JPanel exitPanel = new JPanel(new FlowLayout());
            JPanel secondaryPanel = new JPanel(new BorderLayout());
            JPanel dataPanel = new JPanel(new FlowLayout());

            JTextField sendField = new JTextField("TEXT COMMANDS ONLY", 20);
            JTextField loadFile4Buttons = new JTextField("FILE NAME", 20);
            JTextField ipBox = new JTextField("127.0.0.1", 10);
            JTextField portBox = new JTextField("3000", 5);
            JTextField kbox = new JTextField("5", 3);

            frame.setLayout(new BorderLayout(windowWidth, windowHeight));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            JButton exitButton = new JButton("QUIT PROGRAM"); 
            JButton serverConnect = new JButton("CONNECT TO SERVER");
            JButton sendButton = new JButton("SEND");
            JButton fileSender = new JButton("FILE SENDER");
            JButton pieButton = new JButton("PIE");
            JButton hisButton = new JButton("HIS");
            JButton topKButton = new JButton("TOP K");

            JButton availbutton = new JButton("SHOW FILES");

            mainPanel.setBackground(Color.GRAY);
            fileServer.setBackground(Color.GRAY);
            serverPanel.setBackground(Color.GRAY);
            sendPanel.setBackground(Color.GRAY);
            exitPanel.setBackground(Color.GRAY);
            tertiaryPanel.setBackground(Color.GRAY);
            buttonpanel.setBackground(Color.GRAY);

            String pleasewait = "please be patient server may take some time to calculate</html>";
            JLabel reserved = new JLabel("<html>if server does not respond, try reconnecting<BR>" + pleasewait);

            dataPanel.add(reserved);
            buttonpanel.add(availbutton);
            buttonpanel.add(pieButton);
            fileServer.add(loadFile4Buttons);

            buttonpanel.add(hisButton);
            buttonpanel.add(topKButton);
            buttonpanel.add(kbox);
            buttonpanel.add(comboSort);


            sendPanel.add(sendButton);
            sendPanel.add(sendField);
            sendPanel.add(fileSender);
            exitPanel.add(exitButton);
            serverPanel.add(serverConnect);
            serverPanel.add(ipBox);
            serverPanel.add(portBox);
            secondaryPanel.add(sendPanel, BorderLayout.NORTH);
            secondaryPanel.add(fileServer, BorderLayout.CENTER);
            tertiaryPanel.add(dataPanel, BorderLayout.SOUTH);

            tertiaryPanel.add(secondaryPanel, BorderLayout.NORTH);
            tertiaryPanel.add(buttonpanel, BorderLayout.CENTER);

            mainPanel.add(tertiaryPanel, BorderLayout.CENTER);
            mainPanel.add(exitPanel, BorderLayout.SOUTH);
            mainPanel.add(serverPanel, BorderLayout.NORTH);

            frame.add(mainPanel, BorderLayout.CENTER);

        //frame.pack();
            frame.setVisible(true);
        //loading.setVisible(false);


            sendButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                   // reconnect();
                    try{
                        c.dataOut.writeUTF(sendField.getText()); 
                        c.dataOut.flush();  
                    }catch(IOException ex){
                        System.err.println("IOException at send button");
                    } 
                }          
            }); 

            fileSender.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){


                    fileSender r = new fileSender();
                }          
            }); 


            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
               // System.out.println("exit button clicked");
                    try{
                        c.dataOut.writeUTF("exit");
                        c.dataOut.flush();
                        System.exit(0);
                    }catch(IOException ex){
                        System.err.println("IOException at exit button");
                        System.exit(0);

                    }
                }          
            }); 

            pieButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
               // System.out.println("exit button clicked");
                    try{

                        c.dataOut.writeUTF("pie " + loadFile4Buttons.getText());
                        c.dataOut.flush();
                        
                    }catch(Exception ex){
                        System.err.println(ex + " Exception at loadfile (pie) button");
                    }
                }          
            }); 

            hisButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
               // System.out.println("exit button clicked");
                    try{

                        c.dataOut.writeUTF("his");
                        c.dataOut.flush();
                        
                    }catch(Exception ex){
                        System.err.println(ex + " Exception at loadfile (his) button");
                    }
                }          
            }); 

            topKButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
               // System.out.println("exit button clicked");
                    try{

                        c.dataOut.writeUTF("top " + loadFile4Buttons.getText()+ " " +comboSort.getSelectedItem().toString() + " " +kbox.getText());
                        c.dataOut.flush();
                        
                    }catch(Exception ex){
                        System.err.println(ex + " Exception at loadfile (topk) button");
                    }
                }          
            }); 

            availbutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
               // System.out.println("exit button clicked");
                    try{
                        c.dataOut.writeUTF("files");
                        c.dataOut.flush();
                    }catch(IOException ex){
                        System.err.println(ex+"IOException at availbutton button");
                    }
                }          
            });



            serverConnect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String ip = ipBox.getText();
                    String strPort = portBox.getText();
                    int intPort = Integer.parseInt(strPort);
                    try {
                //System.out.println("reconnect");
                        c = new Client(ip, intPort);
                        c.connect(); 
                //System.out.println("connect");
                    } catch (Exception x){
                        System.err.println("error connecting to server");
                    }
                }     
            }); 

        }

    }
}