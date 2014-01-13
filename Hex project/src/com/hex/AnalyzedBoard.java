package com.hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

	/**The analyzed board is consisted of groups.<br>
	 * Cells a and b of color c are in the same group iff there is a c-colored path from a to b.<br>
	 * Each group is regarded as a node of a graph.<br>
	 * If cell a and b are in group g, then g inherits the neighbors of a and b.
	 */
public class AnalyzedBoard {

	private GroupNode upperSource;
	private GroupNode leftSource;
	private GroupNode bottomSink;
	private GroupNode rightSink;
	
	private byte[][] board;
	
	private ArrayList<GroupNode> groups;
	private ArrayList<GroupNode> rowPlayerGroups;
	private ArrayList<GroupNode> colPlayerGroups;
	private ArrayList<GroupNode> noPlayerGroups;
	
	final private byte ROW_PLAYER;
	final private byte COL_PLAYER;
	final private byte NO_PLAYER;
	final private byte EVAL_STRENGHT;
	private double rowValue=0, colValue=0;
	
	/**
	 * Creates the analyzed graph.
	 * @param board the board to analyze.
	 * @param rowPlayer the player which makes the vertical path.
	 * @param colPlayer the player which makes the horizontal path.
	 * @param noPlayer the empty cell indicator.
	 * @param evalStrenght the depth of the BFS.
	 */
	public AnalyzedBoard(byte[][] board, byte rowPlayer, byte colPlayer, byte noPlayer, byte evalStrenght) {
		ROW_PLAYER = rowPlayer;
		COL_PLAYER = colPlayer;
		NO_PLAYER = noPlayer;
		EVAL_STRENGHT = evalStrenght;
		
		groups = new ArrayList<GroupNode>();
		noPlayerGroups = new ArrayList<GroupNode>();
		rowPlayerGroups = new ArrayList<GroupNode>();
		colPlayerGroups = new ArrayList<GroupNode>();
		
		upperSource = new GroupNode(ROW_PLAYER);
		leftSource = new GroupNode(COL_PLAYER);
		bottomSink = new GroupNode(ROW_PLAYER);
		rightSink = new GroupNode(COL_PLAYER);

		this.board = board;
		analyzeBoard();
	}
	
	/**
	 * Converts the board into undirected graph of groups.<br>
	 * All he groups are stored in the ArrayList <b>this.groups</b>.
	 */
	private void analyzeBoard(){
		GroupNode[][] helper = constructInitialGraph();;
		if(upperSource == bottomSink){
			rowValue = Double.MAX_VALUE;
			colValue = 0;
		}else if(leftSource == rightSink){
			rowValue = 0;
			colValue = Double.MAX_VALUE;
		}else{
			groups.add(upperSource);
			groups.add(leftSource);
			groups.add(bottomSink);
			groups.add(rightSink);
			rowPlayerGroups.add(upperSource);
			rowPlayerGroups.add(bottomSink);
			colPlayerGroups.add(leftSource);
			colPlayerGroups.add(rightSink);
			for(int i=0; i<board.length; i++){
				for(int j=0; j<board.length; j++){
					if(!groups.contains(helper[i][j])){
						if(helper[i][j].getPlayer() == ROW_PLAYER){
							rowPlayerGroups.add(helper[i][j]);
						}else if(helper[i][j].getPlayer() == COL_PLAYER){
							colPlayerGroups.add(helper[i][j]);
						}else{
							noPlayerGroups.add(helper[i][j]);
						}
						groups.add(helper[i][j]);
					}
				}
			}
		}
	}

