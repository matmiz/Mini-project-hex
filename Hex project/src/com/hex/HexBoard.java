package com.hex;

/**
 * Represents the board of the Hex Game as a 2-d matrix.<br>
 */

public class HexBoard {
	
	private byte[][] board;
	public final byte ROW_PLAYER;
	public final byte COL_PLAYER;
	public final byte EMPTY = 0;
	private byte boardSize;
	
	/**
	 * Constructs a game board.
	 * @param size The dimensions of the board
	 */
	public HexBoard(byte size, byte rowPlayer, byte colPlayer) {
		ROW_PLAYER = rowPlayer;
		COL_PLAYER = colPlayer;
		this.boardSize =  (byte) (size + 2);
		this.board = new byte[boardSize][boardSize];
		colorSides();
	}
	
	/**
	 * Assign the rows to the black player and the columns to the white player.<br>
	 * Corner cells are empty.
	 */
	private void colorSides(){
		int i;
		
		for(i=0; i<boardSize; i++){
			board[0][i] = ROW_PLAYER;
			board[boardSize-1][i] = ROW_PLAYER;
			
			board[i][0] = COL_PLAYER;
			board[i][boardSize-1] = COL_PLAYER;
		}
		board[0][0] = EMPTY;
		board[boardSize-1][0] = EMPTY;
		board[0][boardSize-1] = EMPTY;
		board[boardSize-1][boardSize-1] = EMPTY;
	}
	
	/**
	 * Returns the color of the asked cell.
	 * @param row
	 * @param col
	 * @return The color of the [row, col] cell
	 */
	public int getColorAt(int row, int col){
		return board[row+1][col+1];
	}
	
	/**
	 * Assigns the cell in the given coordinates the given color. 
	 * @param row
	 * @param col
	 * @param color
	 */
	public void setColorAt(int row, int col, byte color){
		board[row+1][col+1] = color;
	}
	
	/**
	 * Creates a copy of this board.
	 * @return the copy of this board.
	 */
	public HexBoard getCopy(){
		HexBoard newBoard = new HexBoard((byte) (boardSize-2), ROW_PLAYER, COL_PLAYER);
		for(int i=1; i<boardSize-1; i++){
			for(int j=1; j<boardSize-1; j++){
				newBoard.setColorAt(i-1, j-1, board[i][j]);
			}
		}
		return newBoard;
	}

	/**
	 * Applies an evaluation function on current position from the perspective of the given player.
	 * 
	 * @return Rank from 0(weakest-victory for the other side) to 100(strongest-victory for the current player).
	 */
	public float evaluate() {
		return 0;
	}

	public void print(int indent) {
		for (int i = 0; i < boardSize ; i++) {
			for(int k=0; k<=indent; k++){
				System.out.print('\t');
			}
		    for (int j = 0; j < boardSize; j++) {
		        System.out.print(board[i][j] + " ");
		    }
		    System.out.print("\n");
		}
		System.out.print("\n");
	}
}
