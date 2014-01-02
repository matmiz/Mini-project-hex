package com.hex;
public class HexGame {

	private HexBoard board;
	private MinMaxGameTree gameTree;
	public static byte boardSize;
	public final byte ME = 2;
	public final byte OPPONENT = 1;
	public final byte EMPTY = 0;
	
	/**
	 * Creates a hex game
	 * @param size The size of the game board.
	 * @param treeDepth The maximal de[th of the game-tree.
	 */
	public HexGame(byte size, byte treeDepth) {
		boardSize = size;
		board = new HexBoard(size, ME, OPPONENT);
		gameTree = new MinMaxGameTree(board.getCopy(),treeDepth, ME, OPPONENT, ME);
	}
	
	public void printBoard() {
		board.print(0);
	}
	
	/*
	public void printTree(){
		gameTree.print();
	}
	*/
	public int treeSize(){
		return gameTree.size();
	}

}
