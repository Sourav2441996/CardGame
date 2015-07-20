import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.IOException;
import java.util.Random;

/**
 * The poker memory game main program
 */
public class PokerMemoryGame {
	JFrame window=new JFrame();
	/**
	 * The main program
	 * @param args Not used
	 */
	public static void main(String[] args) {
		new PokerMemoryGame().showStartWindow();
	}
	
	/**
	 * Show starting window
	 */
	public void showStartWindow() {
		// TODO: Task 6.1
		
		window.setResizable(false);
		
		JButton host=new JButton("Host a game");
		JButton join=new JButton("Join a game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(250,70);
		host.setSize(50,25);
		host.addMouseListener(new hostgameActionListener());
		join.addMouseListener(new joingameListener());
		join.setSize(50,25);
		JPanel panel=new JPanel();
		panel.add(host);
		panel.add(join);
		window.add(panel);
		window.setVisible(true);
	}
	
	/**
	 * Generate and return a deck consists of 18 different cards. 
	 * The deck must form 9 pairs of cards of the same value.
	 * @return the deck of cards generated
	 */
	private Deck generateDeck() {
		// TODO: Task 6.1
		Deck deck=new Deck();
		Random r=new Random();
		int value, npair;
		Card toadd;
		
		int[] val; String[] suit;
		suit=Card.getSuits();
		val=Card.getValues();
		while(deck.size()<18)
		{	
			npair=0;
			value=val[r.nextInt(13)];
			while(npair<2)
			{
				toadd=new Card(suit[r.nextInt(4)],value);
				if (deck.DeckContains(toadd)==false)
				{
					deck.addCard(toadd);
					npair++;
				}
			}
			
		}
		return deck;
	}
	/**
	 * Host a local game, with one window locally
	 */
	public void hostGame() {
		System.out.println("Hosting a new game");
		
		Deck deck = generateDeck();
		GameManager gameManager = new GameManager(deck);
		GameWindow gameWindow = new GameWindow(3,6);
		gameManager.addGameView(gameWindow);
		gameWindow.show();
		
		gameManager.startGame();
	}

	
	/**
	 * Join a remote game at the specific host and port
	 * @param host Host address of the game
	 * @param port Port of the game
	 */
	public void joinGame(String host, int port) {
		System.out.println("Join Game at "+host+":"+port);
		
		GameWindow gameWindow = new GameWindow(3,6);
		try {
			gameWindow.setGameActionListener(new RemoteGameActionHandler(host, port, gameWindow));
			gameWindow.show();
		} catch (IOException e) {
			System.err.println("Error connecting to specified host and port.");
		}
	}
	private class hostgameActionListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			window.setVisible(false);
			hostGame();
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private class joingameListener implements MouseListener
	{
		JFrame joinwindow=new JFrame();
		JPanel panel=new JPanel();
		JLabel host=new JLabel("host");
		JLabel port =new JLabel("port");
		JButton join=new JButton("Join");
		JTextField hosttext=new JTextField("",10);
		JTextField porttext=new JTextField("",10);
		public joingameListener()
		{
			
			joinwindow.setSize(400,80);
			joinwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			host.setSize(50,50);
			hosttext.setSize(100,20);
			port.setSize(50,50);
			porttext.setSize(100,50);
			join.setSize(50,50);
			join.addMouseListener(this);
			panel.add(host);
			panel.add(hosttext);
			panel.add(port);
			panel.add(porttext);
			panel.add(join);
			joinwindow.add(panel);
			joinwindow.setResizable(false);
			joinwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			joinwindow.setVisible(false);
			
			
			
			
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(joinwindow.isVisible()==false)
			{
				window.setVisible(false);
				joinwindow.setVisible(true);
			}
			else
			{
				joinwindow.setVisible(false);
				joinGame(hosttext.getText(), Integer.parseInt(porttext.getText()));
			}
			
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
