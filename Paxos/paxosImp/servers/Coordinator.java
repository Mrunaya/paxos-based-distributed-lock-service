package paxosImp.servers;

import paxosImp.PaxosServerNodeImpl;
import paxosImp.PaxosSocket;

import java.util.HashMap;

import paxosImp.ClientSocket;

public class Coordinator {
    private static final int clientPort = 8080;
    private static final int paxosPort = 8081;
    private static final int balance = 1000;
    
    private static final HashMap<Integer, String> mapOfString = new HashMap<Integer, String>();
    
    public static void main(String[] args) {
    	System.out.println("Starting server1");
    	PaxosServerNodeImpl paxosServerNodeImpl = new PaxosServerNodeImpl(1);
    	
    	ClientSocket clientSocket = new ClientSocket(paxosServerNodeImpl, clientPort, paxosPort);
        PaxosSocket paxosSocket = new PaxosSocket(paxosServerNodeImpl, clientPort, paxosPort, balance);
        
    	System.out.println("Starting threads for coordinator");
        
    	Thread clientThread = new Thread(clientSocket);
        Thread paxosThread = new Thread(paxosSocket);
        
        clientThread.start();
        paxosThread.start();
    }
} 