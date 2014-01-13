package com.hex;

public class Hex {

	private static HexGame game;
	
	public static void main(String[] args) {

		game = new HexGame((byte) 3, (byte) 3, (byte) 3, (byte)1, (byte)0, true);

	}
}
