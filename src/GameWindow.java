import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.text.*;


/**
 * Represents a game window
 */
public class GameWindow extends WindowAdapter implements GameView {
		
	private JFrame frame;
	private JTextArea console;
	private PicPane picpane;
	private int rows;
	private int columns;
	private BufferedImage [][] cardImages;
	
	/**
	 * Create a game window with an array of card images
	 * @param rows Number of rows of cards
	 * @param columns Number of columns of cards
	 */
	public GameWindow(int rows, int columns) {
		
		frame = new JFrame("Poker Memory");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(this);		
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		this.rows = rows;
		this.columns = columns;
		cardImages = new BufferedImage[rows][columns];
		for (int i=0;i<rows;i++)
		{
			for (int j=0;j<columns;j++)
			{
				cardImages[i][j]=Card.getBackImage();
			}
		}
		picpane=new PicPane();
		console=new JTextArea();
		console.setRows(8);
		console.setLineWrap(true);
		console.setEditable(false);
		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(console);
		frame.add(picpane);
		frame.add(scrollPane);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	private GameActionListener gameActionListener;
	public void setGameActionListener(GameActionListener gameActionListener) {
		this.gameActionListener = gameActionListener;
	}
	/**
	 * Get the GameActionListener
	 * @return The GameActionListener object in use
	 */
	public GameActionListener getGameActionListener() {
		return gameActionListener;
	}	
	
	
	/**
	 * Add a message to the text area in the window
	 * @param message The message to be added
	 */
	public void addMessage(String message) {
		// TODO: Task 3
		console.append(message+"\n");
	}
	


	/**
	 * Set the card image at the specific location
	 * @param row Row number
	 * @param col Column number
	 * @param image The image
	 */
	public void setCardImage(int row, int col, BufferedImage image) {
		// TODO: Task 3
		cardImages[row][col]=image;
		picpane.repaint();
	}
	
	/**
	 * Set this window visible
	 */
	public void show() {
		frame.setVisible(true);
	}
	
	/**
	 * Activate window so that clicks on the cards are handled
	 */
	public void hide() {
		frame.setVisible(false);
	}
	
	/**
	 * Attempt to close this window
	 */
	public void close() {
		if(gameActionListener != null) {
			gameActionListener.windowClosed();
		}
		
	}
	
	private boolean active;

	public void activate() {
		active = true;
		addMessage("Your turn now!");
	}
	
	public boolean isActive()
	{
		return active;
	}

	public void deactivate() {
		active = false;
	}

	/* Handling window close event, delegate it to hide()
	 * (non-Javadoc)
	 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent event) {
		this.close();
	}
	

	/**
	 * The test case
	 * @param args Not used
	 */
	public static void main(String [] args) {
		System.out.println("Testing GameWindow class");
		
		GameWindow gameWindow = new GameWindow(3,6);
		gameWindow.show();
		
		for(int i=1; i<=10; ++i) {
			int row = (int)(Math.random()*3);
			int col = (int)(Math.random()*6);
			int suit = (int)(Math.random()*Card.getSuits().length);
			int value = (int)(Math.random()*Card.getValues().length);
			gameWindow.setCardImage(row, col, new Card(Card.getSuits()[suit], Card.getValues()[value]).getFrontImage());
			gameWindow.addMessage("This is test message "+i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		System.exit(0);
	}
	
	
	private class PicPane extends JPanel implements MouseListener
	{
		public PicPane() {
			setPreferredSize(new Dimension(columns * Card.getBackImage().getWidth(), rows *Card.getBackImage().getHeight()));
					
			setBackground(Color.WHITE);
			addMouseListener(this);
		}

	
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < columns; col++) {
					if (cardImages[row][col] != null) {
						BufferedImage im = cardImages[row][col];
						g.drawImage(im, col * im.getWidth(), row* im.getHeight(), null);
						im.flush();
					}
				}
			}
		}

		public void mouseClicked(MouseEvent event) {
			int x = event.getX();
			int y = event.getY();
			int col = x / Card.getBackImage().getWidth();
			int row = y / Card.getBackImage().getHeight();
			BufferedImage im=cardImages[row][col];
			if (im != null) {
				gameActionListener.cardClicked(row, col);
			}

		}

		public void mouseEntered(MouseEvent event) {
		}

		public void mouseExited(MouseEvent event) {
		}

		public void mousePressed(MouseEvent event) {
		}

		public void mouseReleased(MouseEvent event) {
		}
	}
}
