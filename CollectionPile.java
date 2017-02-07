import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class CollectionPile extends CardDeck{
	private Suit goalSuit;
	
	/**
	 * Construct a collection pile in the given location with the given suit
	 * @param s
	 * @param x
	 * @param y
	 */
	public CollectionPile(Suit s, int x, int y) {
		super(new Card[] {});
		this.pos_X = x;
		this.pos_Y = y;
		goalSuit = s;
	}
	
	/**
	 * There is no way to get cards away from this deck, returns an empty card deck
	 */
	@Override
	public Card[] getCardsUntil(Card c){
		return (new Card[] {});
	}
	
	/**
	 * The next card will be added to the front of the pile if it is a valid card.  
	 * The card's coordinates will be changed to that
	 * of the collection pile
	 */
	public void addCard(Card c){
		if (isValidNextCard(c)) {
    		this.addToFront(c);
	    	c.setCoordinates(pos_X, pos_Y);
    	}
		return;
	}
		
	
	/**
	 * Determines wheter the given card is valid or not.  Valid cards are of ascending order and of same suit.
	 */
	@Override
	boolean isValidNextCard(Card c) {
		if (this.isEmpty()){
			return (c.getSuit() == goalSuit && c.getValue() == 1);
		}  
		return (c.getSuit() == goalSuit && c.getValue() == this.getTopCard().getValue() + 1);
	}
	
	// Draw a little background showing the suit of the card pile, then draw all cards on top. 
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		switch (goalSuit) {
		case CLUB:
			g2.setColor(new Color(0, 0, 0, 150));
			g2.fillOval((int) (this.pos_X + GameBoard.CARD_WIDTH * 0.31), 
					(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.31), 
					70, 70);
			break;
		case SPADE: 
			g2.setColor(new Color(0, 0, 0, 150));
			g2.fillRect((int) (this.pos_X + GameBoard.CARD_WIDTH * 0.31), 
					(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.31), 
					70, 70);
			break;
		case HEART:
			g2.setColor(new Color(255, 0, 0, 150));
			int[] xpoints = {(int) (this.pos_X + GameBoard.CARD_WIDTH * 0.33), 
					(int) (this.pos_X + GameBoard.CARD_WIDTH * 0.5), 
					(int) (this.pos_X + GameBoard.CARD_WIDTH * 0.67)};
			int[] ypoints = {(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.67), 
					(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.33),
					(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.67)};
			g2.fillPolygon(xpoints, ypoints, 3);
			break;
		case DIAMOND: 
			g2.setColor(new Color(255, 0, 0));
			int[] xpoints1 = {(int) (this.pos_X + GameBoard.CARD_WIDTH * 0.33), 
					(int) (this.pos_X + GameBoard.CARD_WIDTH * 0.5), 
					(int) (this.pos_X + GameBoard.CARD_WIDTH * 0.67)};
			int[] ypoints1 = {(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.6), 
					(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.25),
					(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.6)};
			int[] ypoints2 = {(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.35), 
					(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.7),
					(int) (this.pos_Y + GameBoard.CARD_HEIGHT * 0.35)};
			g2.fillPolygon(xpoints1, ypoints1, 3);
			g2.fillPolygon(xpoints1, ypoints2, 3);
			break;
		}
		
		for (Card c : this.getDeck()){
			c.draw(g);
		}
	}

}
