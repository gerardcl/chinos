package edu.upc.eetac.dxat.gerardcl.chinos.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChinosClient {
	private class ChinosClientThread implements Runnable{
		private Socket socket;
		public BufferedReader reader;
		public PrintWriter writer;
		
		public ChinosClientThread(Socket socket) throws IOException {
			super();
			this.socket = socket;
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.writer = new PrintWriter(socket.getOutputStream()); 
		}
		
		private int readCoins(){
			System.out.println("Cuantas monedas juegas? (de 0 a 3 monedas...)");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input;
			int ncoins = 0;
			try {
				input = reader.readLine();
				ncoins = Integer.valueOf(input);
				if(!(ncoins >= 0 && ncoins <= 3)){
					System.out.println("Apuesta bien por favor...");
					ncoins = readCoins();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ncoins;
		}
		
		private int readBet(int coins){
			System.out.println("Cuantas monedas crees que habra en total? (de "+coins+" a 6 monedas...)");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input;
			int bet = 0;
			try {
				input = reader.readLine();
				bet = Integer.valueOf(input);
				if(!(bet >= coins && bet <= 6)){
					System.out.println("Apuesta bien por favor...");
					bet = readBet(coins);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bet;
		}
		
		private void sendMyBet(){
			int coins = readCoins();
			writer.println("MY BET "+coins+" "+readBet(coins));
			writer.flush();
		}
		
		public void run() {
			writer.println("PLAY "+name);
			writer.flush();
			while(!socket.isClosed()){
				try {
					String response = reader.readLine();
					if(response.equals("WAIT"))
						System.out.println("Esperando al otro jugador...");
					else if(response.startsWith("VERSUS")){
						String name = response.substring("VERSUS ".length(), response.length());
						System.out.println("Juegas contra "+name);
					}
					else if(response.equals("YOUR BET")){
						System.out.println("Ya puedes apostar...");
						sendMyBet();
					}
					else if(response.equals("WAIT BET")){
						System.out.println("Esperando apuesta del contrincante...");
					}	
					else if(response.startsWith("BET OF")){
						String data[] = response.substring("BET OF ".length(), response.length()).split(" ");
						System.out.println("El jugador "+data[0]+" apuesta que hay "+data[1]+" monedas...");
					}	
					else if(response.startsWith("AND")){
						System.out.println(response+" !!!");
						System.out.println("Nos vemos!");
						socket.close();
					}else{
						//not checking response to prevent error response handling, default is none wins and close...
						System.out.println("NONE WINS!!!");
						socket.close();
					}
				} catch (IOException e) {
					if(!socket.isClosed())
						e.printStackTrace();
				}
				
			}
		}
		
	}
	
	
	private String serverAddr;
	private int serverPort;
	private String name;
	
	public static void main(String[] args) {
		ChinosClient client = new ChinosClient();
		client.play();
	}
	
	private void readParameters(){
		//para configurar dinàmicamente todo
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input;
		System.out.println("Introduce el nombre o dir.IP del servidor [localhost]");
		try {
			input = reader.readLine();
			if(input.length()==0){
				serverAddr = "localhost";
			}else{
				serverAddr = input;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Introduce el puerto del servidor [12345]");
		try {
			input = reader.readLine();
			if(input.length()==0){
				serverPort = 12345;
			}else{
				int port = Integer.valueOf(input);
				serverPort = port;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Introduce tu nombre [gerardcl]");
		try {
			input = reader.readLine();
			if(input.length()==0){
				name = "gerardcl";
			}else{
				name = input;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\nCliente iniciado correctamente\n \tDirección servidor = "+ serverAddr+"\n\tPuerto servidor = "+serverPort+"\n\tNombre jugador = "+ name);
		
	}

	public void play(){
		readParameters();
		try {
			Socket socket = new Socket(serverAddr,serverPort);
			Thread th = new Thread(new ChinosClientThread(socket));
			th.start();
//			PrintWriter writer = new PrintWriter(socket.getOutputStream());
//			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			writer.println(name);
//			writer.flush();
//			String response = reader.readLine();
//			System.out.println(response);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
