package com.hex;

/**
 * Represents the board of the Hex Game as a 2-d matrix.<br>
 */

public class HexBoard {
	
	private byte[][] board;
	public final static byte ROW_PLAYER=2;
	public final static byte COL_PLAYER=1;
	public final static byte EMPTY = 0;
	public final static byte  BOARD_SIZE=11;
	
	/**
	 * Constructs a game board.
	 * @param size The dimensions of the board
	 */
	public HexBoard(byte size){//, byte rowPlayer, byte colPlayer) {
		//ROW_PLAYER = rowPlayer;
		//COL_PLAYER = colPlayer;
		//this.BOARD_SIZE =  (byte) (size + 2);
		this.board = new byte[BOARD_SIZE][BOARD_SIZE];
		//colorSides();
	}
	
	/**
	 * Assign the rows to the black player and the columns to the white player.<br>
	 * Corner cells are empty.
	 */
	private void colorSides(){
		int i;
		
		for(i=0; i<BOARD_SIZE; i++){
			board[0][i] = ROW_PLAYER;
			board[BOARD_SIZE-1][i] = ROW_PLAYER;
			
			board[i][0] = COL_PLAYER;
			board[i][BOARD_SIZE-1] = COL_PLAYER;
		}
		board[0][0] = EMPTY;
		board[BOARD_SIZE-1][0] = EMPTY;
		board[0][BOARD_SIZE-1] = EMPTY;
		board[BOARD_SIZE-1][BOARD_SIZE-1] = EMPTY;
	}
	
	/**
	 * Returns the color of the asked cell.
	 * @param row
	 * @param col
	 * @return The color of the [row, col] cell
	 */
	public int getColorAt(int row, int col){
		return board[row][col];
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
		HexBoard newBoard = new HexBoard((byte) (BOARD_SIZE-2));//, ROW_PLAYER, COL_PLAYER);
		for(int i=1; i<BOARD_SIZE-1; i++){
			for(int j=1; j<BOARD_SIZE-1; j++){
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
		for (int i = 0; i < BOARD_SIZE ; i++) {
			for(int k=0; k<=indent; k++){
				System.out.print('\t');
			}
		    for (int j = 0; j < BOARD_SIZE; j++) {
		        System.out.print(board[i][j] + " ");
		    }
		    System.out.print("\n");
		}
		System.out.print("\n");
	}

	public void addPeon(float x, float y) {
		
		
	}
}
