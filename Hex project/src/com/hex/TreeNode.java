package com.hex;
import java.util.ArrayList;

public class TreeNode {

	private float evaluation;
	private HexBoard board; //valid only for leaf node.
	private ArrayList<TreeNode> children; //child nodes.
	private byte[] movement = new byte[3]; //what was the last move and who made it.
	private byte min, max;
	
	public TreeNode(HexBoard board, byte row, byte col, byte color, byte min, byte max) {
		this.board = board;
		movement[0] = row;
		movement[1] = col;
		movement[2] = color;
		this.min = min;
		this.max = max;
	}

	public TreeNode buildSubTree(int curDepth) {
		if(curDepth>0){
			if(children==null){
				return generateSubTree(curDepth);
			}else{
				for(TreeNode tn : children){
					return tn.buildSubTree(curDepth-1);
				}
			}
		}
		this.evaluation = board.evaluate();
		return this;
	}

	private TreeNode generateSubTree(int curDepth) {
		byte nextColor;
		HexBoard childBoard;
		TreeNode childNode;
		TreeNode optimalNode = this;
		TreeNode curNode;
		float optimalValue;
		float curValue;
		children = new ArrayList<TreeNode>();
		
		if(movement[2] == this.min){
			nextColor = this.max;
		}else{
			nextColor = this.min;
		}
		
		
		if(nextColor == this.min){
			optimalValue = 100;
		}else{
			optimalValue = 0;
		}
		
		for(byte i = 0; i<HexBoard.BOARD_SIZE; i++){
			for(byte j=0; j<HexBoard.BOARD_SIZE; j++){
				if(board.getColorAt(i, j) == board.EMPTY){
					childBoard = board.getCopy();
					childBoard.setColorAt(i, j, nextColor);
					childNode = new TreeNode(childBoard, i, j, nextColor, min, max);
					children.add(childNode);
					curNode = childNode.buildSubTree(curDepth-1);
					curValue = curNode.evaluation;
					if(nextColor == min){
						if(optimalValue < curValue) {
							optimalValue = curValue;
							optimalNode = curNode;
						}
					}else{
						if(optimalValue>curValue) {
							optimalValue=curValue;
							optimalNode = curNode;
						}
					}
				}
			}
		}
		this.board = null;
		return optimalNode;
	}

	public void print(int depth) {
		this.board.print(depth);
		if(children!=null){
			for(TreeNode s : children){
				s.print(depth+1);
			}
		}
	}
	
	public int getSize() {
		int size=0;
		
		if(children!=null){
			for(TreeNode s : children){
				size += s.getSize();
			}
		}
		return (size+1);
	}
}
