import java.awt.Graphics;

public class DiscardPile extends CardDeck{
	
	/**
	 * Discard pile is constructed in the given location
	 */
	public DiscardPile() {
		super(new Card[] {});
		this.pos_X = (int) (GameBoard.CARD_SPACE + (GameBoard.CARD_WIDTH * 1.7));
		this.pos_Y = GameBoard.CARD_TOPROW_Y;
				
	}

	/**
	 * Nobody can manually add a card here without going through the extra pile
	 */
	@Override
	boolean isValidNextCard(Card c) {
		return false;
	}
	
	/**
	 * Returns the top card.
	 * When removing cards, the only card of interest is the top card.
	 */
	@Override
	public Card[] getCardsUntil(Card c){
		return (new Card[] {this.removeTopCard()});
	}
	
	/**
	 * Adds the card to the front of the pile
	 */
	public void addCard(Card c){
		this.addToFront(c);
		c.setCoordinates(pos_X, pos_Y);
		return;
	}
	
	/**
	 * When clicked, the top card is returned.  If it is empty, null.
	 * @return
	 */
	public Card handleFirstClick(){
		if (this.isEmpty()) {
			return null;
		}
		return this.getTopCard();
	}
	
	public int getX(){
		return this.pos_X;
	}
	
	public int getY(){
		return this.pos_Y;
	}
	
	public void draw(Graphics g) {
		for (Card c : this.getDeck()){
			c.draw(g);
		}
	}
}
