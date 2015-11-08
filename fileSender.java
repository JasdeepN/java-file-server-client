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
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;


class fileSender extends JPanel {
    static Socket socket;
    String data;
    FileInputStream fis;  
    BufferedInputStream bis;
    InputStream in;      
    OutputStream out;
    static DataInputStream dataIn;
    DataOutputStream dataOut; 

    public static void main(String[] args) throws Exception {
        fileSender main = new fileSender();
    }

    public void connect(String x, int y) throws Exception{
        this.socket = new Socket(x, y);
        
        this.in = this.socket.getInputStream();
        this.out = this.socket.getOutputStream();
        this.dataIn = new DataInputStream(in);
        this.dataOut = new DataOutputStream(out);
    }

    public void closeConnection() throws Exception{
        this.in.close();
        this.out.close();
        this.dataOut.close();
        this.dataIn.close();
        this.socket.close();
    }

    int windowWidth = 500;
    int windowHeight = 125;

    static JFrame fileFrame = new JFrame("File Manager v2.8.1");

    JPanel mainPanel = new JPanel(new BorderLayout());

    JTextField ipBox = new JTextField("127.0.0.1", 10);
    JTextField portBox = new JTextField("3000", 5);
    JTextField fileField = new JTextField("user_data/", 25);

    fileSender(){


        setLayout(new BorderLayout());


        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel exitPanel = new JPanel(new FlowLayout());
        JPanel sendFilePanel = new JPanel(new FlowLayout());
        JPanel secondaryPanel = new JPanel(new BorderLayout());
        JPanel serverPanel = new JPanel(new FlowLayout());

        

        fileFrame.setLayout(new BorderLayout(windowWidth, windowHeight));
        fileFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        fileFrame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
        fileFrame.setLocationRelativeTo(null);
        fileFrame.setResizable(false);

        JButton gobackButton = new JButton("BACK"); 
        JButton test = new JButton("test");
        JButton sendFileButton = new JButton("SEND FILE");
        JButton serverConnect = new JButton("CONNECT TO FILE SERVER");
        JButton availbutton = new JButton("SHOW FILES");


        mainPanel.setBackground(Color.GRAY);
        exitPanel.setBackground(Color.GRAY);
        sendFilePanel.setBackground(Color.GRAY);
        serverPanel.setBackground(Color.GRAY);

        serverPanel.add(serverConnect);
        serverPanel.add(ipBox);
        serverPanel.add(portBox);
        exitPanel.add(gobackButton);
        exitPanel.add(test);
        sendFilePanel.add(availbutton);
        sendFilePanel.add(fileField);
        sendFilePanel.add(sendFileButton);


        secondaryPanel.add(serverPanel, BorderLayout.NORTH);
        secondaryPanel.add(sendFilePanel, BorderLayout.CENTER);

        mainPanel.add(secondaryPanel, BorderLayout.NORTH);

        mainPanel.add(exitPanel, BorderLayout.SOUTH);

        fileFrame.add(mainPanel, BorderLayout.CENTER);

        //fileFrame.pack();
        fileFrame.setVisible(true);
        //loading.setVisible(false);

        gobackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               // System.out.println("exit button clicked");
                //System.exit(0);
                fileFrame.dispose();

            }          
        });

        test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = null;
                // on Windows, retrieve the path of the "Program Files" folder
                File file = new File(System.getenv("home"));

                try {
                    desktop = Desktop.getDesktop();
                    desktop.open(file);

                }catch (IOException l){ 
                    System.err.println("didnt work");
                }
            }          
        });

        availbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               // System.out.println("exit button clicked");
                try{
                    try{
                        dataOut.writeUTF("files");
                        dataOut.flush();
                    }catch(IOException ex){
                        System.err.println(ex+"IOException at availbutton button");
                    }

                }catch(NullPointerException m){
                    System.err.println("NullPointerException at availbutton");
                }
            }          
        });

        sendFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try{
                    sendFile(); 
                }catch(Exception ex){
                    System.err.println(ex + " sendfile button");
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
                    connect(ip, intPort);
                    Receiver getData = new Receiver(fileSender.dataIn);
                    getData.start();
                //System.out.println("connect");
                } catch (Exception x){
                    System.err.println("error connecting to server");
                }
            }     
        }); 

    }


    synchronized public void sendFile() throws Exception{

        try{
            dataOut.writeUTF("sendfile");
            dataOut.flush();

            File myFile = new File(fileField.getText());

            byte[] mybytearray = new byte[(int) myFile.length()];  

            fis = new FileInputStream(myFile);  
            bis = new BufferedInputStream(fis);  

            dataIn = new DataInputStream(bis);     
            dataIn.readFully(mybytearray, 0, mybytearray.length);  

            out = socket.getOutputStream();  
            dataOut = new DataOutputStream(out);     

            dataOut.writeUTF(myFile.getName());     
            dataOut.writeLong(mybytearray.length);     
            dataOut.write(mybytearray, 0, mybytearray.length);     
            dataOut.flush();  

        //Sending file data to the server  
            out.write(mybytearray, 0, mybytearray.length);  
            out.flush();
            closeConnection();  
        }catch (Exception ex){
            System.err.println(ex + " sendFile()");
        }

    }

}

