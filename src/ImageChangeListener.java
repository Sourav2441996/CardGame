import java.awt.image.BufferedImage;

/**
 * This interface is used when an object must be notified about a image changed at a particular row and column
 */
public interface ImageChangeListener {
	/**
	 * Called when it is necessary to update an image at a specific location
	 * @param row Row number
	 * @param col Column number
	 * @param image The image, can be null
	 */
	public void imageChanged(int row, int col, BufferedImage image);
}
