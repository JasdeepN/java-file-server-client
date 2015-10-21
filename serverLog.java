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

class serverLog extends JPanel{
	static JLabel label = new JLabel();
	static String msg = "";
	static JFrame mainFrame = new JFrame("server logs");
    static List<String> logs = new ArrayList<String>();

	public static void main (String[] args){
		System.out.println("server log");
		serverLog window = new serverLog();
	}

	int windowWidth = 500;
	int windowHeight = 300;
	serverLog(){
		setLayout(new BorderLayout());

		mainFrame.add(label);

		mainFrame.setLayout(new BorderLayout(windowWidth, windowHeight));
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainFrame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);

	}

	public static void updateServer(String x){
		System.out.println(x);
		msg = msg + " x";
		label.setText(msg);
		mainFrame.setVisible(true);
	}
}


