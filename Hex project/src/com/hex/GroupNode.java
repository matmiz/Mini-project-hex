package com.hex;

import java.util.ArrayList;

public class GroupNode {

	private byte groupPlayer;
	private ArrayList<GroupNode> neighbors;
	
	public GroupNode(byte player) {
		groupPlayer=player;
		neighbors = new ArrayList<GroupNode>();
	}
	
	public void addNeighbor(GroupNode neighbor){
		if(!neighbors.contains(neighbor)){
			neighbors.add(neighbor);
		}
	}

	public byte getPlayer() {
		return groupPlayer;
	}
	
	public ArrayList<GroupNode> getNeighbors(){
		return neighbors;
	}

	public void uniteGroups(GroupNode group) {
		for(GroupNode g : group.getNeighbors()){
			g.getNeighbors().remove(group);
			g.addNeighbor(this);
			this.addNeighbor(g);
		}
	}
}
