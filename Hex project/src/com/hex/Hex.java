package com.hex;
public class Hex {

	private static HexGame game;
	public static void main(String[] args) {
		game = new HexGame((byte) 10, (byte) 3);
		System.out.println("tree size: " + game.treeSize());
	}

}
