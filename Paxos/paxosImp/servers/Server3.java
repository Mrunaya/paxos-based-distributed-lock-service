package paxosImp.servers;

import paxosImp.ClientSocket;
import paxosImp.PaxosServerNodeImpl;
import paxosImp.PaxosSocket;

public class Server3 {
    private static final int clientPort = 8084;
    private static final int paxosPort = 8085;
    
    public static void main(String[] args) {
    	System.out.println("Starting server3");
    	PaxosServerNodeImpl paxosServerNodeImpl = new PaxosServerNodeImpl(3);
    	
        ClientSocket clientSocket = new ClientSocket(paxosServerNodeImpl, clientPort, paxosPort);
        PaxosSocket paxosSocket = new PaxosSocket(paxosServerNodeImpl, clientPort, paxosPort);
        
        System.out.println("Starting threads for server 3");
        
        Thread clientThread = new Thread(clientSocket);
        Thread paxosThread = new Thread(paxosSocket);
        
        clientThread.start();
        paxosThread.start();
    }
} 