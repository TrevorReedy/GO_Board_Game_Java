//move.java
package main;

import piece.pieces;

public class move {

	int newCol;
	int newRow;
	
	pieces piece;
//	piece capture;
	
	Board board = new Board();
	public move(Board board , int newCol, int newRow) {
		this.newCol = newCol;
		this.newRow = newRow;
		board.n_passes = 0;
//		this.capture = board.getPiece(newCol, newRow);
	}
}