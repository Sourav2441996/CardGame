import java.util.Random;

/**
 * Represents a game board with an array of cards
 */
public class GameBoard {

	private int rows;
	private int columns;
	private Card[][] gameBoard;
	private ImageChangeListener imageChangeListener;
	
	/**
	 * Create a game board of specific size
	 * @param rows Number of rows 
	 * @param columns Number of columns
	 * @param deck Deck of cards to be used
	 */
	public GameBoard(int rows, int columns, Deck deck) {
		this.rows = rows;
		this.columns = columns;
		initializeGameBoard(deck);
		//setImageChangeListener=(new ImageChangeListener());
	}
	/**
	 * Set the image change listener of this board
	 * @param imageChangeListener The change listener to be notified when an image change
	 */
	public void setImageChangeListener(ImageChangeListener imageChangeListener) {
		this.imageChangeListener = imageChangeListener;
	}
	/**
	 * Fill the board randomly with the deck of card
	 * The deck of card will be shuffled and cards will be drawn from it
	 * All cards will be facing down
	 * @param deck Deck of card to use
	 */
	public void initializeGameBoard(Deck deck) {
		// TODO: Task 2
		gameBoard=new Card[rows][columns];
		deck.shuffle();
		
		for (int i=0;i<rows;i++)
		{
			for (int j=0;j<columns;j++)
			{
				
				gameBoard[i][j]=deck.draw();
			}
		}
	
		
		
		
		
	}
	
	/**
	 * Get number of rows
	 * @return Number of rows
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * Get number of columns
	 * @return Number of columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Get the card value at the specific location. Return -1 if there is no card there
	 * @param row Row of card
	 * @param col Column of card
	 * @return Value of the card, -1 if there no card there
	 */
	public int getValueAt(int row, int col) {
		// TODO: Task 2
		if (gameBoard[row][col]!=null)
		{
			return gameBoard[row][col].getValue();
		}
		else
		{
			return -1;
		}
		
	}

	public Card getCard(int row, int col)
	{
		if(gameBoard[row][col]!=null)
		{
			return gameBoard[row][col];
			
		}
		else
		{
			return null;
		}
	}
	public int numflip()
	{
		int num=0;
		for (int i=0;i<rows;i++)
		{
			for (int j=0;j<columns;j++)
			{
				if (gameBoard[i][j]!=null)
				{
					if(gameBoard[i][j].isFacingUp())
					{
						num++;
					}
				}
				
			}
		}
		return num;
	}
	
	public int[][] flipcood()
	{
		int[][] coordinates=new int[2][2];
		int k=0;int l=0;
		for (int i=0;i<rows;i++)
		{
			for (int j=0;j<columns;j++)
			{
				if(gameBoard[i][j]!=null)
				{
					if(gameBoard[i][j].isFacingUp())
					{
						
						k=0;
						coordinates[l][k]=i;
						k++;
						coordinates[l][k]=j;
						l++;
						
					}
				}
				
			}
		}
		return coordinates;
		
	}
	
	public void resetAll()
	{
		for (int i=0;i<rows;i++)
		{
			for (int j=0;j<columns;j++)
			{
				if(gameBoard[i][j]!=null)
				{
					resetCardAt(i,j);
				}
			}
		}
	}

	/**
	 * Flip card at the specific location 
	 * Flip if there is a card there and the card is facing down
	 * @param row Row of card
	 * @param col Column of card
	 * @return true if the card is flipped, otherwise false
	 */
	public boolean flipCardAt(int row, int col) {
		// TODO: Task 2
		Card a=this.gameBoard[row][col];
		if(a!=null)
		
		{
			if(a.isFacingUp()==false )
			{
				a.flip();
				if(imageChangeListener!=null)
				{
					imageChangeListener.imageChanged(row, col, a.getFrontImage());
				}
				
				return true;
			
			}
			else
			{
				return false;
			}
		}
		return true;
		
		
	}
	
	/**
	 * Reset the card at the specific location so that it is facing down
	 * @param row Row of card
	 * @param col Column of card
	 */
	public void resetCardAt(int row, int col) {
		// TODO: Task 2
		Card a=gameBoard[row][col];
		if (a!=null)
		{
			if (a.isFacingUp())
			{
				a.flip();
				if (imageChangeListener!=null)
				{
					imageChangeListener.imageChanged(row, col, Card.getBackImage());
				}
				
			}
		}
			
			
		
	}
	
	/**
	 * Remove a card at a specific location
	 * @param row Row number
	 * @param col Column number
	 */
	public void removeCardAt(int row, int col) {
		// TODO: Task 2
		
			this.gameBoard[row][col]=null;
			if (imageChangeListener!=null)
			{
				imageChangeListener.imageChanged(row, col, null);
			}
			
		
	}
	
	public boolean empty() {
		for(int row=0;row<rows;++row) {
			for(int col=0;col<columns;++col) {
				if(gameBoard[row][col] != null)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * The test case
	 * @param args Not used
	 */
	public static void main(String [] args) {
		System.out.println("Testing GameBoard class");
		
		Deck deck = new Deck();
		String [] suits = Card.getSuits();
		int [] values = Card.getValues();
		
		for(int i=0; i<8; ++i) {
			Card card = new Card(suits[(int)(Math.random()*suits.length)], values[(int)(Math.random()*values.length)]);
			deck.addCard(card);
		}
		
		GameBoard gameBoard = new GameBoard(2,4,deck);

		for(int i=0; i<10; ++i) {
			int row = (int)(Math.random()*2);
			int col = (int)(Math.random()*4);
			if(Math.random() < 0.1)
				gameBoard.removeCardAt(row, col);
			else if(Math.random() < 0.5 )
				gameBoard.flipCardAt(row, col);
			else 
				gameBoard.resetCardAt(row, col);
			
		
		}
		
		System.out.println("Random game board status:");
		gameBoard.printBoard();

	}
	
	/**
	 * Print board to console for debugging purpose
	 */
	public void printBoard() {
		for(int row=0;row<getRows();++row) {
			for(int col=0;col<getColumns();++col) {
				Card card = gameBoard[row][col];
				if(card == null) {
					System.out.print(" * \t");
				} else if(card.isFacingUp()) {
					System.out.print("+"+card+"\t");
				} else {
					System.out.print("-"+card+"\t");
				}
			}
			System.out.println();
		}
	}
	
	
}