	/**
	 * constructs the graph from the board. uniting cells into groups.
	 * @return 2D matrix which is identical to board, but instead of numbers each cell holds a pointer to the group which that cell belongs to.
	 */
	private GroupNode[][] constructInitialGraph() {
		GroupNode[][] helper= new GroupNode[board.length][board.length];
		GroupNode curGroup;
		GroupNode neighbor;
		
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board.length; j++){
				
				curGroup = new GroupNode(board[i][j]);
				
				if(i==0){
					if(board[i][j] == upperSource.getPlayer()){
						curGroup = upperSource;
					}else{
						curGroup.addNeighbor(upperSource);
						upperSource.addNeighbor(curGroup);
					}
				}else{		
					neighbor = helper[i-1][j];
					if((board[i][j] == neighbor.getPlayer()) && (board[i][j] != NO_PLAYER)){
						curGroup = helper[i-1][j];
					}else{
						curGroup.addNeighbor(neighbor);
						neighbor.addNeighbor(curGroup);
					}
				}
				
				if(j==0){
					if(board[i][j] == leftSource.getPlayer()){
						leftSource.uniteGroups(curGroup);
						curGroup = leftSource;
					}else{
						curGroup.addNeighbor(leftSource);
						leftSource.addNeighbor(curGroup);
					}
				}else{
					neighbor = helper[i][j-1];
					if((board[i][j] == neighbor.getPlayer()) && (board[i][j] != NO_PLAYER)){
						neighbor.uniteGroups(curGroup);
						curGroup = neighbor;
					}else{
						curGroup.addNeighbor(neighbor);
						neighbor.addNeighbor(curGroup);
					}
				}
				
				if(i==board.length-1){
					if(board[i][j] == bottomSink.getPlayer()){
						bottomSink.uniteGroups(curGroup);
						if(curGroup == upperSource){
							upperSource = bottomSink;
						}
						curGroup = bottomSink;
					}else{
						curGroup.addNeighbor(bottomSink);
						bottomSink.addNeighbor(curGroup);
					}
				}
				
				if(j==board.length-1){
					if(board[i][j] == rightSink.getPlayer()){
						rightSink.uniteGroups(curGroup);
						if(curGroup == leftSource){
							leftSource = rightSink;
						}
						curGroup = rightSink;
					}else{
						curGroup.addNeighbor(rightSink);
						rightSink.addNeighbor(curGroup);
					}
				}
				
				if( (i != 0) && (j != board.length-1) ){
					neighbor = helper[i-1][j+1];
					if((board[i][j] == neighbor.getPlayer()) && (board[i][j] != NO_PLAYER)){
						neighbor.uniteGroups(curGroup);
						curGroup = neighbor;
					}else{
						curGroup.addNeighbor(neighbor);
						neighbor.addNeighbor(curGroup);
					}
				}
				
				helper[i][j] = curGroup;
			}
		}
		return helper;
	}
	
	public double getBoardValue(byte player){
		double result;
		if(player == ROW_PLAYER){
			if(rowValue == Double.MAX_VALUE){
				return rowValue;
			}
			if(colValue == Double.MAX_VALUE){
				return 0;
			}
		}else{
			if(rowValue == Double.MAX_VALUE){
				return 0;
			}
			if(colValue == Double.MAX_VALUE){
				return colValue;
			}
		}
		rowValue = calculateBoardValue(ROW_PLAYER);
		colValue = calculateBoardValue(COL_PLAYER);
		if(player == ROW_PLAYER){
			if(colValue != 0){
				result = rowValue/colValue;
			}else{
				result = Double.MAX_VALUE;
			}
		}else{
			if(rowValue != 0){
				result =  colValue/rowValue;
			}else{
				result = Double.MAX_VALUE;
			}
		}
		return result;
	}

	private double calculateBoardValue(byte player) {
		//run bfs from each player group
		//run edmonds-karp to determine max-flow
		int result;
		int[] vertexGradient = getVertexGradient(player);
		if(player == ROW_PLAYER){
			result = edmondsKarp(vertexGradient, upperSource, bottomSink, player);
		}else{
			result = edmondsKarp(vertexGradient, leftSource, rightSink, player);
		}
		return result;
	}

	private int[] getVertexGradient(byte player) {
		int [] gradients = new int[groups.size()];
		if(player == ROW_PLAYER){
			for(GroupNode g : rowPlayerGroups){
				BFS(player, g, gradients);
			}
		}else{
			for(GroupNode g : colPlayerGroups){
				BFS(player, g, gradients);
			}
		}
		return gradients;
	}

	private void BFS(byte player, GroupNode vertex, int[] gradients) {
		int nodeIndex;
		int fatherIndex;
		GroupNode curNode;
		ArrayList<GroupNode> queue = new ArrayList<GroupNode>();
		ArrayList<GroupNode> vSet = new ArrayList<GroupNode>();
		int[] distances = new int[groups.size()];
		queue.add(vertex);
		vSet.add(vertex);
		gradients[groups.indexOf(vertex)] = (int) Math.pow(2,16);
		distances[groups.indexOf(vertex)] = 0;
		while(!queue.isEmpty()){
			curNode = queue.get(queue.size()-1);
			queue.remove(queue.size()-1);
			fatherIndex = groups.indexOf(curNode);
			
			if(distances[fatherIndex]>=EVAL_STRENGHT){
				continue;
			}
			
			for(GroupNode g : curNode.getNeighbors()){
				if(g.getPlayer() != NO_PLAYER){
					continue;
				}
				if(!vSet.contains(g)){
					vSet.add(g);
					queue.add(g);
					nodeIndex = groups.indexOf(g);
					distances[nodeIndex] = distances[fatherIndex]+1;
					gradients[nodeIndex] += calcGradient(distances[nodeIndex],256);
				}
			}
		}
	}
	private int calcGradient(int distance, int sourcePower){
		return (int) Math.pow(sourcePower,(1/(Math.pow(2, Math.log(distance+1)/Math.log(2)))));
	}
	
	/**
	 * Finds the maximum flow in a flow network.
	 * @param E neighbour lists
	 * @param C capacity matrix (must be n by n)
	 * @param s source
	 * @param t sink
	 * @return maximum flow
	 */
    private int edmondsKarp(int[] gradients, GroupNode s, GroupNode t, byte player) {
        int n = groups.size();
        // Residual capacity from u to v is C[u][v] - F[u][v]
        int[][] F = new int[n][n];
        int sIndex = groups.indexOf(s);
        int vIndex;
        int uIndex;
        int tIndex = groups.indexOf(t);
        while (true) {
            int[] P = new int[n]; // Parent table
            Arrays.fill(P, -1);
            P[sIndex] = sIndex;
            int[] M = new int[n]; // Capacity of path to node
            M[sIndex] = Integer.MAX_VALUE;
            // BFS queue
            Queue<GroupNode> Q = new LinkedList<GroupNode>();
            Q.offer(s);
            LOOP:
            while (!Q.isEmpty()) {
                GroupNode u = Q.poll();
                uIndex = groups.indexOf(u);
                for (GroupNode v : u.getNeighbors()) {
                    // There is available capacity,
                    // and v is not seen before in search
                	vIndex = groups.indexOf(v);
                    if (getCapacity(uIndex,vIndex,gradients, player) - F[uIndex][vIndex] > 0 && P[vIndex] == -1) {
                        P[vIndex] = uIndex;
                        M[vIndex] = Math.min(M[uIndex], getCapacity(uIndex,vIndex,gradients, player) - F[uIndex][vIndex]);
                        if (v != t)
                            Q.offer(v);
                        else {
                            // Backtrack search, and write flow
                            while (P[vIndex] != vIndex) {
                            	uIndex = P[vIndex];
                                F[uIndex][vIndex] += M[tIndex];
                                F[vIndex][uIndex] -= M[tIndex];
                                v = u;
                                vIndex = uIndex;
                            }
                            break LOOP;
                        }
                    }
                }
            }
            if (P[tIndex] == -1) { // We did not find a path to t
                int sum = 0;
                for (int x : F[sIndex])
                    sum += x;
                return sum;
            }
        }
    }

	private int getCapacity(int uIndex, int vIndex, int[] gradients, byte player) {
		if((groups.get(uIndex).getPlayer() != NO_PLAYER) && (groups.get(uIndex).getPlayer() != player)){
			return 0;
		}
		if((groups.get(vIndex).getPlayer() != NO_PLAYER) && (groups.get(vIndex).getPlayer() != player)){
			return 0;
		}
		return (gradients[uIndex] + gradients[vIndex])/2 ;
	}
	
	
}
