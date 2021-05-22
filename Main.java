package game;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		// create JFrame object
		JFrame frame = new JFrame();
		
		// create Game object
		Game game = new Game();
		
		// set bounds on game window
		frame.setBounds(10, 10, 800, 600);
		
		// set title of game
		frame.setTitle("Game: Breakout Ball");
		
		// cannot resize the screen
		frame.setResizable(false);
		
		// make the window visible to the user
		frame.setVisible(true);
		
		// turn off the program when user closes the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add game to frame
		frame.add(game);
		
	}

}
