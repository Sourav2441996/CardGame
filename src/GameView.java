import java.awt.image.BufferedImage;


/**
 * Represents a game view for game manger to use
 */
public interface GameView {
	/**
	 * Set the GameActionListener to use
	 * @param gameActionListener The GameActionListener object
	 */
	public void setGameActionListener(GameActionListener gameActionListener);
	
	public void addMessage(String message);
	public void setCardImage(int row, int col, BufferedImage image);
	
	/**
	 * Activate window so that clicks on the cards are handled
	 */
	public void activate();
	
	public boolean isActive();
	/**
	 * Deactivate window so that clicks on the cards will not be handled
	 */
	public void deactivate();
}
