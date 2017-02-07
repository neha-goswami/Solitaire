import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Game implements Runnable{
	public void run() {
		final JFrame frame = new JFrame("Solitaire");
		frame.setLocation(300, 0);
		
		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		status_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// Time Remaining Label
		final JLabel timeRemainingStatus = new JLabel("Time Remaining: 30:00");
		Font font = timeRemainingStatus.getFont();
		Font toUse = new Font(font.getName(), Font.BOLD, 25);
		timeRemainingStatus.setFont(toUse);
		Border remainingBorder = new EmptyBorder(1, 5, 1, 500);
		timeRemainingStatus.setBorder(remainingBorder);
		status_panel.add(timeRemainingStatus, Component.LEFT_ALIGNMENT);
		
		// Time Elapsed Label
		final JLabel timeElapsedStatus = new JLabel("Time Elapsed: 0:00");
		timeElapsedStatus.setFont(toUse);
		Border elapsedBorder = new EmptyBorder(1, 500, 1, 5);
		timeElapsedStatus.setBorder(elapsedBorder);
		status_panel.add(timeElapsedStatus, Component.RIGHT_ALIGNMENT);

		// Main playing board
		final GameBoard board = new GameBoard(timeElapsedStatus, timeRemainingStatus);
		frame.add(board, BorderLayout.CENTER);
		
		// Instructions panel pop up
		final JPanel button_panel = new JPanel();
		button_panel.setBackground(new Color(15, 128, 45));
		JButton instructionButton = new JButton("???");
		instructionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(board,
					    "Instructions follow the normal game of Solitaire." + "\n" + "\n"
					    + "if (stillConfused){" + "\n"
					    + "     tryGoogle();" +"\n" + "}", 
					    "Instructions", JOptionPane.PLAIN_MESSAGE);
				
			}
		});
		button_panel.add(instructionButton, Component.RIGHT_ALIGNMENT);
		frame.add(button_panel, BorderLayout.NORTH);
		
		// scoreboard pop up
		JButton hiScoreButton = new JButton("Scoreboard");
		hiScoreButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				try{ 
					BufferedReader br = new BufferedReader(new FileReader("Highscores.txt"));
					
					String scores = "";
					
					for (int i = 0; i < 10; i++){
						String thisLine = br.readLine();
						if (thisLine.length() > 3) {
							scores += thisLine.substring(0, thisLine.length() - 5) + "\n";
						} else {
							scores += thisLine.substring(0, 2) + "\n";
						}
					}
					br.close();
				
					JOptionPane.showMessageDialog(board,
					     scores, 
					    "Scoreboard", JOptionPane.PLAIN_MESSAGE);
				} catch (FileNotFoundException fe) {
				      System.out.println("File Not Found: ");
			    } catch (IOException ioe) {
				      System.out.println("ioe: ");
			    }
			}
		}
		);
		button_panel.add(hiScoreButton, Component.LEFT_ALIGNMENT);
		
		// reset button 
		final JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				board.reset();
			}
		});
		button_panel.add(reset, Component.LEFT_ALIGNMENT);
		
		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}

}
