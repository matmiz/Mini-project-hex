package com.hex;
/**
 * Represents the gamePiece of the Hex Game
 */

public class GamePiece {
	private int id; //0 for red 1 for blue
	private int x;
	private int y;

	public GamePiece(int id,int x , int y){
		this.id=id;
		this.x=x;
		this.y=y;
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public int getID(){
		return this.id;
	}
}
