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
	 * searches the children of the root for the child that is reponsible for roots evaluation.
	 * @return
	 */
	public byte[] getOptimalMovement(){
		byte[] result = null;
		for(TreeNode tn : tree.getRoot().children){
			if(tree.getRoot().evaluation == tn.evaluation){
				result = tn.movement;
				break;
			}
		}
		return result;	
	}
	
	/**
	 * Completes the tree up to the needed depth while calculating the optimal value;
	 * @param curDepth
	 * @param alpha
	 * @param beta
	 * @param evaluate
	 * @return
	 */
	public double buildSubtreeAlphaBeta(int curDepth, double alpha, double beta, boolean evaluate){
		HexBoard newBoard;
		TreeNode newNode;
		byte nextMove = (this.movement[2]==tree.max? tree.min : tree.max);
		double curValue;
		int boardHash;
		//if its a leaf, evaluate it directly and return that node
		if(curDepth == 0) {
			//we dont have to evaluate anything if the current player is the minimizer.
			if(tree.getRoot().movement[2] == tree.max || !evaluate){
				this.evaluation = 0;
			}else{
				boardHash = board.hashify();
				if(tree.hm.containsKey(boardHash)){
					this.evaluation = tree.hm.get(boardHash);
				}else{
					this.evaluation = board.evaluate(tree.max);
					tree.hm.put(boardHash, this.evaluation);
				}
			}
			return this.evaluation;
		}
		//its not a leaf.
		
		if(nextMove == tree.max){
			curValue = Double.NEGATIVE_INFINITY;
		}else{
			curValue = Double.POSITIVE_INFINITY;
		}

		//if children == null, that means that this node was previously a leaf, thus need to generate its own leaves
		if(this.children == null){
			
			if(board.getBoardEmptySize() == 0){
				this.evaluation = this.board.evaluate(tree.max);
				return this.evaluation;
			}

			this.children = new ArrayList<TreeNode>(board.getBoardEmptySize());
			for(int i = 0; i < board.getBoardSize(); i++){
				for(int j = 0; j < board.getBoardSize(); j++){
					if(board.getColorAt(i, j) == board.EMPTY){
						newBoard = board.getCopy();
						newBoard.setColorAt(i, j, nextMove);
						newNode = new TreeNode(newBoard, (byte)i, (byte)j, nextMove, tree);
						children.add(newNode);
						curValue = prunAlphaBeta(newNode, alpha, beta,curDepth, curValue, nextMove);	
					}
				}
			}
			//this.board = null;
			
		}else{
			for(TreeNode tn : this.children){
				curValue = prunAlphaBeta(tn, alpha, beta, curDepth, curValue, nextMove);	
			}
		}
		this.evaluation = curValue;
		return curValue;
		
	}
	
	/**
	 * helper function for buildSubtreeAlphaBeta function.
	 * @param tn
	 * @param alpha
	 * @param beta
	 * @param curDepth
	 * @param curValue
	 * @param nextMove
	 * @return
	 */
	private double prunAlphaBeta(TreeNode tn, double alpha, double beta, int curDepth, double curValue, byte nextMove){
		double childValue;
		if(Double.isInfinite(curValue)){
			curValue = tn.buildSubtreeAlphaBeta(curDepth-1, alpha, beta, true);
		}else{
			if(nextMove == tree.max){
				if(curValue>beta){
					tn.buildSubtreeAlphaBeta(curDepth-1, curValue, beta, false);
				}else{
					childValue = tn.buildSubtreeAlphaBeta(curDepth-1, curValue, beta, true);
					if(childValue>curValue){
						curValue = childValue;
					}
				}
			}else{
				if(curValue<alpha){
					tn.buildSubtreeAlphaBeta(curDepth-1, alpha, curValue, false);
				}else{
					childValue = tn.buildSubtreeAlphaBeta(curDepth-1, alpha, curValue, true);
					if(childValue<curValue){
						curValue = childValue;
					}
				}
			}
		}
		return curValue;
	}
	
	/**
	 * Prints the board of this node.
	 * @param depth used for indentation. 0 at the beginning.
	 */
	public void print(int depth) {
		if(depth>2) return;
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
		}else{
			size = 1;
		}
		return size;
	}

	/**
	 * Searches the children of the current node for the child that corresponds to the given movement.
	 * @param i
	 * @param j
	 * @return
	 */
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
	
	public HexBoard getBoard(){
		return this.board;
	}
}
