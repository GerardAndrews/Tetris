package Tetris;

import Tetris.Shape.Tetrominoes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import sf.Sound;
import sf.SoundFactory;




// @SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int BoardWidth = 15;
    final int BoardHeight = 26;

    Timer timer;
    boolean isFallingFinished = false;
    boolean isStarted = false;
    boolean isPaused = false;
    boolean inStartMenu = true;
    int numLinesRemoved = 0;
    int curX = 0;
    int curY = 0;
    String name;
    String highScore;
    JLabel statusbar;
    JLabel score;
    Shape curPiece;
    Shape savedPiece;
    Shape followingPiece;
    Tetrominoes[] board;

    public final static String DIR = "src/res/";
    public final static String SOUND_AutoMove = DIR + "sfx_sounds_button6.wav";
    public final static String SOUND_PlayerMove = DIR + "sfx_sounds_button3.wav";
    public final static String SOUND_Stop = DIR + "sfx_alarm_loop3.wav";
    

    public Board(Tetris parent) {
       setFocusable(true);
       // curPiece = new Shape();
       // curPiece.setRandomShape();
       // if 
       followingPiece = new Shape();
       followingPiece.setRandomShape();
       
       timer = new Timer(400, this);
       timer.start(); 

       statusbar =  parent.getStatusBar();
       statusbar.setFont(statusbar.getFont().deriveFont(20.0f));
       highScore = this.getHighScore();
       
       score = new JLabel("Current High Score: " + highScore);
       add(score, BorderLayout.CENTER);
       
       score.setFont(statusbar.getFont().deriveFont(20.0f));
       board = new Tetrominoes[BoardWidth * BoardHeight];
       addKeyListener(new TAdapter());
       clearBoard(); 
       
    }

    public void actionPerformed(ActionEvent e) {
    	scaleDifficulty();
    		
        if (isFallingFinished) {
        	
        	// The tertromino fell, now for the next one
            isFallingFinished = false;
            newPiece();
        } else {
        	Sound move = SoundFactory.getInstance(SOUND_AutoMove);
        	SoundFactory.play(move);
            oneLineDown();
        }
    }


    int squareWidth() { 
    	return (int) getSize().getWidth() / BoardWidth; 
    	}
    
    int squareHeight() { 
    	return (int) getSize().getHeight() / BoardHeight; 
    	}
    
    Tetrominoes shapeAt(int x, int y) { 
    	return board[(y * BoardWidth) + x]; 
    	}

    public void start()
    {
    	// Start the game!
    	
        if (isPaused)
            return;
        
        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        savedPiece = null;
        clearBoard();

        newPiece();
        timer.start();
        statusbar.setText("Score: " + String.valueOf(numLinesRemoved) + 
    			"    P = Pause/Unpause | Q = Quit");
    }

    public void pause()
    {
        if (!isStarted)
            return;

        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            statusbar.setText("Paused     "
            		+ "P = Pause/Unpause | Q = Quit");
        } else {
            timer.start();
            statusbar.setText("Score: " + String.valueOf(numLinesRemoved) + 
        			"    P = Pause/Unpause | Q = Quit");
        }
        repaint();
    }

    public void paint(Graphics g)
    { 
        super.paint(g);
        Font fnt0 = new Font("arial", Font.BOLD, 50);
    	g.setFont(fnt0);
    	g.setColor(Color.black);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

        if (inStartMenu == false) {
        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != Tetrominoes.NoShape)
                    drawSquare(g, 0 + j * squareWidth(),
                               boardTop + i * squareHeight(), shape);
            }
        }

        if (curPiece.getShape() != Tetrominoes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, 0 + x * squareWidth(),
                           boardTop + (BoardHeight - y - 1) * squareHeight(),
                           curPiece.getShape());
            }
        }
        
        if (followingPiece.getShape() != Tetrominoes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int shapeX = 0 + followingPiece.x(i);
                int shapeY = - followingPiece.y(i);
                
                int x = 0 + shapeX * squareWidth() + 75;
                int y = boardTop + (BoardHeight - shapeY - 1) * 
                		squareHeight() - 850;
                
                drawSquare(g, x, y, followingPiece.getShape());
                Font fnt1 = new Font("arial", Font.BOLD, 20);
            	g.setFont(fnt1);
                g.drawString("Next Piece", 25, 50);//
            }
        }
        if (savedPiece != null) {
            for (int i = 0; i < 4; ++i) {
                int shapeX = 0 + savedPiece.x(i);
                int shapeY = - savedPiece.y(i);
                
                int x = 0 + shapeX * squareWidth() + 400;
                int y = boardTop + (BoardHeight - shapeY - 1) * 
                		squareHeight() - 850;
                
                drawSquare(g, x, y, savedPiece.getShape());
                Font fnt2 = new Font("arial", Font.BOLD, 20);
            	g.setFont(fnt2);
                g.drawString("Saved Piece", 350, 50);//
            }
        }
        else {
        	Font fnt3 = new Font("arial", Font.BOLD, 20);
        	g.setFont(fnt3);
            g.drawString("Saved Piece", 350, 50);
        }
        
        if (isPaused) {
        	Font fnt3 = new Font("arial", Font.BOLD, 20);
        	g.setFont(fnt0);
            g.drawString("Controls", 150, 200);
            g.setFont(fnt3);
            g.drawString("<= & =>: Move piece Left or Right", 100, 240);
            g.drawString("Up and Down Arrows: Rotate Piece", 100, 280);
            g.drawString("D: Move One Line Down", 140, 320);
            g.drawString("Spacebar: Instant Drop", 150, 360);
            g.drawString("C: Save Piece", 180, 400);
        }
        
        }
        else {
        	pause();
        	ShowIntroScreen(g);
        }
    }
    
    public void ShowIntroScreen(Graphics g) {
        g.drawString("Tetris", 195, 400);
        g.drawString("Press S to start!", 100, 550);
        statusbar.setText("P = Pause/Unpause | Q = Quit");
    }

    private void dropDown()
    {
    	// Where is the tetromino after it went down by 1?
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1))
                break;
            --newY;
        }
        pieceDropped();
    }

    private void oneLineDown()
    {
        if (!tryMove(curPiece, curX, curY - 1))
            pieceDropped();
    }

    private void scaleDifficulty() {
    	if(numLinesRemoved >= 5 && numLinesRemoved <= 10) {
    		timer.stop();
    		this.timer = new Timer(350, this);
    		timer.start();
    	}
    	
    	if(numLinesRemoved >= 11 && numLinesRemoved <= 15) {
    		timer.stop();
    		this.timer = new Timer(300, this);
    		timer.start();
    	}
    	
    	if(numLinesRemoved >= 16 && numLinesRemoved <= 20) {
    		timer.stop();
    		this.timer = new Timer(250, this);
    		timer.start();
    	}
    	
    	if(numLinesRemoved >= 21 && numLinesRemoved <= 25) {
    		timer.stop();
    		this.timer = new Timer(200, this);
    		timer.start();
    	}
    	
    	if(numLinesRemoved >= 26 && numLinesRemoved <= 30) {
    		timer.stop();
    		this.timer = new Timer(150, this);
    		timer.start();
    	}
    	
    	if(numLinesRemoved >= 61 && numLinesRemoved <= 70) {
    		timer.stop();
    		this.timer = new Timer(100, this);
    		timer.start();
    	}
    }
    
    private void clearBoard()
    {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            board[i] = Tetrominoes.NoShape;
    }

    private void pieceDropped()
    {
    	Sound stop = SoundFactory.getInstance(SOUND_Stop);
        for (int i = 0; i < 4; ++i) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BoardWidth) + x] = curPiece.getShape();
            
        }

        removeFullLines();

        if (!isFallingFinished)
        	SoundFactory.play(stop);
            newPiece();
    }

    private void newPiece()
    {
    	// Create a new piece
//        curPiece.setRandomShape();
        // What is the current piece's name?
        curPiece = followingPiece;
        
        followingPiece = new Shape();
        followingPiece.setRandomShape();

        
        curX = BoardWidth / 2 + 1;
        curY = BoardHeight - 1 + curPiece.minY();
        


        // What if we can't move?
        if (!tryMove(curPiece, curX, curY)) {
        	clearBoard();
            curPiece.setShape(Tetrominoes.NoShape);
            timer.stop();
            isStarted = false;
            CheckScore();
            statusbar.setText("Game Over! Press R to restart or Q to quit.");
            if (highScore.contentEquals("AAA:0")) {
            	highScore = this.getHighScore();
            }
            GameOver();
        }
    }
    
    private void PieceSwap() {
    	if (savedPiece == null) {
            savedPiece = curPiece;
            newPiece();
       	 }
       	if (savedPiece.getShape() != Tetrominoes.NoShape) {
       		Shape formerSavedPiece;
       		formerSavedPiece = savedPiece;
       		savedPiece = curPiece;
       		curPiece = formerSavedPiece;
       	}
    }
    
    private void GameOver() {
    	score.setText("Score: " + numLinesRemoved + "\n High Score: " + 
    highScore);
    }

    private boolean tryMove(Shape newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt(x, y) != Tetrominoes.NoShape)
                return false;
        }
        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void removeFullLines()
    {
        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
            	// Delete it
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                         board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
        	// Add to score
            numLinesRemoved += numFullLines;
            statusbar.setText("Score: " + String.valueOf(numLinesRemoved) + 
        			"    P = Pause/Unpause | Q = Quit");
            isFallingFinished = true;
            Sound stop = SoundFactory.getInstance(SOUND_Stop);
            SoundFactory.play(stop);
            curPiece.setShape(Tetrominoes.NoShape);
            repaint();
        }
     }

    private void drawSquare(Graphics g, int x, int y, Tetrominoes shape)
    {
        Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), 
            new Color(102, 204, 102), new Color(102, 102, 204), 
            new Color(204, 204, 102), new Color(204, 102, 204), 
            new Color(102, 204, 204), new Color(218, 170, 0)
        };


        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + 1);

    }

    class TAdapter extends KeyAdapter {

         public void keyPressed(KeyEvent e) {
        	 int keycode = e.getKeyCode();
        	 Sound manipulate = SoundFactory.getInstance(SOUND_PlayerMove);
             if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
            	 
            	 // Restart and Quit when game over
            	 if (keycode == 'r' || keycode == 'R') {
                	start();
                	score.setText("");
                	statusbar.setText("Score: " + 
                	String.valueOf(numLinesRemoved) + 
                	" P = Pause/Unpause | Q = Quit");
                	return;
            	 }
                	 
                if (keycode == 'q' || keycode == 'Q') {
                    System.exit(0);
                    return;
                	 }
                 return;
             }
             // Pause and Quit

             if (keycode == 'p' || keycode == 'P') {
                 pause();
                 return;
             }
             
             if (keycode == 's' || keycode == 'S') {
            	 inStartMenu = false;
            	 start();
                 return;
             }
             
             if (keycode == 'q' || keycode == 'Q') {
            	 System.exit(0);
                 return;
             }
             
             if (isPaused)
                 return;
             
             // Movement Keys

             switch (keycode) {
             case KeyEvent.VK_LEFT:
                 tryMove(curPiece, curX - 1, curY);
                 SoundFactory.play(manipulate);
                 break;
             case KeyEvent.VK_RIGHT:
                 tryMove(curPiece, curX + 1, curY);
                 SoundFactory.play(manipulate);
                 break;
             case KeyEvent.VK_DOWN:
                 tryMove(curPiece.rotateRight(), curX, curY);
                 SoundFactory.play(manipulate);
                 break;
             case KeyEvent.VK_UP:
                 tryMove(curPiece.rotateLeft(), curX, curY);
                 SoundFactory.play(manipulate);
                 break;
             case KeyEvent.VK_SPACE:
                 dropDown();
                 SoundFactory.play(manipulate);
                 break;
             case KeyEvent.VK_Q:
                 System.exit(0);
                 break;
             case 'd':
                 oneLineDown();
                 SoundFactory.play(manipulate);
                 break;
             case 'D':
                 oneLineDown();
                 SoundFactory.play(manipulate);
                 break;
             case 'c':
                 PieceSwap();
                 break;
             case 'C':
                 PieceSwap();
                 break;
             }

         }
     }
    
    public String getHighScore() {
    	
    	FileReader readFile = null;
    	BufferedReader reader = null;
    	try {
    		readFile = new FileReader("highscore.dat");
    		
    		reader = new BufferedReader(readFile);
    		
    		String line = reader.readLine();
    		
    		return line;
    	}
    	
    	catch (Exception e) {
    		return "AAA:0";
    	}
    	
    	finally {
    		try {
    			if (reader != null) {
    				reader.close();
    				}
    		} 
    			catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    public void CheckScore() {
    	// System.out.println(highScore);
    	int _highScore = -1;
    	
    	if(!highScore.isEmpty()) {
    		String fields[] = highScore.split(":");
    		
    		_highScore = Integer.parseInt(fields[1]);
    	}
    	
    	if (numLinesRemoved > _highScore) {
    		name = JOptionPane.showInputDialog("You've set a new score:! "
    	+ numLinesRemoved + " Please Enter your name:");
    		highScore = name + ":" + numLinesRemoved;
    		
    		File scoreFile = new File("highscore.dat");
    		// Does highscore.dat exist?
    		if (!scoreFile.exists()) {
    			try {
					scoreFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    		FileWriter writeFile = null;
    		BufferedWriter writer = null;
    		try {
    			writeFile = new FileWriter(scoreFile);
    			writer = new BufferedWriter(writeFile);
    			writer.write(highScore);
    		}
    		catch (Exception e) {
    			//errors
    		}
    		
    		finally {
    			if (writer != null) {
    				try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
    			}
    		}		
    	}
    }
}