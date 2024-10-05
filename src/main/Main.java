//Main.java
package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main {

	
	public static void main(String[] args) {
		
		
		GridBagConstraints gbc = new GridBagConstraints();
		Board board = new Board(); //creates board object
		gbc.insets = new Insets(15,-520,15,-200);
		
		JFrame frame = new JFrame(); //new gui window
		frame.setLayout(new GridBagLayout()); //centers board within frame
		frame.setMinimumSize(new Dimension(1600,1600)); //width, height intitilizing
		frame.setLocationRelativeTo(null); //centers frame
		frame.getBackground();  
		

		JButton pass_button = new JButton("Pass");
		pass_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.pass(); // Correctly call the method on the board instance
//                System.out.println("Number of clicks: " + board.n_clicks);
              board.isBlackTurn = !board.isBlackTurn;

            }
        });
		 gbc.gridx = 0;
	     gbc.gridy = 1; // Pass button in the second row
	     
	     frame.add(pass_button, gbc); // attaches pass button
	        
	        
		JButton resign_button = new JButton("Resign");
		resign_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.resign(); // Correctly call the method on the board instance
//                System.out.println("Number of clicks: " + board.n_clicks);
            }
        });
		  gbc.gridx = 1;
	      gbc.gridy = 1; // Resign button in the third row
	        frame.add(resign_button, gbc); // attaches resign button
		
		
		frame.add(board); //attaches board
		
//		frame.setVisible(true); 
		frame.setVisible(true); 
		
	}
}