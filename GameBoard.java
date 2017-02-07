import java.awt.*;


import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel{
	private static final int BOARD_WIDTH = 2000; //made private instead of public
	private static final int BOARD_HEIGHT = 1500;
	
	private static final int INTERVAL = 500;
	private static final int MAX_TIME = 900; // must be a multiple of 60, must be in whole minutes.
	
	public static final int CARD_WIDTH = BOARD_WIDTH / 12;
	public static final int CARD_SPACE = CARD_WIDTH / 2;
	public static final int CARD_HEIGHT = BOARD_HEIGHT / 7;
	public static final int CARD_TOPROW_Y = BOARD_HEIGHT / 12;
	public static final int CARD_BOTROW_Y = (int) (CARD_SPACE + (CARD_HEIGHT * 1.4));
	
	private FannedDeck fDeck1;//= new FannedDeck(cd, CARD_SPACE, CARD_BOTROW_Y);
	private FannedDeck fDeck2;
	private FannedDeck fDeck3;
	private FannedDeck fDeck4;
	private FannedDeck fDeck5;
	private FannedDeck fDeck6;
	private FannedDeck fDeck7;
	private ExtraPile extraPile;
	private DiscardPile discardPile;
	private CollectionPile clubPile;
	private CollectionPile spadePile;
	private CollectionPile diamondPile;
	private CollectionPile heartPile;
	
	private CardDeck[] allDecks = new CardDeck[13];
	private FannedDeck[] fDeckArr = new FannedDeck[7];
	private CollectionPile[] cDeckArr = new CollectionPile[4];
	
	private Mode mode;
	
	private final static float[] dash1 = {10.0f};
	private static final BasicStroke dashed = new BasicStroke(4.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 
			2.0f, dash1, 0.0f);
	private Rectangle2D.Double[] rectPreview = new Rectangle2D.Double[1];
	
	
	private int seconds = 0;
	private boolean isStillPlaying = true;
	
	private JLabel timeElapsedStatus;
	private JLabel timeRemainingStatus;
	
	public GameBoard(JLabel timeElapsedStatus, JLabel timeRemainingStatus) {
		
		//set background color
		this.setBackground(new Color(15, 128, 45));
		
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); 
		
		this.timeElapsedStatus = timeElapsedStatus;
		this.timeRemainingStatus = timeRemainingStatus;
		
		ArrayList<Card> mainDeck = createMasterDeck();
		initializeDecks(mainDeck);
		
		// Initialize mouse listener
		mode = Mode.SELECT_START_MODE;
		Mouse mouseListener = new Mouse();
		this.addMouseListener(mouseListener); 
		
	}
	
	void tick() {
		// check for the game end conditions (running out of max time)
		if (seconds < MAX_TIME && isStillPlaying) {
			seconds++;
			int minLeft = (MAX_TIME - seconds) / 60;
			int secondsElapsed = seconds % 60;
			String secElapsed = "";
			if (secondsElapsed < 10) {
				secElapsed += "0";
			}
			secElapsed += Integer.toString(secondsElapsed);
			
			int secondsLeft = 60 - secondsElapsed;
			if (secondsLeft == 60) {
				secondsLeft = 0;
			}
			String secLeft = "";
			if (secondsLeft < 10) {
				secLeft += "0";
			}
			secLeft += Integer.toString(secondsLeft);
			
			//update labels for time remaining and time elapsed every second
			timeElapsedStatus.setText("Time elapsed: " + (seconds / 60) + ":" + secElapsed);
			timeRemainingStatus.setText("Time Remaining: " + minLeft + ":" + secLeft);
			
		} else if (!isStillPlaying) {
			timeRemainingStatus.setText("Congratulations! You won.");
			addNameToHighScore();
			this.reset();
			
		} else {
			timeElapsedStatus.setText("Time's up! Game over. You lost.");
		}
		repaint();
	}
	
	private void addNameToHighScore(){
		String winner = JOptionPane.showInputDialog("Nice. Name?");
		
		// Read in the high score file, write in name and time if it is in the top 10. 
		try { 
			BufferedReader br = new BufferedReader(new FileReader("Highscores.txt"));
			Map<Integer, String> scores = new TreeMap<Integer, String>();
			
			String minsWon = "" + (seconds / 60);
			if (Integer.parseInt(minsWon) < 10){
				minsWon = "0" + minsWon;
			}
			
			String secondsWon = "" + (seconds % 60);
			if (Integer.parseInt(secondsWon) < 10){
				secondsWon = "0" + secondsWon;
			}
			
			String secString = "" + seconds;
			while (secString.length() < 3){
				secString = "0" + secString;
			}
			
			//prepare the string to enter into the file
			String winningString = (". " + winner + ": " + minsWon + ":" + secondsWon + " - " + secString);
			System.out.println(winningString);
			
			boolean included = false;
			// Add every entry in the file to a map.  Key of the map is its rank, Value is the string username
			for (int i = 0; i < 10; i++){
				String textString = br.readLine();
				String oldName = ".";
				if (textString.length() > 3){
				    oldName = textString.substring(1);
				    System.out.println(oldName);
					int oldSeconds = Integer.parseInt(textString.substring(textString.length() - 3));

					// If the user has beaten the next lower entry, add first the user and the next entry after.  
					if (seconds <= oldSeconds && !included) {
						scores.put(i, winningString);
						scores.put(i + 1, oldName);
						i++;
						included = true;
					}
				}
				scores.put(i, oldName);
			}
			br.close();
			
			// Write back the 10 values in the map to the file
			BufferedWriter bw = new BufferedWriter(new FileWriter("Highscores.txt"));
			for (int i = 0; i < 10; i++){
				bw.write("" + (i + 1));
				bw.write(scores.get(i));
				bw.newLine();
			}
			bw.close();
		      
		    } catch (FileNotFoundException e) {
		      System.out.println("File Not Found: ");
		    } catch (IOException ioe) {
			      System.out.println("ioe: ");
			    }

	}
	
	//Reset game by reinitializing decks and restarting the timer
	public void reset() {
		initializeDecks(createMasterDeck());
		seconds = 0;
		requestFocusInWindow();
	}
	
	// Used to populate a shuffled master deck.  Kept as an ArrayList so I could shuffle it. 
	private ArrayList<Card> createMasterDeck(){
		Suit[] suits = {Suit.CLUB, Suit.SPADE, Suit.DIAMOND, Suit.HEART};
		ArrayList<Card> masterDeck = new ArrayList<Card>(52);
		for (Suit s : suits) {
			for (int i = 1; i <= 13; i++) {
				masterDeck.add(new Card(s, i));
			}
		}
		Collections.shuffle(masterDeck);
		return masterDeck;
	}
	
	private void initializeDecks(ArrayList<Card> masterDeck){
		discardPile = new DiscardPile();
		extraPile = new ExtraPile(deckArray(24, masterDeck), discardPile);
		fDeck1 = new FannedDeck(deckArray(1, masterDeck), CARD_SPACE, CARD_BOTROW_Y);
		fDeck2 = new FannedDeck(deckArray(2, masterDeck), (int) (CARD_SPACE + (CARD_WIDTH * 1.7)), CARD_BOTROW_Y);
		fDeck3 = new FannedDeck(deckArray(3, masterDeck), (int) (CARD_SPACE + (CARD_WIDTH * 2 * 1.7)), CARD_BOTROW_Y);
		fDeck4 = new FannedDeck(deckArray(4, masterDeck), (int) (CARD_SPACE + (CARD_WIDTH * 3 * 1.7)), CARD_BOTROW_Y);
		fDeck5 = new FannedDeck(deckArray(5, masterDeck), (int) (CARD_SPACE + (CARD_WIDTH * 4 * 1.7)), CARD_BOTROW_Y);
		fDeck6 = new FannedDeck(deckArray(6, masterDeck), (int) (CARD_SPACE + (CARD_WIDTH * 5 * 1.7)), CARD_BOTROW_Y);
		fDeck7 = new FannedDeck(deckArray(7, masterDeck), (int) (CARD_SPACE + (CARD_WIDTH * 6 * 1.7)), CARD_BOTROW_Y);
		clubPile = new CollectionPile(Suit.CLUB, (int) (CARD_SPACE + (CARD_WIDTH * 3 * 1.7)), CARD_TOPROW_Y);
		spadePile = new CollectionPile(Suit.SPADE, (int) (CARD_SPACE + (CARD_WIDTH * 4 * 1.7)), CARD_TOPROW_Y);
		diamondPile = new CollectionPile(Suit.DIAMOND, (int) (CARD_SPACE + (CARD_WIDTH * 5 * 1.7)), CARD_TOPROW_Y);
		heartPile = new CollectionPile(Suit.HEART, (int) (CARD_SPACE + (CARD_WIDTH * 6 * 1.7)), CARD_TOPROW_Y);
		
		allDecks[0] = extraPile;
		
		fDeckArr[0] = fDeck1;
		fDeckArr[1] = fDeck2;
		fDeckArr[2] = fDeck3;
		fDeckArr[3] = fDeck4;
		fDeckArr[4] = fDeck5;
		fDeckArr[5] = fDeck6;
		fDeckArr[6] = fDeck7;
		
		cDeckArr[0] = clubPile;
		cDeckArr[1] = spadePile;
		cDeckArr[2] = diamondPile;
		cDeckArr[3] = heartPile;
		
	}
	
	//puts all of the array list into an array
	private Card[] deckArray(int size, ArrayList<Card> masterDeck){
		Card[] cTemp = new Card[size];
		for (int i = 0; i < cTemp.length; i++){
			cTemp[i] = masterDeck.remove(0);
		}
		return cTemp;
	}
	

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
	}
	
	//used to process mouse clicks
	private class Mouse extends MouseAdapter {
		/** Location of the mouse when it was last pressed. */
		private Card highestCard = null;
		private CardDeck initialDeck = null;

		/** Code to execute when the button is pressed while the mouse 
		 *  is in the canvas. 
		 *
		 *  This method is in the Paint class so that it can access and 
		 *  update the state of the paint application. 
		 */
		//used to determine if an array of decks contains a certain click
		private boolean deckArrayIncludes(CardDeck[] cd, int x, int y){
			boolean acc = false;
			for (int i = 0; i < cd.length; i++){
				acc = acc || (cd[i].includesClick(x, y));
			}
			return acc;
		}
		
		
		//Only 2 mouse modes - mouse processing was made based on switch cases. 
		@Override
		public void mousePressed(MouseEvent arg0){
			rectPreview[0] = null; 
			Point p = arg0.getPoint();
			switch (mode) {
			case SELECT_START_MODE: 
				// The extra pile handles the first click if the extra pile was clicked
				if (extraPile.includesClick(p.x, p.y)){
					extraPile.handleFirstClick();
					
				//The discard pile handles the click if it was clicked	
				} else if (discardPile.includesClick(p.x, p.y)) {
					if (!discardPile.isEmpty()){
						highestCard = discardPile.handleFirstClick();
						mode      = Mode.SELECT_END_MODE;
						initialDeck = discardPile;
						rectPreview[0] = new Rectangle2D.Double(discardPile.getX(), discardPile.getY(), 
								CARD_WIDTH, CARD_HEIGHT);
					}
				
				// A fanned deck handles a click if a fanned deck was clicked	
				} else if (deckArrayIncludes(fDeckArr, p.x, p.y)){
					FannedDeck fTemp = null;
					//determine which fanned deck was clicked
					for (int i = 0; i < 7; i++) {
						if (fDeckArr[i].includesClick(p.x, p.y)){
							fTemp = fDeckArr[i];
							break;
						}
					}
					//save as initial deck (abstract!!)
					initialDeck = fTemp;
					if (initialDeck.isEmpty()){
						break;
					}
					
					highestCard = Card.highestCardOfClick(fTemp, p.x, p.y);
					if (!highestCard.isFaceUp()){	
						// If the card clicked is face down and is the topmost card, we flip it.  Else, do nothing.
						if (highestCard.equals(fTemp.getTopCard())){	
							highestCard.flip();
							highestCard = null;
							initialDeck = null;
							break;
							}
						break;
					}
						mode      = Mode.SELECT_END_MODE;
						rectPreview[0] = new Rectangle2D.Double(highestCard.getX(), highestCard.getY(), 
								CARD_WIDTH, fTemp.getTopCard().getY() + CARD_HEIGHT - highestCard.getY());
					} 
				break;
				
			case SELECT_END_MODE:
				mode      = Mode.SELECT_START_MODE;
				
				// if the second click is in a fanned deck, we see if we can add the previous card/cards
				if (deckArrayIncludes(fDeckArr, p.x, p.y)){
					mode      = Mode.SELECT_START_MODE;
					FannedDeck fTemp = null;
					for (int i = 0; i < 7; i++) {
						if (fDeckArr[i].includesClick(p.x, p.y)){
							fTemp = fDeckArr[i];
							break;
						}
					}
					
					//add all cards until the selected card to the fanned deck
					if (fTemp.isValidNextCard(highestCard)){
						Card[] newCards = initialDeck.getCardsUntil(highestCard);
						for (int i = 0; i < newCards.length; i++){
							fTemp.addCard(newCards[i]);
						}
					}
					
				// Add card into the chosen collection pile
				} else if (deckArrayIncludes(cDeckArr, p.x, p.y)){
					CollectionPile cTemp = null;
					for (int i = 0; i < 4; i++) {
						if (cDeckArr[i].includesClick(p.x, p.y)){
							cTemp = cDeckArr[i];
							break;
						}
					}
					if (initialDeck.getTopCard().equals(highestCard) 
							&& cTemp.isValidNextCard(highestCard)) {
						cTemp.addCard(initialDeck.removeTopCard());
						
						//Game is won when all decks have kings in them
						isStillPlaying = (clubPile.numCards() != 13) ||
								(spadePile.numCards() != 13) ||
								(diamondPile.numCards() != 13) ||
								(diamondPile.numCards() != 13);
					}
				}
				break;
			}
			repaint();
		}
	}
	
	
	// Draw piles/all cards in the pile, and draw the highlighted selected cards
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(dashed);
		
		//Draw dashed card locations
		for (int i = 0; i < 7; i++) {
			if (i != 2) {
			g2.draw(new Rectangle2D.Double(CARD_SPACE + (CARD_WIDTH * i * 1.7), CARD_TOPROW_Y, 
					CARD_WIDTH, CARD_HEIGHT));
			}
			g2.draw(new Rectangle2D.Double(CARD_SPACE + (CARD_WIDTH * i * 1.7), CARD_BOTROW_Y, 
					CARD_WIDTH, CARD_HEIGHT));
		}
		fDeck1.draw(g);
		fDeck2.draw(g);
		fDeck3.draw(g);
		fDeck4.draw(g);
		fDeck5.draw(g);
		fDeck6.draw(g);
		fDeck7.draw(g);
		extraPile.draw(g);
		discardPile.draw(g);
		clubPile.draw(g);
		spadePile.draw(g);
		diamondPile.draw(g);
		heartPile.draw(g);
		
		g2.setColor(Color.CYAN);
		if (!(rectPreview[0] == null) ) {
			g2.draw(rectPreview[0]);
		}
	}

}
