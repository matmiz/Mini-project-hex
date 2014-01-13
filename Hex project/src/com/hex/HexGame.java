package com.hex;

import com.hex.view.BoardView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class HexGame extends Activity{

	private HexBoard board;
	private MinMaxGameTree gameTree;
	public static byte boardSize;
	public static byte ME;
	public static byte OPPONENT;
	public static byte EMPTY;
	public static boolean PLAYING; 
	private byte[] nextMove = new byte[3];
	/**
	 * Creates a hex game
	 * @param size The size of the game board.
	 * @param treeDepth The maximal depth of the game-tree.
	 * @param isComputerRowPlayer determines which of the players is the computer.
	 * @param colPlayer player whos goal is to make <b>horizontal<b> path.
	 * @param rowPlayer player whos goal is to make <b>vertical<b> path.
	 * @param evaluationStrenght the depth of the BFS in the evaluation function.
	 */
	public HexGame(byte size, byte treeDepth, byte evaluationStrenght, byte rowPlayer, byte colPlayer, boolean isComputerRowPlayer) {
		boardSize = size;
		
		if(isComputerRowPlayer){
			HexGame.ME = rowPlayer;
			HexGame.OPPONENT = colPlayer;
		}else{
			HexGame.ME = colPlayer;
			HexGame.OPPONENT = rowPlayer;
		}
		
		//set EMPTY to by something different than colPlayer and rowPlayer
		HexGame.EMPTY = (byte) (Math.abs(colPlayer)+Math.abs(rowPlayer)+1);
		
		board = new HexBoard(size, rowPlayer, colPlayer, EMPTY, evaluationStrenght);
		gameTree = new MinMaxGameTree(board.getCopy(),treeDepth, ME, OPPONENT, ME);
	}
	public HexGame(){
		boardSize = HexBoard.BOARD_SIZE;
		ME = 0;
		OPPONENT = 1;
		//set EMPTY to by something different than colPlayer and rowPlayer
		HexGame.EMPTY = (byte) (Math.abs(1)+Math.abs(0)+1);
		Log.d("hexgame","creating new game");
		board = new HexBoard((byte)boardSize, (byte)ME, (byte)OPPONENT, EMPTY, (byte)5);
		gameTree = new MinMaxGameTree(board.getCopy(),2, ME, OPPONENT, ME);		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//board=new HexBoard(HexBoard.BOARD_SIZE, ME, OPPONENT, EMPTY, (byte)2);
		Log.d("onCreate","creating new board view");
		BoardView bView=new BoardView(this);//,board);
		PLAYING=true;
		setContentView(bView);
	}
	/**
	 * Prints the current playing board
	 */
	public void printBoard() {
		board.print(0);
	}
	
	/**
	 * Calculates the size of the game tree.
	 * @return the Size of the game tree.
	 */
	public int treeSize(){
		return gameTree.size();
	}
	
	/**
	 * Tells the computer that the player made a move.
	 * @param i the row of the cell.
	 * @param j the column of the cell.
	 * @return true if player is victorious, false otherwise.
	 */
	public boolean moved(int i, int j){
		TreeNode tn;
		//update the board
		board.setColorAt(i, j, OPPONENT);
		
		//evaluate the board in order to check if there is a win
		if(board.evaluate(ME) == 0){
			return true;
		}
		
		//get the node in the game tree which corresponds to the current board
		tn = gameTree.getMovedNode(i, j, OPPONENT, board.getCopy());
		
		//set that node as the root of the game tree.
		nextMove = gameTree.assignNewRoot(tn);
		return false;
	}
	
	/**
	 * Calculates the move of the computer.
	 * @return array [i, j, victory] when {i,j} is the cell and <b>victory</b> specifies whether the computer made a victorious move.
	 */
	public int[] getNexMove(){
		TreeNode tn;
		int[] nMove = new int[3];
		nMove[0] = nextMove[0];
		nMove[1] = nextMove[1];
		
		//update the board.
		board.setColorAt(nMove[0], nMove[1], ME);
		
		//evaluate the board for victory checking
		if(board.evaluate(ME) == Double.MAX_VALUE){
			nMove[2] = 1;
		}else{
			nMove[2] = 0;
			
			//get the noe in the game tree which corresponds to the current board
			tn = gameTree.getMovedNode(nMove[0], nMove[1], ME, board.getCopy());
			
			//make that nodeto be theroot of the game tree.
			gameTree.assignNewRoot(tn);
		}
		return nMove;
	}

}
