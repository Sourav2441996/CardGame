
/**
 * This interface is used when an object can be used to handle game actions
  */
public interface GameActionListener {
	/**
	 * A card at the specific location is clicked
	 * @param row Row of card
	 * @param col Column of card
	 */
	public void cardClicked(int row, int col);
	/**
	 * Game is closed
	 */
	public void windowClosed();
}
