import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a deck of card
 */
public class Deck {
	private ArrayList<Card> deck;
	/**
	 * Create an empty deck of card
	 */
	public Deck() {
		deck = new ArrayList<Card>();
	}
	
	/**
	 * Get size of deck
	 * @return the size of deck
	 */
	public int size() {
		return deck.size();
	}
	
	/**
	 * Add a card to bottom of deck
	 * @param card The card to add
	 */
	public void addCard(Card card) {
		deck.add(card);
	}
	/**
	 * Shuffle the deck
	 */
	public void shuffle() {
		// TODO: Task 1
		Random r=new Random();
		int sh=r.nextInt(deck.size());
		for (int i=0;i<deck.size();i++)
		{
			Card a=deck.get(i);
			deck.set(i, deck.get(sh));
			deck.set(sh, a);
			sh=r.nextInt(deck.size());
			
		}
	}
	
	/**
	 * Remove and return the top of the deck
	 * @return a card on the top of deck
	 */
	public Card draw() {
		Card top = deck.get(0);
		deck.remove(0);
		return top;
	}
	/**
	 * Remove and return the first card in the deck with the specific value
	 * @param value The card value
	 * @return a card with the specific value, or null if not exists
	 */
	public Card drawByValue(int value) {
		// TODO: Task 1
		for (int i=0;i<deck.size();i++)
		{
			if (deck.get(i).getValue()==value)
			{
				Card a=deck.get(i);
				deck.remove(i);
				return a;
			}
		}
		return null;
	}
	
	public boolean DeckContains(Card a)
	{
		for (int i=0;i<deck.size();i++)
		{
			if (deck.get(i).getValue()==a.getValue() && deck.get(i).getSuit()==a.getSuit())
			{
				return true;
			}
			
		}
		return false;
	}

	/**
	 * The test case
	 * @param args Not used
	 */
	public static void main(String [] args) {
		System.out.println("Testing Deck class");
		Deck deck = new Deck();
		deck.addCard(new Card("1S"));
		deck.addCard(new Card("1H"));
		deck.addCard(new Card("1C"));
		deck.addCard(new Card("1D"));
		deck.addCard(new Card(Card.SPADE, 2));
		deck.addCard(new Card(Card.HEART, 2));
		deck.addCard(new Card(Card.CLUB, 2));
		deck.addCard(new Card(Card.DIAMOND, 2));
		deck.shuffle();
		
		while(deck.size()>0) {
			Card top = deck.draw();
			Card pair = deck.drawByValue(top.getValue());
			System.out.println("Drawn cards: "+top+", "+pair);
		}
	}
}
