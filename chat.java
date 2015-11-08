//dependancies
import java.awt.*;
import javax.swing.*;
import java.util.*;

class chat{
//variables
	static JFrame chat_window_frame = new JFrame("pre-alpha build");

	JPanel mainPanel = new JPanel(new BorderLayout());


	int windowWidth = 600;
	int windowHeight = 350;

//end variables
//set constraints

	chat(){
		//default constructor
		chat_window_frame.setLayout(new BorderLayout(windowWidth, windowHeight));
		chat_window_frame.getContentPane().setBackground(Color.GRAY); 
		chat_window_frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		chat_window_frame.setBounds(windowWidth, windowHeight, windowWidth, windowHeight);
		chat_window_frame.setLocationRelativeTo(null);
		chat_window_frame.setResizable(false);
		chat_window_frame.add(mainPanel);
		chat_window_frame.setVisible(true);
	}
}