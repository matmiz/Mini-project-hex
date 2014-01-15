
package com.hex;

import java.util.HashMap;

/**
 * Represents a MinMax game-tree. <br>
 * Notice that the tree is recursively self-constructing up to the provided maximal depth.<br>
 * The construction includes applying the evaluation function, thus the process may take a considerable time.
*/
public class MinMaxGameTree {
	
	public HashMap<Integer, Double> hm;
	private TreeNode root;
	private int leafSize;
	public byte min, max;
	private HexBoard mainBoard;
	/**
	 * Creates a MinMax game-tree with supplied board as the root position of the tree.<br>
	 * @param board The root position.
	 * @param leafSize The maximal number of leaves in the game tree
	 * @param playedColor The color of the player which made the last move.<br>
	 * If its the first move, playedColor must be the opposite of the player which has the next turn.
	 * @param min The rival player.
	 * @param max The automatic player.
	 *  
	 */
	public MinMaxGameTree(HexBoard board, int leafSize, byte playedColor, byte min, byte max) {
		this.max = max;
		this.min = min;
		this.leafSize = leafSize;
		mainBoard = board;
		hm = new HashMap<Integer, Double>((int) Math.ceil(this.leafSize*0.15));
		assignNewRoot(new TreeNode(board.getCopy(), (byte)0, (byte)0, playedColor, this));
	}

	/**
	 * When a move is made to a certain position, make that position be the root position.
	 * @param newRoot Must be a TreeNode of the game-tree, otherwise a heavy and time consuming tree-generation will take place.
	 */
	public byte[] assignNewRoot(TreeNode newRoot){
		root = newRoot;
		hm.clear();
		root.buildSubtreeAlphaBeta(calculateTreeDepth(mainBoard), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
		return root.getOptimalMovement();
	}
	
	public void print(){
		root.print(0);
	}
	
	public int size(){
		return root.getSize();
	}
	
	public TreeNode getRoot(){
		return this.root;
	}

	
	public TreeNode getMovedNode(int i, int j, byte player, HexBoard board) {
		TreeNode tn =  root.getChildWithMovement(i, j);
		if(tn == null){
			tn = new TreeNode(board, (byte)i, (byte)j, player, this);
			tn.buildSubtreeAlphaBeta(calculateTreeDepth(mainBoard)-1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
			root.attachChild(tn);
		}
		return tn;
	}
	
	private int calculateTreeDepth(HexBoard board){
		int td = 1;
		int movesLeft = board.getBoardEmptySize();
		int terminalSize = movesLeft;
		while((terminalSize = terminalSize*(movesLeft-td)) < this.leafSize){
			td++;
		}
		return td;
	}

}
