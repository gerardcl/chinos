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
		
		public void run() {
			writer.println("PLAY "+name);
			writer.flush();
			while(!socket.isClosed()){
				try {
					String response = reader.readLine();
					if(response.equals("WAIT"))
						System.out.println("Esperando al otro jugador...");
					if(response.equals("VERSUS")) 
						//TODO PARSEAR EL NOMBRE....
						System.out.println(response);
					if(response.equals("YOUR BET"))
						//TODO LEER POR CONSOLA LA APUESTA
						//TODO sendMyBet(coins,totalCoins);
						System.out.println("Ya puedes apostar...");
					//if(response.equals("WAIT BET"))
						//TODO asdf
						
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
