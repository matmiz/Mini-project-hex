package com.hex;
public class Hex {

	private static HexGame game;
	public static void main(String[] args) {
		game = new HexGame();//HexBoard.BOARD_SIZE);
		System.out.println("tree size: " + game.treeSize());
	}

}
