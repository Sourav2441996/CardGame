
/**
 * Tester to test a single-game-window game
 */
public class GameTester {
	/**
	 * The test case
	 * @param args Not used
	 */
	public static void main(String [] args) {
		Deck deck = new Deck();
		for(int i=1; i<=9; ++i) {
			deck.addCard(new Card(Card.SPADE, i));
			deck.addCard(new Card(Card.HEART, i));
		}
		
		GameManager gameManager = new GameManager(deck);
		GameWindow gameWindow = new GameWindow(3,6);
		gameManager.addGameView(gameWindow);
		gameWindow.show();
		
		gameManager.startGame();
		
	}
	
}
