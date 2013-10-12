package edu.upc.eetac.dxat.gerardcl.chinos.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChinosServer implements Runnable{

	private int port;
	private ServerSocket ssocket;
	private Thread mainThread;
	private int numClients;
	
	public static void main(String[] args) {
		ChinosServer server = new ChinosServer(12345);
		server.startServer();
	}

	public ChinosServer(int port) {
		super();
		this.port = port;
	}

	public void run() {
		try {
			ssocket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Server accepting connections...");
		ChinosGame currentGame = null;
		while(true){
			if((numClients%2)==0){
				currentGame = new ChinosGame();
			}
			try {
				Socket socket = ssocket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//carga una cadena finalizada en \n y command ya no lo lleva
				String command = reader.readLine(); 
				if(command.startsWith("PLAY")){
					String name = command.substring("PLAY ".length(), command.length());
					Player player = new Player(socket, name, numClients%2);
					currentGame.addPlayer(player);
					numClients++;
				}
//				PrintWriter writer = new PrintWriter(socket.getOutputStream());
//				writer.println("ECHO: "+command);
//				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void startServer(){
		//this: obj. de una classe que implemente la interface Runnable (en este caso nostros mismos)
		mainThread = new Thread(this, "Chinos Server main thread");  
		mainThread.start();
	}

}
