import java.awt.Graphics;

public class ExtraPile extends CardDeck{
	private DiscardPile discardPile = null;
	
	/** 
	 * @param cardArray
	 * Constructs an extra pile given a specific card array and places it in the right spot
	 */
	public ExtraPile(Card[] cardArray, DiscardPile dp) {
		super(cardArray);
		this.pos_X = GameBoard.CARD_SPACE;
		this.pos_Y = GameBoard.CARD_TOPROW_Y;
		for (int i = 0; i < cardArray.length; i++) {
			cardArray[i].setCoordinates(pos_X, pos_Y);
		}
		this.discardPile = dp;
	}
	
	/** 
	 * No card can be added to the top by the user.  It can only be refilled from the discard pile, which has its 
	 * own way of doing so
	 */
	@Override
	public boolean isValidNextCard(Card c){
		return false;
	}
	
	/**
	 * Will refill cards from the discard pile if empty, else it will send a card to the discard pile.
	 * @return
	 */
	public Card handleFirstClick(){
		if (!this.isEmpty()){
			Card newCard = this.removeTopCard();
			newCard.flip();
			discardPile.addCard(newCard);
			return newCard;
		}
		while (!discardPile.isEmpty()){
			Card tempCard = discardPile.removeTopCard();
			tempCard.flip();
			this.addToFront(tempCard);
		}
		return null;
	}
	
	/**
	 * There are no cards needed to get until this card.  None should be removed.
	 */
	@Override
	public Card[] getCardsUntil(Card c){
		return (new Card[] {});
	}
	
	public void draw(Graphics g) {
		for (Card c : this.getDeck()){
			c.draw(g);
		}
	}
	

}
