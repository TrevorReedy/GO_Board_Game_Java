//input.java
package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import piece.pieces;



import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {
    private Board board;

    public Input(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // Calculate the closest intersection
        int col = Math.round(x / (float) board.tile_size);
        int row = Math.round(y / (float) board.tile_size);

        // Round col and row to the nearest integer
        col = Math.max(0, Math.min(board.cols , col));
        row = Math.max(0, Math.min(board.rows , row));

        // Adjust to find the closest intersection
        int intersectionX = col * board.tile_size;
        int intersectionY = row * board.tile_size;
        

        


        // Check if the click is closer to the intersection
        if (Math.abs(x - intersectionX) < board.tile_size / 2 && Math.abs(y - intersectionY) < board.tile_size / 2) {
            // Ensure the tile is not already occupied
            if (!board.tileStates[col][row]) {
                board.tileStates[col][row] = true;
                
                
                
                
                board.repaint(); 
                System.out.println(board.isBlackTurn ? "Black Stone placed at (" + col + ", " + row:"White Stone placed at (" + col + ", " + row);
                move move = new move(board, col, row);
                board.makeMove(move);
                board.incrementClicks();
               
                
            } else {
                System.out.println("Tile already occupied.");
            }
        } else {
            System.out.println("Click not close to any intersection. X= "+x+"y= "+y);
        }
    }
}
