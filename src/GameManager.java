import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * A game manager controls the game logic, takes interaction from game windows and update game board accordingly 
 * @author yklam2
 *
 */
public class GameManager implements ImageChangeListener {
	
	private GameBoard gameBoard;
	private ArrayList<Player> players;
	private ArrayList<Player> playerQueue;
	private Player currentPlayer;
	
	/**
	 * Create a game manager that uses a specific game board
	 * @param gameBoard
	 */
	public GameManager(Deck deck) {
		gameBoard = new GameBoard(3,6, deck);
		gameBoard.setImageChangeListener(this);
		players = new ArrayList<Player>();
		playerQueue = new ArrayList<Player>();

		// TODO: Task 7
		startServer();
	}
	
	/**
	 * Start a game server
	 */
	public void startServer() {
		try {
			new GameServer().start();
		} catch (IOException e) {
			System.err.println("Error starting server");
		}
	}
	
	/**
	 * Add a game view to the game
	 * @param gameView The game view to be added
	 */
	public synchronized void addGameView(GameView gameView) {
		Player player = new Player(gameView);
		gameView.setGameActionListener(new GameActionHandler(player));
		playerQueue.add(player);
	}
	
	/**
	 * Close a game window. End the current turn if the game window is the current one
	 * Terminate the program if all windows are closed
	 * @param gameWindow The gameWindow to be closed
	 */
	public synchronized void closeGameView(Player player) {
		
		broadcast("Player "+player.getPlayerNumber()+" left.");
		
		player.close();
		
		if(currentPlayer == player) {
			playerQueue.remove(player);
			currentPlayer = null;
			endTurn();
		} else {
			playerQueue.remove(player);
		}

		if(playerQueue.size() == 0 && currentPlayer == null) {
			System.exit(0);
		}
	}

	/**
	 * Broadcast a message to all windows
	 * @param message Message to broadcast
	 */
	public synchronized void broadcast(String message) {
		// TODO: Task 4.1
		for(Player p: players)
		{
			if(p.gameView!=null)
			{
				p.gameView.addMessage(message);
			}
			
		}
		
	}
	/**
	 * Inform all window that the game board is changed
	 */
	public synchronized void imageChanged(int row, int col, BufferedImage image) {
		// TODO: Task 4.2
		for (Player player :players)
		{
			if(player.gameView!=null)
			{
				player.gameView.setCardImage(row, col, image);
			}
				
						
		}
		
	}	
		
	/**
	 * Start a game
	 */
	public synchronized void startGame() {
		if(playerQueue.size() > 0) {
			broadcast("Game started!");
			currentPlayer = null;
			nextTurn();
		}
	}
	
	/**
	 * Start next turn by:
	 *   - clearing the board, remove/un-flip cards on board
	 *   - pick the next player
	 *   - start a new turn
	 */
	public void nextTurn() {
		prepareNextTurn();
		if(gameOver()) { 
			broadcast("The game is over");
			players.sort(new Comparator<Player>() {
				public int compare(Player p1, Player p2) {
					return p2.getScore() - p1.getScore();
				}
			});
			StringBuffer result = new StringBuffer();
			result.append("=== Result ==="+System.lineSeparator());
			for(Player player: players) {
				result.append("Player "+player.getPlayerNumber()+": "+player.score+System.lineSeparator());
			}
			broadcast(result.toString());
		} else {
			pickNextPlayer();
			startTurn();
		}
	}
	
	/**
	 * Prepare for the next turn, it does two things:
	 *   - if there are two cards facing up, remove them from game board if they have the same value
	 *   - make all cards facing down
	 */
	public synchronized void prepareNextTurn() {
		// TODO: Task 4.1
		int numflip=gameBoard.numflip();
		if(numflip==2)
		{
			int[][] flipcood=new int[2][2];
			flipcood=gameBoard.flipcood();
			if(gameBoard.getValueAt(flipcood[0][0], flipcood[0][1])==gameBoard.getValueAt(flipcood[1][0], flipcood[1][1]))
			{
				gameBoard.removeCardAt(flipcood[0][0], flipcood[0][1]);
				gameBoard.removeCardAt(flipcood[1][0], flipcood[1][1]);
				currentPlayer.incScore();
			}
			else
			{
				
				gameBoard.resetCardAt(flipcood[0][0], flipcood[0][1]);
				gameBoard.resetCardAt(flipcood[1][0], flipcood[1][1]);
			}
		}
	}
		
		
		
		
	

