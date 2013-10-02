package edu.upc.eetac.dxat.gerardcl.chinos.server;

import java.net.Socket;

public class Player {
	private Socket socket;
	private String name;
	private int position;
	private int coins;
	private int totalCoins;
	
	public Player(Socket socket, String name, int position) {
		super();
		this.socket = socket;
		this.name = name;
		this.position = position;
	}
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public int getTotalCoins() {
		return totalCoins;
	}
	public void setTotalCoins(int totalCoins) {
		this.totalCoins = totalCoins;
	}
	public String getName() {
		return name;
	}
	public int getPosition() {
		return position;
	}
	public int getCoins() {
		return coins;
	}
}
