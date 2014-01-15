package com.hex;
/**
 * Represents the board of the Hex Game as a 2-d matrix.<br>
 */

public class HexBoard {
	
	private byte[][] board;
	public final byte ROW_PLAYER;
	public final byte COL_PLAYER;
	public final byte EMPTY;
	private byte boardSize;
	private final byte EVAL_STRENGHT;
	private byte emptyCells;
	/**
	 * Constructs a game board.
	 * @param size The dimensions of the board
	 * @param evaluationStrenght The depth of the BFS in the evalution function
	 */
	public HexBoard(byte size, byte rowPlayer, byte colPlayer, byte noColor, byte evaluationStrenght) {
		ROW_PLAYER = rowPlayer;
		COL_PLAYER = colPlayer;
		EMPTY = noColor;
		EVAL_STRENGHT = evaluationStrenght;
		this.boardSize =  (byte) size;
		this.board = new byte[boardSize][boardSize];
		this.emptyCells = (byte) (size*size);
		
		//fill the board with EMPTY
		for(int i = 0; i < boardSize; i++){
			for(int j = 0; j < boardSize; j++){
				this.board[i][j] = EMPTY;
			}
		}
		
	}
	
	public int getBoardSize(){
		return boardSize;
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
		if(color!=EMPTY){
			this.emptyCells--;
		}
		board[row][col] = color;
	}
	
	/**
	 * Creates a copy of this board.
	 * @return the copy of this board.
	 */
	public HexBoard getCopy(){
		HexBoard newBoard = new HexBoard((byte) boardSize, ROW_PLAYER, COL_PLAYER, EMPTY, EVAL_STRENGHT);
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				newBoard.setColorAt(i, j, board[i][j]);
			}
		}
		return newBoard;
	}

	/**
	 * Applies an evaluation function on current position from the perspective of the given player.
	 * @return Rank from 0(weakest-victory for the other side) to 100(strongest-victory for the current player).
	 */
	public double evaluate(byte player) {
		AnalyzedBoard ab = new AnalyzedBoard(this.board, ROW_PLAYER, COL_PLAYER, EMPTY, EVAL_STRENGHT);
		double result = ab.getBoardValue(player);
		return result;
	}
	
	/**
	 * Prints the board.
	 * @param indent
	 */
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

	public int getBoardEmptySize() {
		return this.emptyCells;
	}
	
	/**
	 * Encodes the board to a unique hash value.
	 * @return
	 */
	public int hashify(){
		String s = "";
		for(int i = 0; i<board.length; i++){
			for(int j = 0; j<board.length; j++){
				s+=(char) (board[i][j] & 0xFF);
			}
		}
		return s.hashCode();
	}
	
}
