package com.hex;

import com.hex.view.BoardView;
import com.hex.view.HexView;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class HexGame extends Activity {

	private HexBoard board;
	private MinMaxGameTree gameTree;
	public final byte ME = 2;
	public final byte OPPONENT = 1;
	public final byte EMPTY = 0;
	private HexView hView;
	private final static String TAG="hexGame";
	/**
	 * Creates a hex game
	 * @param size The size of the game board.
	 * @param treeDepth The maximal de[th of the game-tree.
	 */
	public HexGame(){//byte treeDepth) {
		board = new HexBoard(HexBoard.BOARD_SIZE);//, ME, OPPONENT);
		//gameTree = new MinMaxGameTree(board.getCopy(),treeDepth, ME, OPPONENT, ME);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"creating a new game");
		super.onCreate(savedInstanceState);
		//creating new board
		Log.d(TAG,"building the board");
		board=new HexBoard(HexBoard.BOARD_SIZE);
		Log.d(TAG,"creating board view");
		BoardView bView=new BoardView(this,board);
		this.hView= new HexView(this);
		Log.d(TAG,"created board vieW");
		hView.setView(bView);
		setContentView(bView);
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
