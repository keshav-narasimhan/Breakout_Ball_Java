package game;

/*
 * import statements
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements KeyListener, ActionListener {
	
	// random number generator
	private Random rand = new Random();
	
	// initialize the game to be 'not over'
	private boolean game_over = false;
	
	// initialize the game to be 'not playing'
	private boolean isPlaying = false;
	
	// initialize variables that will denote the ball location + speed
	private int xBall = 300;
	private int yBall = 240;
	private int dxBall = rand.nextInt(4) + 2;
	private int dyBall = rand.nextInt(4) + 2;
	
	// initialize variable for paddle location
	private int xPaddle = 400;
	
	// initialize number of bricks
	private int num_bricks = 21;
	
	// create an array that will hold the x,y coordinates of the bricks
	int [][] bricks;
	
	private Timer timer;
	
	public Game() {
		// enable keypad inputs
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		// initialize bricks
		bricks = new int[num_bricks][2];
		int index = 0;
		int init_ypos = 50;
		for (int i = 0; i < 3; i++) {
			int init_xpos = 75;
			for (int j = 0; j < 7; j++) {
				bricks[index][0] = init_xpos;
				bricks[index][1] = init_ypos;
				index++;
				init_xpos += 90;
			}
			init_ypos += 55;
		}
		
		// initialize timer
		timer = new Timer(5, this);
		timer.start();
	}
	
	public void paint (Graphics g) {
		
		if (game_over) {
			isPlaying = false;
			game_over = true;
			g.setColor(Color.RED);
			g.setFont(new Font("Times New Roman", Font.BOLD, 40));
			g.drawString("GAME OVER!", 270, 310);
			g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
			g.drawString("Press Enter to Play Again", 300, 330);
		} else {
			// fill background of game window
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 600);
			
			// fill borders of game window
			g.setColor(Color.MAGENTA);
			g.fillRect(0, 0, 5, 600);
			g.fillRect(0, 0, 800, 5);
			g.fillRect(780, 0, 5, 600);
			g.fillRect(0, 558, 800, 5);
			
			// draw player paddle
			g.setColor(Color.CYAN);
			g.fillRect(this.xPaddle, 520, 100, 10);
			
			// draw ball
			g.setColor(Color.YELLOW);
			g.fillOval(xBall, yBall, 26, 26);
			
			// draw bricks
			g.setColor(Color.WHITE);
			for (int a = 0; a < num_bricks; a++) {
				if (bricks[a][0] > 0 && bricks[a][1] > 0) {
					g.fillRect(bricks[a][0], bricks[a][1], 85, 50);
				}
			}
		}
		
		g.dispose();
	}
	
	public void updateRight() {
		// have started playing
		isPlaying = true;
		
		// update paddle pos.
		this.xPaddle += 10;
	}
	
	public void updateLeft() {
		// have started playing
		isPlaying = true;
		
		// update paddle pos.
		this.xPaddle -= 10;
	}
	
	public void updateBall() {
		// if the ball is touching a wall, change its x mvmt
		if (xBall + 26 >= 775 || xBall <= 5) {
			dxBall = dxBall * -1;
		}
		
		// update ball's x pos.
		xBall += dxBall;
		
		// if the ball is touching a wall, change its y mvmt
		if (yBall + 26 >= 560) {
			game_over = true;
		}
		if (yBall <= 5) {
			dyBall = dyBall * -1;
		}
		
		// update ball's y pos.
		yBall += dyBall;
		
		// if all the bricks have been broken, game is over
		boolean check = true;
		for (int a = 0; a < num_bricks; a++) {
			if (bricks[a][0] != 0) {
				check = false;
				break;
			}
		}
		if (check) {
			game_over = true;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// start the timer
		timer.start();
		
A:		if (isPlaying) {
			// update the ball direction if hitting the paddle
			Rectangle ballRect = new Rectangle(xBall, yBall, 26, 26);
			if (ballRect.intersects(new Rectangle(this.xPaddle, 520, 100, 10))) {
				dyBall = dyBall * -1;
				yBall += dyBall;
				break A;
			}
			
			for (int a = 0; a < num_bricks; a++) {
				Rectangle brick = new Rectangle(bricks[a][0], bricks[a][1], 85, 50);
				if (ballRect.intersects(brick)) {
					// update bricks array so that hit bricks won't be drawn
					bricks[a][0] = 0;
					bricks[a][1] = 0;

					// check brick collisions with ball
					Point upper = new Point(ballRect.x + 13, ballRect.y);
					Point lower = new Point(ballRect.x + 13, ballRect.y + 26);
					Point left = new Point(ballRect.x, ballRect.y + 13);
					Point right = new Point(ballRect.x + 26, ballRect.y + 13);
					
					if (brick.contains(upper) || brick.contains(lower)) {
						dyBall = dyBall * -1;
					}
					if (brick.contains(left) || brick.contains(right)) {
						dxBall = dxBall * -1;
					}
					
					break;
				}
			}
			
			// update ball location
			updateBall();
		}
		
		// call the paint() function again to show update positions
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// get the code of the key pressed
		int key_code = e.getKeyCode();
		
		// move the paddle left if LEFT was pressed
		if (key_code == KeyEvent.VK_LEFT) {
			if (this.xPaddle <= 10) {
				this.xPaddle = 10;
			} else {
				updateLeft();
			}
		}
		
		// if the current game is over, user can restart game on ENTER
		if (game_over && key_code == KeyEvent.VK_ENTER) {
			game_over = false;
			isPlaying = true;
			xBall = 300;
			yBall = 240;
			dxBall = rand.nextInt(4) + 2;
			dyBall = rand.nextInt(4) + 2;
			xPaddle = 400;
			
			int index = 0;
			int init_ypos = 50;
			for (int i = 0; i < 3; i++) {
				int init_xpos = 75;
				for (int j = 0; j < 7; j++) {
					bricks[index][0] = init_xpos;
					bricks[index][1] = init_ypos;
					index++;
					init_xpos += 90;
				}
				init_ypos += 55;
			}
			
			timer.restart();
		}
		
		// move the paddle right if RIGHT was pressed
		if (key_code == KeyEvent.VK_RIGHT) {
			if (this.xPaddle + 100 >= 775) {
				this.xPaddle = 775 - 100;
			} else {
				updateRight();
			}
		}
	}

	/*
	 * Unused Functions
	 */
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}

}
