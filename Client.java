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

//CLIENT

class Client extends JPanel{
    static Socket socket;
    String data;
    FileInputStream fis;  
    BufferedInputStream bis;
    InputStream in;      
    OutputStream out;
    static DataInputStream dataIn;
    DataOutputStream dataOut; 
    static boolean isConnected = false;

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
        int windowHeight = 175;
        File myFile;

        static JFrame frame = new JFrame("Client Build v2.8.2");


        MainWindow(){


            setLayout(new BorderLayout());


            JPanel mainPanel = new JPanel(new BorderLayout());
            JPanel buttonpanel = new JPanel(new FlowLayout());
            JPanel tertiaryPanel = new JPanel(new BorderLayout());


            JPanel serverPanel = new JPanel(new FlowLayout());
            JPanel fileServer = new JPanel(new FlowLayout());

            JPanel sendPanel = new JPanel (new FlowLayout());
            JPanel exitPanel = new JPanel(new FlowLayout());
            JPanel secondaryPanel = new JPanel(new BorderLayout());
            //JPanel dataPanel = new JPanel(new FlowLayout());

            final JTextField sendField = new JTextField("TEXT COMMANDS ONLY", 20);
            JTextField loadFile4Buttons = new JTextField("FILE NAME", 20);
            final JTextField ipBox = new JTextField("127.0.0.1", 10);
            final JTextField portBox = new JTextField("3000", 5);

            frame.setLayout(new BorderLayout(windowWidth, windowHeight));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            JButton exitButton = new JButton("QUIT PROGRAM"); 
            JButton serverConnect = new JButton("CONNECT TO SERVER");
            JButton sendButton = new JButton("SEND");
            JButton fileSender = new JButton("FILE SENDER");
            JButton showServerData = new JButton("SERVER STATUS"); 
            JButton serverButton = new JButton("SERVER LOGS");

            mainPanel.setBackground(Color.GRAY);
            fileServer.setBackground(Color.GRAY);
            serverPanel.setBackground(Color.GRAY);
            sendPanel.setBackground(Color.GRAY);
            exitPanel.setBackground(Color.GRAY);
            tertiaryPanel.setBackground(Color.GRAY);
            buttonpanel.setBackground(Color.GRAY);

           // String pleasewait = "please be patient server may take some time to calculate</html>";
            //String serverRespond = "<html>if server does not respond, try reconnecting<BR>";
            //JLabel reserved = new JLabel(serverRespond + pleasewait);

           // dataPanel.add(reserved);
            fileServer.add(loadFile4Buttons);

            buttonpanel.add(serverButton);

            sendPanel.add(fileSender);
            sendPanel.add(sendField);
            sendPanel.add(sendButton);

            exitPanel.add(exitButton);
            exitPanel.add(showServerData);

            serverPanel.add(serverConnect);
            serverPanel.add(ipBox);
            serverPanel.add(portBox);
            secondaryPanel.add(sendPanel, BorderLayout.NORTH);
            secondaryPanel.add(fileServer, BorderLayout.CENTER);
           // tertiaryPanel.add(dataPanel, BorderLayout.SOUTH);

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

            showServerData.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if (isConnected == true){
                        serverView x = new serverView();
                    } else {
                        popupMsg x = new popupMsg("connect to server first", "server error", 400, 100);
                    }
                }          
            }); 

            serverButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    if (isConnected == true){
                        serverLog x = new serverLog();
                    } else {
                        popupMsg x = new popupMsg("no logs to show - connect to a server", "server error", 400, 100);
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
                    }catch(Exception ex){
                        System.err.println("unhandled exception at exit button (server not connected?)");
                        System.exit(0);

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
                        isConnected = true;
                        popupMsg x = new popupMsg("connected", "success", 400, 100);
                        serverView.updateServer("connected");
                //System.out.println("connect");
                    } catch (Exception x){
                        popupMsg y = new popupMsg("Failure, is the server running?", "error", 400, 100);
                        serverView.updateServer("failure");
                        System.err.println("could not connect to server (is it on?)");
                    }
                }     
            });

            /**
            * @ removed funtionality
            * PIE HISTOGRAM AND TOP K ACTIONS LISTENERS
            **/


            /* 
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

            */



}

}
}