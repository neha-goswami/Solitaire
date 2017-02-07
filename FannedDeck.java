import java.awt.Graphics;

public class FannedDeck extends CardDeck{
	
	/**
	 * instantiate fanned card deck, staggers the vertical orientation, flips the top card to be face up
	 * 
	 * @param cardArray
	 * @param x
	 * @param y
	 */
	public FannedDeck(Card[] cardArray, int x, int y){
		super(cardArray);
		this.pos_X = x;
		this.pos_Y = y;
		int space = 60;
		for (int i = 0; i < cardArray.length; i++){
			cardArray[i].setCoordinates(pos_X, pos_Y + (i * space));
		}
		Card topCard = cardArray[cardArray.length - 1];
		topCard.flip();
	}

	// valid cards must be of descending value and alternating color
	@Override
	boolean isValidNextCard(Card c) {
		if (isEmpty()) {
			if (c.getValue() == 13) {
				return true;
			}
			return false;
		}
		Card topCard = getTopCard();
		return (c.getValue() == topCard.getValue() - 1 && 
				!(c.getColor() == topCard.getColor()));
	}
	//added cards must be staggered vertically
	@Override
	public void addCard(Card c){
		if (isValidNextCard(c)){
			if (this.isEmpty()){
				this.addToFront(c);
				return;
			}
			Card oldTop = this.getTopCard();
			this.addToFront(c);
			c.setCoordinates(this.pos_X, oldTop.getY() + 60);
			}
		}
	
	public void draw(Graphics g) {
		for (Card c : this.getDeck()){
			c.draw(g);
		}
	}
	
	//determines if any of the cards have been clicked, checks from topmost to backmost
	public Card highestCardClicked(int xClick, int yClick){
		Card[] cTemp = this.getDeck();
		Card highCard = null;
		for (int i = 0; i < cTemp.length; i++){
			if (cTemp[i].includesClick(xClick, yClick)){
				highCard = cTemp[i];
			}
		}
		return highCard;
	}
	
	//in order to drag multiple cards to another fanned deck, we must remove all of them below the highest clicked
	@Override
	public Card[] getCardsUntil(Card highestCard){
		Card[] temp = new Card[highestCard.getValue() - this.getTopCard().getValue() + 1];
		for (int i = 0; i < temp.length; i++){
			temp[temp.length - 1 - i] = this.removeTopCard();
		}
		return temp; //in order from highest to lowest
	}

}
