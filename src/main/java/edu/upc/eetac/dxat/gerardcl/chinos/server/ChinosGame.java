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
		public void sendBetOf(String bet){
			this.writer.println("BET OF "+player.getName()+" "+bet);
			this.writer.flush();
		}
		public void sendWinner(String winner){
			this.writer.println("AND THE WINNER IS.... "+winner);
			this.writer.flush();
		}
		public void sendNoneWins(/*String p1, int c1, int b1, String p2, int c1, int b2*/){
			this.writer.println("NONE WINS!!!");
			this.writer.flush();
		}
		public void run() {
			while(!player.getSocket().isClosed()){
				try {
					String response = reader.readLine();
					if(response.startsWith("MY BET")){
						System.out.println("EMPEZANDO APUESTAS!!!");
						String rx_numbers = response.substring("MY BET ".length(), response.length());
						String rx_bet[] = rx_numbers.split(" ");
						System.out.println("Recibido de : "+ player.getName() +"\n\tSe juega: "+rx_bet[0]+"\n\tApuesta: "+rx_bet[1]);
						player.setCoins(Integer.valueOf(rx_bet[0]));
						player.setTotalCoins(Integer.valueOf(rx_bet[1]));
						if(player.getPosition()==0){
							playersThread[0].sendBetOf(rx_bet[1]);
							playersThread[1].sendYourBet();
							playersThread[0].sendWaitBet();
						}else{
							playersThread[1].sendBetOf(rx_bet[1]);
							int actualCoins = playersThread[1].getPlayer().getCoins() + playersThread[0].getPlayer().getCoins();
							if(actualCoins == playersThread[0].getPlayer().getTotalCoins()){
								playersThread[0].sendWinner(playersThread[0].getPlayer().getName());
								playersThread[1].sendWinner(playersThread[0].getPlayer().getName());
							}else if(actualCoins == playersThread[1].getPlayer().getTotalCoins()){
								playersThread[0].sendWinner(playersThread[1].getPlayer().getName());
								playersThread[1].sendWinner(playersThread[1].getPlayer().getName());
							}else{
								playersThread[0].sendNoneWins();
								playersThread[1].sendNoneWins();
							}
							playersThread[0].getPlayer().getSocket().close();
							playersThread[1].getPlayer().getSocket().close();
							
						}
					}else{
						
					}
				} catch (IOException e) {
					if(!player.getSocket().isClosed())
						e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	
	public PlayerThread[] playersThread;

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
