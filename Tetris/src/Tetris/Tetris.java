package Tetris;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;




@SuppressWarnings("serial")
public class Tetris extends JFrame {
    JLabel statusbar;


    public Tetris() {
        statusbar = new JLabel("0");
        add(statusbar, BorderLayout.SOUTH);
        Board board = new Board(this);
        add(board);
        board.start();
        setSize(525, 1050);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

   }

   public JLabel getStatusBar() {
       return statusbar;
   }
   

    public static void main(String[] args) {
    	Tetris game = new Tetris();
    	game.setLocationRelativeTo(null);
    	game.setVisible(true);



    } 
}