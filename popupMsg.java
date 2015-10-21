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

/**
 * @author Jasdeep Nijjar
 *
 * @descritption Creates a new windows to display a message that was sent to the method 
 *
 * @pram String X, where X is the message to be displayed on the new window
 * @pram util List of type String, a list of messages to be displayed useful to save multiple messages and display them at once	 
 */

class popupMsg extends JPanel{

	popupMsg(String x, String windowName, int windowWidth, int windowHeight){
		final JFrame popup = new JFrame(windowName); //MAKE NAME CHANGE BASED ON CALLING METHOD (CHANGE CONSTRUCTOR)

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

	popupMsg(java.util.List<String> x, String windowName, int windowWidth, int windowHeight){
		final JFrame popup = new JFrame(windowName);

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