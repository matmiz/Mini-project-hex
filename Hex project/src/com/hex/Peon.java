package com.hex;
/**
 * Represents the peon of the Hex Game
 */

public class Peon {
	private int id; //0 for white 1 for black
	private int x;
	private int y;

	public Peon(int id,int x , int y){
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
