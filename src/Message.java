import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * A generic message object used for game communications
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Message for the cardClicked method in RemoteGameActionHandler
	 */
	public static final int CARD_CLICKED = 101;
	/**
	 * Message for the windowClosed method in RemoteGameActionHandler
	 */
	public static final int WINDOW_CLOSED = 102;
	
	/**
	 * Message for the activate method in RemoteGameWindow
	 */
	public static final int ACTIVATE = 201;
	/**
	 * Message for the deactivate method in RemoteGameWindow
	 */
	public static final int DEACTIVATE = 202;
	/**
	 * Message for the addMessage method in RemoteGameWindow
	 */
	public static final int ADD_MESSAGE = 203;
	/**
	 * Message for the setCardImage method in RemoteGameWindow
	 */
	public static final int SET_CARD_IMAGE = 204;
	
	/**
	 * Type of message
	 */
	public int type;
	/**
	 * Row number
	 */
	public int row;
	/**
	 * Column number
	 */
	public int col;
	/**
	 * Text message
	 */
	public String message;
	
	private int width;
	private int height;
	private int [] pixels;
	/**
	 * Get the image in this message
	 * @return The image
	 */
	public BufferedImage getImage() {
		if(pixels != null) {
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			image.setRGB(0, 0, width, height, pixels, 0, width);
			return image;
		}
		return null;
	}

	/**
	 * Create a simple message
	 * @param type Type of message
	 */
	public Message(int type) {
		this.type = type;
	}
	/**
	 * Create a simple message with text message
	 * @param type Type of message
	 * @param message Text message
	 */
	public Message(int type, String message) {
		this.type = type;
		this.message = message;
	}
	/**
	 * Create a message that specifies a row and column
	 * @param type Type of message
	 * @param row Row number
	 * @param col Column number
	 */
	public Message(int type, int row, int col) {
		this.type = type;
		this.row = row;
		this.col = col;
	}
	/**
	 * Create a message that specifies an image at a specific row and column
	 * @param type Type of message
	 * @param row Row number
	 * @param col Column number
	 * @param image The image, can be null to indicate no-image
	 */
	public Message(int type, int row, int col, BufferedImage image) {
		this.type = type;
		this.row = row;
		this.col = col;
		if(image != null) {
			width = image.getWidth();
			height = image.getHeight();
			pixels = image.getRGB(0, 0, width, height, null, 0, width);
		}
	}
   
}
