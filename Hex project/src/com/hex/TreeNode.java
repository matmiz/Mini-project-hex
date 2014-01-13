package com.hex;

import java.util.ArrayList;

public class TreeNode {

	public double evaluation;
	private HexBoard board; //valid only for leaf node.
	private ArrayList<TreeNode> children; //child nodes.
	public byte[] movement = new byte[3]; //what was the last move and who made it.
	private MinMaxGameTree tree;
	
	/**
	 * Creates a new Tree node
	 * @param board the board which this node represent.
	 * @param row the row of the last movement.
	 * @param col the column of the last movement.
	 * @param color the color which made the last movement.
	 * @param tree the game tree.
	 */
	public TreeNode(HexBoard board, byte row, byte col, byte color, MinMaxGameTree tree) {
		this.board = board;
		movement[0] = row;
		movement[1] = col;
		movement[2] = color;
		this.tree = tree;
	}

	/**
	 * Builds sub tree for the current node.
	 * @param curDepth the current depth in the tree.
	 * @return this node.
	 */
	public TreeNode buildSubTree(int curDepth) {
		TreeNode optimalNode = this;
		TreeNode curNode = this;
		double optimalVal;
		//check if we should build subtree at all
		if(curDepth>0){
			//if we dont have children list, then this is a leaf and subtree generation should take place.
			if(children==null){
				curNode = generateSubTree(curDepth);
				this.evaluation = curNode.evaluation;
				if(this == tree.getRoot()){
					return curNode;
				}
				return this;
			}else{
				if(movement[2] == tree.max){
					optimalVal = Float.MAX_VALUE;
				}else{
					optimalVal = 0;
				}
				//make children build their own subtrees
				for(TreeNode tn : children){
					curNode = tn.buildSubTree(curDepth-1);
					if(movement[2] == tree.max){
						if(curNode.evaluation<optimalVal){
							optimalVal = curNode.evaluation;
							optimalNode = curNode;
						}
					}else{
						if(curNode.evaluation>optimalVal){
							optimalVal = curNode.evaluation;
							optimalNode = curNode;
						}
					}
				}
				this.evaluation = optimalVal;
				if(this == tree.getRoot()){
					return optimalNode;
				}
				return this;
			}
		}
		this.evaluation = board.evaluate(tree.max);
		return this;
	}

	/**
	 * Heavy and time consuming operation:<br>
	 * The current node will generate its own subtree while calculating the optimal node for the MAX player.
	 * @param curDepth the current tree depth.
	 * @return the optimal node for the MAX player.
	 */
	private TreeNode generateSubTree(int curDepth) {
		byte nextColor;
		HexBoard childBoard;
		TreeNode childNode;
		TreeNode optimalNode = this;
		TreeNode curNode = this;
		double optimalValue;
		double curValue;
		children = new ArrayList<TreeNode>();
		
		if(movement[2] == tree.min){
			nextColor = tree.max;
		}else{
			nextColor = tree.min;
		}
		
		
		if(nextColor == tree.min){
			optimalValue = Float.MAX_VALUE;
		}else{
			optimalValue = 0;
		}
		
		for(byte i = 0; i<HexGame.boardSize; i++){
			for(byte j = 0; j<HexGame.boardSize; j++){
				if(board.getColorAt(i, j) == board.EMPTY){
					childBoard = board.getCopy();
					childBoard.setColorAt(i, j, nextColor);
					childNode = new TreeNode(childBoard, i, j, nextColor, this.tree);
					children.add(childNode);
					curNode = childNode.buildSubTree(curDepth-1);
					curValue = curNode.evaluation;
					if(nextColor == tree.min){
						if(optimalValue > curValue) {
							optimalValue = curValue;
							optimalNode = curNode;
						}
					}else{
						if(optimalValue<curValue) {
							optimalValue = curValue;
							optimalNode = curNode;
						}
					}
				}
			}
		}
		this.board = null;
		return optimalNode;
	}

	/**
	 * Prints the board of this node.
	 * @param depth used for indentation. 0 at the beginning.
	 */
	public void print(int depth) {
		this.board.print(depth);
		if(children!=null){
			for(TreeNode s : children){
				s.print(depth+1);
			}
		}
	}
	
	/**
	 * Calculates the number of nodes in the subtree.
	 * @return number of nodes in the subtree.
	 */
	public int getSize() {
		int size=0;
		
		if(children!=null){
			for(TreeNode s : children){
				size += s.getSize();
			}
		}
		return (size+1);
	}

	public TreeNode getChildWithMovement(int i, int j) {
		for(TreeNode tn : this.children){
			if((tn.movement[0] == i) && (tn.movement[1] == j)){
				return tn;
			}
		}
		return null;
	}

	public void attachChild(TreeNode tn) {
		this.children.add(tn);
	}
}
