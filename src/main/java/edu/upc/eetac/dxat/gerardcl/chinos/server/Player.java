package edu.upc.eetac.dxat.gerardcl.chinos.server;

import java.net.Socket;

public class Player {
	private Socket socket;
	private String name;
	private int position;
	private int coins;		//las que tiene en la mano
	private int totalCoins;	//las que predice
	
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
		//System.out.println("Apuesto que hay un total de "+ totalCoins +" monedas");
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
	public void setCoins(int coins) {
		//System.out.println("Me juego "+ coins +" monedas");
		this.coins = coins;
	}
}
