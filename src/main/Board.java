package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

import piece.pieces;

public class Board extends JPanel {
    
	public boolean[][] tileStates; // 2-d array to track tiles
	public pieces[][] pieceArray; // 2-d array to track pieces
	

    boolean isBlackTurn = true; // New variable to track the turn
	public int white_score = 0;
	public int black_score = 0;
	public int n_clicks = 0; // Track the number of clicks
    int emptyIntersections;
    int n_passes = 0;
   
    public int tile_size = 80;
    public int box_size = 20;
    public int cols = 8;
    public int rows = 8;
    
    

    public Board() {
        this.setPreferredSize(new Dimension((cols) * tile_size, (rows) * tile_size));
        this.setBackground(new Color(245, 222, 178));
        tileStates = new boolean[cols+1][rows+1];
        pieceArray = new pieces[cols+1][rows+1];
        addMouseListener(new Input(this));
    }

    
    
    private Color emptySurroundingColor(ArrayList<int[]> group) {
        Set<Color> surroundingColors = new HashSet<>();
        
        for (int[] coords : group) {
            int col = coords[0];
            int row = coords[1];
            
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int newCol = col + dir[0];
                int newRow = row + dir[1];
                if (isValidPosition(newCol, newRow) && tileStates[newCol][newRow] && pieceArray[newCol][newRow] != null) {
                    surroundingColors.add(pieceArray[newCol][newRow].color);
                }
            }
        }
        
        if (surroundingColors.size() == 1) {
            return surroundingColors.iterator().next();
        }
        return null; // Neutral territory
    }
    
    private int getCapturedStones(boolean forBlack) {
        int capturedStones = 0;
        for (int col = 0; col <= cols; col++) {
            for (int row = 0; row <= rows; row++) {
                if (tileStates[col][row] && pieceArray[col][row] != null) {
                    if (pieceArray[col][row].isWhite == forBlack) {
                        capturedStones++;
                    }
                }
            }
        }
        return capturedStones;
    }
    private ArrayList<int[]> groupEmptyTerritory(int startCol, int startRow, boolean[][] visited) {
        ArrayList<int[]> group = new ArrayList<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startCol, startRow});
        visited[startCol][startRow] = true;
        
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int col = current[0];
            int row = current[1];
            group.add(current);
            
            for (int[] dir : directions) {
                int newCol = col + dir[0];
                int newRow = row + dir[1];
                if (isValidPosition(newCol, newRow) && !tileStates[newCol][newRow] && !visited[newCol][newRow]) {
                    queue.offer(new int[]{newCol, newRow});
                    visited[newCol][newRow] = true;
                }
            }
        }
        
        return group;
    }

    private boolean isBlackSurrounding(ArrayList<int[]> group) {
        Set<Color> surroundingColors = new HashSet<>();
        
        for (int[] coords : group) {
            int col = coords[0];
            int row = coords[1];
            
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int newCol = col + dir[0];
                int newRow = row + dir[1];
                if (isValidPosition(newCol, newRow) && tileStates[newCol][newRow] && pieceArray[newCol][newRow] != null) {
                    surroundingColors.add(pieceArray[newCol][newRow].color);
                }
            }
        }
        
        if (surroundingColors.size() == 1) {
            return surroundingColors.iterator().next() == Color.BLACK;
        }
        return false; // Default to white if territory is neutral
    }
    
    public void pass() {
        n_passes++;
        System.out.println("pass count: " + n_passes);
        if (n_passes == 2) {
            score_game();
            System.out.println("end of game");
        }
        incrementClicks();
    }
    
    public void resign() {
        score_game();
        System.out.println("end of game");
    }

    public void makeMove(move move) {
        Color pieceColor = isBlackTurn ? Color.BLACK : Color.WHITE;
        boolean isWhite = !isBlackTurn;
        pieces newPiece = new pieces(this, move.newCol, move.newRow, pieceColor, isWhite);
        newPiece.col = move.newCol;
        newPiece.row = move.newRow;
        newPiece.xPos = move.newCol * tile_size;
        newPiece.yPos = move.newRow * tile_size;

        pieceArray[move.newCol][move.newRow] = newPiece;
        tileStates[move.newCol][move.newRow] = true;

        captureOpponentStones(newPiece);
        repaint();
    }

    public void captureOpponentStones(pieces placedPiece) {
        ArrayList<pieces> opponentNeighbors = getOpponentNeighbors(placedPiece);
        for (pieces opponentPiece : opponentNeighbors) {
            if (hasNoLiberties(opponentPiece)) {
                removeGroup(opponentPiece);
            }
        }
    }
    
    private boolean hasNoLiberties(pieces piece) {
        Set<pieces> group = new HashSet<>();
        return hasNoLibertiesRecursive(piece, group);
    }

    private boolean hasNoLibertiesRecursive(pieces piece, Set<pieces> group) {
        if (group.contains(piece)) {
            return true;
        }
        if (hasEmptyAdjacent(piece)) {
            return false;
        }
        group.add(piece);
        


        ArrayList<pieces> sameColorNeighbors = getSameColorNeighbors(piece);
        for (pieces neighbor : sameColorNeighbors) {
            if (!hasNoLibertiesRecursive(neighbor, group)) {
                return false;
            }
        }

        return true;
    }
    
    private boolean hasEmptyAdjacent(pieces piece) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newCol = piece.col + dir[0];
            int newRow = piece.row + dir[1];
            if (isValidPosition(newCol, newRow) && pieceArray[newCol][newRow] == null) {
                return true;
            }
        }
        return false;
    }

    private void removeGroup(pieces piece) {
        Set<pieces> group = new HashSet<>();
        removeGroupRecursive(piece, group);
        for (pieces removedPiece : group) {
            pieceArray[removedPiece.col][removedPiece.row] = null;
            tileStates[removedPiece.col][removedPiece.row] = false;
            if (removedPiece.isWhite) {
                black_score++;
            } else {
                white_score++;
            }
        }
        System.out.println("Black Score= " + black_score);
        System.out.println("White Score= " + white_score);
        updateLiberties();
    }

    private void removeGroupRecursive(pieces piece, Set<pieces> group) {
        if (group.contains(piece)) {
            return;
        }
        group.add(piece);

        ArrayList<pieces> sameColorNeighbors = getSameColorNeighbors(piece);
        for (pieces neighbor : sameColorNeighbors) {
            removeGroupRecursive(neighbor, group);
        }
    }
    
    private void updateLiberties() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (pieceArray[i][j] != null) {
                    pieceArray[i][j].liberties = calculateLiberties(pieceArray[i][j]);
                }
            }
        }
    }
    
    private int calculateLiberties(pieces piece) {
        int libertyCount = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newCol = piece.col + dir[0];
            int newRow = piece.row + dir[1];
            if (isValidPosition(newCol, newRow) && pieceArray[newCol][newRow] == null) {
                libertyCount++;
            }
        }
        return libertyCount;
    }
    
    private boolean isValidPosition(int col, int row) {
        return col >= 0 && col < cols + 1 && row >= 0 && row < rows + 1;
    }
    
    public ArrayList<pieces> getOpponentNeighbors(pieces piece) {
        ArrayList<pieces> opponents = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newCol = piece.col + dir[0];
            int newRow = piece.row + dir[1];
            if (isValidPosition(newCol, newRow) && pieceArray[newCol][newRow] != null &&
                pieceArray[newCol][newRow].isWhite != piece.isWhite) {
                opponents.add(pieceArray[newCol][newRow]);
            }
        }
        return opponents;
    }

    public ArrayList<pieces> getSameColorNeighbors(pieces piece) {
        ArrayList<pieces> sameColor = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newCol = piece.col + dir[0];
            int newRow = piece.row + dir[1];
            if (isValidPosition(newCol, newRow) && pieceArray[newCol][newRow] != null &&
                pieceArray[newCol][newRow].isWhite == piece.isWhite) {
                sameColor.add(pieceArray[newCol][newRow]);
            }
        }
        return sameColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2d);
        drawStones(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < cols; i++) {
            g2d.drawLine(i * tile_size, 0, i * tile_size, rows * tile_size);
        }
        for (int i = 0; i < rows; i++) {
            g2d.drawLine(0, i * tile_size, cols * tile_size, i * tile_size);
        }
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.green);
        for (int i = 0; i < cols+1; i++) {
            for (int j = 0; j < rows+1; j++) {
                if (!tileStates[i][j]) {
                    int x = i * tile_size - (box_size / 2);
                    int y = j * tile_size - (box_size / 2);
                    g2d.drawRect(x, y, box_size, box_size);
                }
            }
        }
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(0, 0, cols * tile_size, rows * tile_size);
    }

    private void drawStones(Graphics2D g2d) {
        for (int i = 0; i < cols+1; i++) {
            for (int j = 0; j < rows+1; j++) {
                pieces piece = pieceArray[i][j];
                if (piece != null) {
                    piece.paint(g2d);
                }
            }
        }
    }

    public boolean isWhiteTurn() {
        return (getTotalMoves() % 2 == 0);
    }

    public int getTotalMoves() {
        int count = 0;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (pieceArray[i][j] != null) {
                    count++;
                }
            }
        }
        return count;
    }



