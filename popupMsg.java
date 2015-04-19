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

class popupMsg extends JPanel{
	JFrame popup = new JFrame("MESSAGE FROM SERVER");
	int windowWidth = 400;
	int windowHeight = 300;
	popupMsg(String x){

		setLayout(new BorderLayout());

		popup.setLayout(new BorderLayout(windowWidth, windowHeight));
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
		popup.setLocationRelativeTo(null);
		popup.setResizable(false);
		JPanel mainPopPanel = new JPanel(new FlowLayout());
		JPanel exitPanel = new JPanel(new FlowLayout());
		JLabel msg = new JLabel(x);
		JButton okButt = new JButton("OK");
		mainPopPanel.add(msg, BorderLayout.CENTER);
		exitPanel.add(okButt, BorderLayout.CENTER);
		popup.add(exitPanel, BorderLayout.SOUTH);
		popup.add(mainPopPanel, BorderLayout.NORTH);
		popup.setVisible(true);
		okButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				popup.setVisible(false);
			}
		}); 
	}

	popupMsg(java.util.List<String> x){
		Object[] y = x.toArray();
		setLayout(new BorderLayout());

		popup.setLayout(new BorderLayout(windowWidth, windowHeight));
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
		popup.setLocationRelativeTo(null);
		popup.setResizable(false);
		JPanel mainPopPanel = new JPanel(new FlowLayout());
		JPanel exitPanel = new JPanel(new FlowLayout());
		String output = "";
		
		for(int count = 0; count < x.size(); count++){
			output =  output + y[count].toString();
		}
		JLabel msg = new JLabel(output, SwingConstants.CENTER);
		JButton okButt = new JButton("OK");
		mainPopPanel.add(msg, BorderLayout.CENTER);
		exitPanel.add(okButt, BorderLayout.CENTER);
		popup.add(exitPanel, BorderLayout.SOUTH);
		popup.add(mainPopPanel, BorderLayout.NORTH);
		popup.setVisible(true);
		okButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				popup.setVisible(false);
			}
		}); 
	}
}