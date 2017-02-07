import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Card {
	private Suit suit; 
	private int value;
	private Color color;
	private boolean isFaceUp;
	private String number;
	private int x;
	private int y;
	
	// outline for cards
	private final BasicStroke cardOutline = new BasicStroke(4.5f);
	
	public Card(Suit suitz, int valuez){
		suit = suitz;
		if (suit == Suit.DIAMOND || suit == Suit.HEART) {
			color = Color.RED;
		} else {
			color = Color.BLACK;
		}
		
		//must be a number 1-13
		if (valuez < 14) {
			value = valuez;
			if (value < 11 && value > 0) {
				number = Integer.toString(valuez);
			} else if (value == 11) {
				number = "J";
			} else if (value == 12) {
				number = "Q";
			} else { number = "K";}
		} else {
			value = -1;
		}

		isFaceUp = false;
		x = 0;
		y = 0;
	}
	
	public Suit getSuit(){
		return this.suit;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	// card equality determined by suit/color/value equality
	public boolean equals(Card tgt) {
		return suit == tgt.suit && value == tgt.value 
				&& color.equals(tgt.color);
	}
	
	public void setCoordinates(int newX, int newY){
		x = newX;
		y = newY;
		return;
	}
		
	public void flip(){
		isFaceUp = !isFaceUp;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean isFaceUp(){
		return isFaceUp;
	}
	
	//used to find the card clicked in a fanned deck with overlapping cards
	public static Card highestCardOfClick(FannedDeck fd, int xClick, int yClick){
		if (fd.isEmpty()) {
			return null;
		}
		Card fCards[] = fd.getDeck();
		for (int i = fCards.length - 1; i >= 0; i--) {
			if (fCards[i].includesClick(xClick, yClick)){
				return fCards[i];
			}
		}
		return null;
	}
	
	//to determine if a click happened in a specific card
	public boolean includesClick(int xClick, int yClick){
		return (x <= xClick && xClick <= x + GameBoard.CARD_WIDTH 
					&& y <= yClick && yClick <= y + GameBoard.CARD_HEIGHT);
	}
	
	//drawing determined by suit
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(cardOutline);
		if (isFaceUp) {
			g2.setColor(Color.WHITE);
			g2.fillRect(x, y, GameBoard.CARD_WIDTH, GameBoard.CARD_HEIGHT);	
			
			g2.setColor(Color.BLACK);
			g2.drawRect(x, y, GameBoard.CARD_WIDTH, GameBoard.CARD_HEIGHT);
			
			g2.setColor(color);
			g2.setFont(g2.getFont().deriveFont(40.0f));
			g2.drawString(number, x + GameBoard.CARD_WIDTH - 50, y + 40);
			g2.drawString(number, x + 20, y + GameBoard.CARD_HEIGHT - 20);
			
			switch (suit) {
			case CLUB: 
				g2.fillOval(x + 20, y + 20, 20, 20);
				g2.fillOval(x + GameBoard.CARD_WIDTH - 40, y + GameBoard.CARD_HEIGHT - 40, 20, 20);
				break;
			case SPADE: 
				g2.fillRect(x + 20, y + 20, 20, 20);
				g2.fillRect(x + GameBoard.CARD_WIDTH - 40, y + GameBoard.CARD_HEIGHT - 40, 20, 20);
				break;
			case HEART: 
				int[] xpoints = {x + 25, x + 35, x + 45};
				int[] ypoints = {y + 45, y + 15, y + 45};
				int[] xPointsBot = {x + GameBoard.CARD_WIDTH - 45, x + GameBoard.CARD_WIDTH - 35, 
						x + GameBoard.CARD_WIDTH - 25};
				int[] yPointsBot = {y + GameBoard.CARD_HEIGHT - 20, y + GameBoard.CARD_HEIGHT - 50, 
						y + GameBoard.CARD_HEIGHT - 20};
				g2.fillPolygon(xpoints, ypoints, 3);
				g2.fillPolygon(xPointsBot, yPointsBot, 3);
				break;
			case DIAMOND: 
				int[] xpoints1 = {x + 25, x + 35, x + 45};
				int[] ypoints1 = {y + 45, y + 15, y + 45};
				int[] xpoints2 = {x + 25, x + 35, x + 45};
				int[] ypoints2 = {y + 25, y + 55, y + 25};
				g2.fillPolygon(xpoints1, ypoints1, 3);
				g2.fillPolygon(xpoints2, ypoints2, 3);
				
				int[] xpointsBot1 = {x + GameBoard.CARD_WIDTH - 45, x + GameBoard.CARD_WIDTH - 35, 
						x + GameBoard.CARD_WIDTH - 25};
				int[] ypointsBot1 = {y + GameBoard.CARD_HEIGHT - 20, y + GameBoard.CARD_HEIGHT - 50, 
						y + GameBoard.CARD_HEIGHT - 20};
				int[] xpointsBot2 = {x + GameBoard.CARD_WIDTH - 45, x + GameBoard.CARD_WIDTH - 35, 
						x + GameBoard.CARD_WIDTH - 25};
				int[] ypointsBot2 = {y + GameBoard.CARD_HEIGHT - 40, y + GameBoard.CARD_HEIGHT - 10, 
						y + GameBoard.CARD_HEIGHT - 40};
				g2.fillPolygon(xpointsBot1, ypointsBot1, 3);
				g2.fillPolygon(xpointsBot2, ypointsBot2, 3);
				break;
			}
		} else {
			g2.setColor(new Color(168, 86, 191));
			g2.fillRect(x, y, GameBoard.CARD_WIDTH, GameBoard.CARD_HEIGHT);	
			
			g2.setColor(Color.BLACK);
			g2.drawRect(x, y, GameBoard.CARD_WIDTH, GameBoard.CARD_HEIGHT);
		}
		
	}
}


