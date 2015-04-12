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


class Client extends JPanel{
    Socket socket;
    static String data;
    static boolean exit = false;
    static Scanner scanInput = new Scanner(System.in);
    static String username;

    static class Sender extends Thread {
        DataOutputStream dataOut;

        
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
            while (true){
                sendData(); 
            }
        }

        synchronized public void sendData() {
            data = scanInput.nextLine();
            try {
                dataOut.writeUTF(data);
                dataOut.flush();

            } catch(Exception e) {
                System.err.println("Sender Error");
                System.exit(0);
            }
        }
    }

    static class Receiver extends Thread {
        DataInputStream dataIn;
        String returnData = null;

        public Receiver(DataInputStream dataIn) {
            this.dataIn = dataIn;
        }

        public void run() {
            while (exit == false){
                reciveData(); 
            }
        }

        synchronized public void reciveData(){
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

        // Handle the connection
        Thread sender = new Sender(dataOut);
        Thread receiver = new Receiver(dataIn);

        sender.start();
        receiver.start();

        sender.join();
        receiver.join();
    }

    public static void main(String[] args) throws Exception {
        MainWindow main = new MainWindow();
        //move into window
       // Client c = new Client(args[0], Integer.valueOf(args[1]), args[2]);
       // c.connect(); 

    }
}

class MainWindow extends JPanel {
    int windowWidth = 500;
    int windowHeight = 350;

    MainWindow(){
        setLayout(new BorderLayout());

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

        frame.setLayout(new BorderLayout(windowWidth, windowHeight));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JButton exitButton = new JButton("QUIT PROGRAM"); 
        JButton serverConnect = new JButton("CONNECT TO SERVER");
        JButton sendButton = new JButton("SEND");


        JTextField sendField = new JTextField("", 20);
        JTextField ipBox = new JTextField(" enter server IP");
        JTextField portBox = new JTextField(" enter server port");


        JPanel mainPanel = new JPanel(new BorderLayout());
       // JPanel panel2 = new JPanel(new GridLayout(1, 3));
        JPanel serverPanel = new JPanel(new FlowLayout());
        JPanel secondaryPanel = new JPanel (new FlowLayout());
        JPanel exitPanel = new JPanel(new FlowLayout());



        mainPanel.setBackground(Color.GRAY);
        //serverPanel.setBackground(Color.GRAY);


      //  serverPanel.add(new visual());
      // serverPanel.add(new Legend());
        secondaryPanel.add(sendButton);
        secondaryPanel.add(sendField);
        exitPanel.add(exitButton);
        serverPanel.add(serverConnect);
        serverPanel.add(ipBox);
        serverPanel.add(portBox);

        mainPanel.add(secondaryPanel, BorderLayout.CENTER);
        mainPanel.add(exitPanel, BorderLayout.SOUTH);
        mainPanel.add(serverPanel, BorderLayout.NORTH);

        frame.add(mainPanel, BorderLayout.CENTER);

        //frame.pack();
        frame.setVisible(true);
        //loading.setVisible(false);

        
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("exit button clicked");
                System.exit(0);
            }          
        }); 

        serverConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("SERVER CONNECT");
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
}