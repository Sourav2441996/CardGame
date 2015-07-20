import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A special GameActionListener that redirect actions to the server on the other side of the connection,
 * It also receives message from the server and ask the GameWindow object to handle the actions accordingly
 */
public class RemoteGameActionHandler implements GameActionListener, Runnable {

	private GameWindow gameWindow;
	private Socket socket;
	/**
	 * Create a new RemoteGameActionHandler that connects to the specific host and port, 
	 * received actions will be redirected to the specific GameWindow object
	 * @param host Host to connect to
	 * @param port Port number to use
	 * @param gameWindow GameWindow object that actions should be redirect to
	 * @throws IOException When connection fails
	 */
	public RemoteGameActionHandler(String host, int port, GameWindow gameWindow) throws IOException {
		this.gameWindow = gameWindow;
		socket = new Socket(host, port);
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
	public void run() {
		try {
			while(true) {
				Message message = (Message)in.readObject();
				switch(message.type) {
				case Message.ACTIVATE:
					gameWindow.activate();
					break;
				case Message.DEACTIVATE:
					// TODO: Task 8
					gameWindow.deactivate();
					break;
				case Message.ADD_MESSAGE:
					// TODO: Task 8
					gameWindow.addMessage(message.message);
					break;
				case Message.SET_CARD_IMAGE:
					// TODO: Task 8
					gameWindow.setCardImage(message.row, message.col, message.getImage());
					break;
				}
			}
		} catch(IOException | ClassNotFoundException e) {
			System.out.println("Connection closed");
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

	public void cardClicked(int row, int col) {
		// TODO: Task 8
		Message message=new Message(Message.CARD_CLICKED,row,col);
		sendMessage(message);
	}
	public void windowClosed() {
		Message message = new Message(Message.WINDOW_CLOSED);
		sendMessage(message);
		// Window closed, game ends
		System.exit(0);
	}
	
}
