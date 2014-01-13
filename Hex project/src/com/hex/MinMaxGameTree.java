/**
 * Represents a MinMax game-tree. <br>
 * Notice that the tree is recursively self-constructing up to the provided maximal depth.<br>
 * The construction includes applying the evaluation function, thus the process may take a considerable time.
*/
package com.hex;

public class MinMaxGameTree {
	
	private TreeNode root;
	private int maxDepth;
	public byte min, max;
	/**
	 * Creates a MinMax game-tree with supplied board as the root position of the tree.<br>
	 * @param board The root position.
	 * @param maxDepth The maximal depth of the game tree.
	 * @param playedColor The color of the player which made the last move.<br>
	 * If its the first move, playedColor must be the opposite of the player which has the next turn.
	 * @param min The rival player.
	 * @param max The automatic player.
	 *  
	 */
	public MinMaxGameTree(HexBoard board, int maxDepth, byte playedColor, byte min, byte max) {
		this.max = max;
		this.min = min;
		this.maxDepth = maxDepth;
		assignNewRoot(new TreeNode(board, (byte)0, (byte)0, playedColor, this));
	}

	/**
	 * When a move is made to a certain position, make that position be the root position.
	 * @param newRoot Must be a TreeNode of the game-tree, otherwise a heavy and time consuming tree-generation will take place.
	 */
	public byte[] assignNewRoot(TreeNode newRoot){
		TreeNode optimalNode;
		root = newRoot;
		optimalNode = root.buildSubTree(maxDepth);
		return optimalNode.movement;
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
			tn.buildSubTree(this.maxDepth - 1);
			root.attachChild(tn);
		}
		return tn;
	}
}
