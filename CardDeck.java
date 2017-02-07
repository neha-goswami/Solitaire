import java.util.LinkedList;

public abstract class CardDeck {
	private LinkedList<Card> deck; 
	public int pos_X; //only the card decks have access to these
	public int pos_Y;
	
	//create encapsulated linkedlist of cards
	public CardDeck(Card[] cardArray){
		deck = new LinkedList<Card>();
		for (Card c : cardArray){
			deck.add(c);
		}
		pos_X = 0;
		pos_Y = 0;
	}
	
	abstract boolean isValidNextCard(Card c);
	
	abstract Card[] getCardsUntil(Card c);
	
	public void addCard(Card c){
		if (this.isValidNextCard(c)){
			deck.addLast(c);
		}
	}
	public boolean isEmpty(){
		return deck.isEmpty();
	}
	
	public Card getTopCard(){
		return deck.getLast();
	}
	
	public int numCards(){
		return deck.size();
	}
	
	public Card[] getDeck(){
		return deck.toArray(new Card[deck.size()]);
	}
	
	public Card removeTopCard(){
		Card removed = deck.removeLast();
		return removed;
	}

	public void addToBack(Card c){
		deck.addFirst(c);
		c.setCoordinates(this.pos_X, this.pos_Y);
		return;
	}
	
	public void addToFront(Card c){
		deck.addLast(c);
		c.setCoordinates(this.pos_X, this.pos_Y);
	}
	
	public boolean includesClick(int xClick, int yClick){
		if (this.isEmpty()){
			return (pos_X <= xClick && xClick <= (pos_X + GameBoard.CARD_WIDTH) 
					&& pos_Y <= yClick && yClick <= (pos_Y + GameBoard.CARD_HEIGHT));
		}
		return (pos_X <= xClick && xClick <= (pos_X + GameBoard.CARD_WIDTH) 
				&& pos_Y <= yClick && yClick <= (getTopCard().getY() + GameBoard.CARD_HEIGHT));
	}
}
