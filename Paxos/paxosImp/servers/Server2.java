package paxosImp.servers;

import paxosImp.ClientSocket;
import paxosImp.PaxosServerNodeImpl;
import paxosImp.PaxosSocket;

public class Server2 {
    private static final int clientPort = 8082;
    private static final int paxosPort = 8083;
    
    public static void main(String[] args) {
    	System.out.println("Starting server2");
    	PaxosServerNodeImpl paxosServerNodeImpl = new PaxosServerNodeImpl(2);
    	
        ClientSocket clientSocket = new ClientSocket(paxosServerNodeImpl, clientPort, paxosPort);
        PaxosSocket paxosSocket = new PaxosSocket(paxosServerNodeImpl, clientPort, paxosPort);
        
        System.out.println("Starting threads for server 2");
        
        Thread clientThread = new Thread(clientSocket);
        Thread paxosThread = new Thread(paxosSocket);
        
        clientThread.start();
        paxosThread.start();
    }
} 