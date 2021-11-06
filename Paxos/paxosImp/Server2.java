package paxosImp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 extends PaxosServerNodeImpl {
	
	/**
	 * 
	 * Accepts client requests
	 * */
	ServerSocket commandHandlerServer;
	/**
	 * 
	 * Paxos nodes requests
	 * 
	 * */
	ServerSocket paxosNodeServer;
	
	private int serverId;
	
	public Server2(int serverId, String[] clientPort, String[] serverPort) throws IOException {
		this.serverId = serverId;
		commandHandlerServer = new ServerSocket(Integer.parseInt(clientPort[0]));
		paxosNodeServer = new ServerSocket(Integer.parseInt(serverPort[0]));
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int serverId = Integer.parseInt(args[0]);
		String[] paxosNodeClientPorts = args[1].split(",");
		String[] paxosNodeServerPorts = args[2].split (",");
		
		//System.out.println("Bootstraping server " + serverId +  ", " + "ClientPort: " + clientPort + ", paxosNodeport: " + paxosNodePort);
		Server2 server = new Server2(serverId, paxosNodeClientPorts, paxosNodeServerPorts);
		//System.out.println("Server node in network 1 : " +paxosNodeListeningPorts[0]+" Server node in network 2 : "+paxosNodeListeningPorts[1]);
		
		System.out.println("Server Bootstraped");
		Socket clientSocket = null;
		Socket serverSocket = null;
		
		
		while (true) {//why?
			clientSocket = server.commandHandlerServer.accept();

			Socket socket1 = new Socket("localhost", 8081);
			Socket socket2 = new Socket("localhost", 8083);
			Socket socket3 = new Socket("localhost", 8085);
			
			ObjectOutputStream outputStream1 =new  ObjectOutputStream(socket1.getOutputStream());
			ObjectOutputStream outputStream2 =new  ObjectOutputStream(socket2.getOutputStream());
			ObjectOutputStream outputStream3 =new  ObjectOutputStream(socket3.getOutputStream());
			outputStream1.writeObject("server 1 writing");
			outputStream2.writeObject("server 2 writing");
			outputStream3.writeObject("server 3 writing");
			System.out.println("Client connected");
			break;
			
		}
		
		while (true) {
		serverSocket = server.paxosNodeServer.accept();
		System.out.println("Server connected");
		break;
		}
		
		
		
		ObjectInputStream inputStreamFromClient = 
				new ObjectInputStream(clientSocket.getInputStream());
		
		String command = (String) inputStreamFromClient.readObject();
		
		ObjectInputStream inputStreamFromServer = 
				new ObjectInputStream(serverSocket.getInputStream());
		
		String command1 = (String) inputStreamFromServer.readObject();
		
		System.out.println(command);
		System.out.println(command1);
    }
	
	
	
}
