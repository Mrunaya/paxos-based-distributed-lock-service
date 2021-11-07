package paxosImp.servers;

import paxosImp.PaxosServerNodeImpl;
import paxosImp.PaxosSocket;
import paxosImp.ClientSocket;

public class Server {
    private static final int clientPort = 8080;
    private static final int paxosPort = 8081;
    
    public static void main(String[] args) {
    	System.out.println("Starting server1");
    	PaxosServerNodeImpl paxosServerNodeImpl = new PaxosServerNodeImpl(1);
    	
    	ClientSocket clientSocket = new ClientSocket(paxosServerNodeImpl, clientPort, paxosPort);
        PaxosSocket paxosSocket = new PaxosSocket(paxosServerNodeImpl, clientPort, paxosPort);
        
    	System.out.println("Starting threads for server 1");
        
    	Thread clientThread = new Thread(clientSocket);
        Thread paxosThread = new Thread(paxosSocket);
        
        clientThread.start();
        paxosThread.start();
    }
} 