public void incrementClicks() {
    n_clicks++;
    isBlackTurn = !isBlackTurn;
}

public void score_game() {
    emptyIntersections = 0;
    white_score = 0;
    black_score = 0;
    
    boolean[][] visited = new boolean[cols+1][rows+1];
    
    for (int col = 0; col <= cols; col++) {
        for (int row = 0; row <= rows; row++) {
            if (!tileStates[col][row] && !visited[col][row]) {
                ArrayList<int[]> emptyGroup = groupEmptyTerritory(col, row, visited);
                emptyIntersections += emptyGroup.size();
                
                Color surroundingColor = emptySurroundingColor(emptyGroup);
                
                if (surroundingColor == Color.BLACK) {
                    black_score += emptyGroup.size();
                    for (int[] coords : emptyGroup) {
                      pieceArray[coords[0]][coords[1]] = new pieces(this, coords[0], coords[1], Color.BLACK, false);
                      repaint();

             
                    }
                } else if (surroundingColor == Color.WHITE) {
                    white_score += emptyGroup.size();
                    for (int[] coords : emptyGroup) {
                        pieceArray[coords[0]][coords[1]] = new pieces(this, coords[0], coords[1], Color.WHITE, true);
                        repaint();
                    }
                }
                // If surroundingColor is null, it's neutral territory and not scored
            }
        }
    }
    repaint();
    // Add captured stones to the score
    black_score += getCapturedStones(true);
    white_score += getCapturedStones(false);
    
    System.out.println("Empty Intersections: " + emptyIntersections);
    System.out.println("Black Score: " + black_score);
    System.out.println("White Score: " + white_score);
}



}