	/**
	 * Check if the game is over
	 * @return true if the game is over, false otherwise
	 */
	private boolean gameOver() {
		// TODO: Task 4.1
		if (gameBoard.empty())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	/**
	 * Pick the next available player
	 */
	public synchronized void pickNextPlayer() {
		if(currentPlayer != null) {
			playerQueue.add(currentPlayer);
		}
		currentPlayer = playerQueue.remove(0);
	}
	
	/**
	 * Start the turn by activating a window
	 */
	public void startTurn() {
		if(currentPlayer != null) {
			broadcast("Player "+currentPlayer.getPlayerNumber()+"'s turn!");
			currentPlayer.getGameView().activate();
		}
	}
	
	/**
	 * End the turn by deactivating a window, and schedule for the next turn
	 */
	public void endTurn() {
		if(currentPlayer != null) {
			currentPlayer.getGameView().deactivate();
		}
		scheduleNextTurn();
	}
	/**
	 * Start next turn after 0.5s
	 */
	public void scheduleNextTurn() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				nextTurn();
			}
		}.start();
	}
	int n=1;
	
	/**
	 * Try to flip a card at the specific location. If two cards are flipped, end the turn.
	 * It should inform all windows that the game board is changed.
	 * @param row Row of card
	 * @param col Column of card
	 */
	public void flipCardAt(int row, int col) {
		// TODO: Task 4.1
		int numflip=0;
		numflip=gameBoard.numflip();
		if (numflip<2)
		{
			gameBoard.flipCardAt(row, col);
			if(gameBoard.getCard(row, col)!=null)
				{
					imageChanged(row, col, gameBoard.getCard(row, col).getFrontImage());
				}
				
				
			if (n==2)
			{
				n=1;
				endTurn();
				
			}
			else
			{
				n++;
			}
			
		}
	}

	private class Player {
		private GameView gameView;
		private int score;
		private int playerNumber;
		
		public Player(GameView gameView) {
			this.gameView = gameView;
			this.score = 0;
			players.add(this);
			playerNumber = players.size();
		}
		public int getPlayerNumber() {
			return playerNumber;
		}
		public GameView getGameView() {
			return gameView;
		}
		public void close() {
			gameView = null;
		}
		public void incScore() {
			score++;
			broadcast("Player "+playerNumber+" score 1 point, total score is "+score);
		}
		public int getScore() {
			return score;
		}
	}
	private class GameActionHandler implements GameActionListener {
		private Player player;
		public GameActionHandler(Player player) {
			this.player = player;
		}
		public void cardClicked(int row, int col) {
			// TODO: Task 5.1
			if(player.gameView!=null)
			{
				if(player.gameView.isActive())
				{
					
					flipCardAt(row, col);
				}
			}
			
			
		}
		public void windowClosed() {
			// TODO: Task 5.1
			if (player.gameView!=null)
			{
				closeGameView(player);
			}
			
		}
	}
	
	private class GameServer extends Thread {
		private ServerSocket serverSocket;
		public GameServer() throws IOException {
			serverSocket = new ServerSocket(20080);
		}
		public void run() {
			try {
				while(true) {
					Socket socket;
					socket = serverSocket.accept();
					addGameView(new RemoteGameWindow(socket));
				}
			} catch (IOException e) {
				System.err.println("Error establishing new connection");
			}
		}
	}
	/**
	 * The test case
	 * @param args Not used
	 */
	public static void main(String [] args) {
		Deck deck = new Deck();
		int v=0;
		for(int i=0; i<3; ++i) {
			v += 1+(int)(Math.random()*2);
			deck.addCard(new Card(Card.SPADE, v));
			deck.addCard(new Card(Card.HEART, v));
			deck.addCard(new Card(Card.CLUB, v));
			deck.addCard(new Card(Card.DIAMOND, v));
		}
		for(int i=0; i<3; ++i) {
			v += 1+(int)(Math.random()*2);
			deck.addCard(new Card(Card.SPADE, v));
			deck.addCard(new Card(Card.HEART, v));
		}
		
		GameManager gameManager = new GameManager(deck);	
		GameWindow [] gameWindows = { new GameWindow(3,6), new GameWindow(3,6) };
				
		for(GameWindow gameWindow: gameWindows) {
			gameManager.addGameView(gameWindow);
			gameWindow.show();
		}

		gameManager.startGame();
		
		for(int i=0; i<24; ++i) {
			int row = (int)(Math.random()*3);
			int col = (int)(Math.random()*6);
			gameManager.flipCardAt(row,col);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		System.exit(0);
	}
	
}
