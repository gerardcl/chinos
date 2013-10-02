package edu.upc.eetac.dxat.gerardcl.chinos.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ChinosGame {
	private class PlayerThread implements Runnable{

		private Player player;
		private BufferedReader reader;
		private PrintWriter writer;
		
		public PlayerThread(Player player) throws IOException {
			super();
			this.player = player;
			this.reader = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
			this.writer = new PrintWriter(player.getSocket().getOutputStream()); 
		}
		public Player getPlayer() {
			return player;
		}
		public void sendWait(){
			this.writer.println("WAIT");
			this.writer.flush();
		}
		public void sendVersus(String name){
			this.writer.println("VERSUS "+name);
			this.writer.flush();
		}
		public void sendYourBet(){
			this.writer.println("YOUR BET");
			this.writer.flush();
		}
		public void sendWaitBet(){
			this.writer.println("WAIT BET");
			this.writer.flush();
		}
		public void run() {
			while(!player.getSocket().isClosed()){
				try {
					String response = reader.readLine();
					//TODO 
					
				} catch (IOException e) {
					if(!player.getSocket().isClosed())
						e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	
	private PlayerThread[] playersThread;

	public ChinosGame() {
		super();
		this.playersThread = new PlayerThread[2]; 
	}

	public void addPlayer(Player player) throws IOException{
		this.playersThread[player.getPosition()] = new PlayerThread(player);
		new Thread(playersThread[player.getPosition()]).start();;
		if(player.getPosition() == 0)
			playersThread[0].sendWait();
		if(player.getPosition() == 1){
			playersThread[0].sendVersus(playersThread[1].getPlayer().getName());
			playersThread[1].sendVersus(playersThread[0].getPlayer().getName());
			playersThread[0].sendYourBet();
			playersThread[1].sendWaitBet();
		}
	}
	

}
