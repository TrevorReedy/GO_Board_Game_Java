package piece;
import main.Board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Board;

public class pieces {
	
	public int col,row;
	public int xPos,yPos;
	public boolean isWhite;
	public Color color;
	public int value;
	public int liberties;
	

           Board board;
       	public pieces(Board board, int col, int row, Color color, boolean isWhite) {
    		this.board = board;
    		this.col = col;
    		this.row = row;
    		this.isWhite = isWhite;
    		this.color = color;    		
            this.xPos = col * board.tile_size;
            this.yPos = row * board.tile_size;

    	}

        public void paint(Graphics2D g2d) {
            g2d.setColor(color);
            // Draw piece shape (e.g., oval or image)
            g2d.fillOval(xPos-board.box_size, yPos-board.box_size, board.box_size*2, board.box_size*2);
        }
           

}
