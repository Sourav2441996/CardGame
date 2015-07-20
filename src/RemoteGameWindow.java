import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A special GameWindow that redirect actions to the clients on the other side of the connection,
 * It also receives message from the client and asks the GameActionListener to handle the actions accordingly
 */
public class RemoteGameWindow implements GameView, Runnable {

	private Socket socket;
	/**
	 * Create a new RemoteGameWindow that handle the connection from the specific socket.
	 * @param socket Socket of the incoming connection
	 * @throws IOException When connection fails
	 */
	public RemoteGameWindow(Socket socket) throws IOException {
		this.socket = socket;
		setupConnection();
		System.out.println("Connection established");
	}
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private void setupConnection() throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(socket.getInputStream());
		new Thread(this).start();
	}
	
	private GameActionListener gameActionListener;
	public void setGameActionListener(GameActionListener gameActionListener) {
		this.gameActionListener = gameActionListener;
	}
	
	public void run() {
		try {
			while(true) {
				Message message = (Message)in.readObject();
				switch(message.type) {
				case Message.CARD_CLICKED:
					// TODO: Task 8
					if(gameActionListener!=null && active==true)
					{
						gameActionListener.cardClicked(message.row, message.col);
					}
					
					break;
				case Message.WINDOW_CLOSED:
					gameActionListener.windowClosed();
					break;
				}
			}
		} catch(IOException | ClassNotFoundException e) {
			System.out.println("Connection closed");
			gameActionListener.windowClosed();
		}
	}
	
	private void sendMessage(Message message) {
		try {
			out.writeObject(message);
			out.flush();
			out.reset();
		} catch (IOException e) {
			System.err.println("Failed to send message");
		}
	}
	private boolean active=false;
	public void activate() {
		Message message = new Message(Message.ACTIVATE);
		active=true;
		sendMessage(message);
	}
	public void deactivate() {
		// TODO: Task 8
		Message message=new Message(Message.DEACTIVATE);
		active=false;
		sendMessage (message);
	}
	public void addMessage(String message) {
		// TODO: Task 8
		Message m=new Message(Message.ADD_MESSAGE, message);
		sendMessage(m);	
	}
	
	public boolean isActive()
	{
		return active;
		
	}
	public void setCardImage(int row, int col, BufferedImage image) {
		// TODO: Task 8
		Message message=new Message(Message.SET_CARD_IMAGE,row,col, image );
		sendMessage(message);
	}


}
