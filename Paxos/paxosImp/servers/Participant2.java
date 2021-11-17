package paxosImp.servers;

import paxosImp.CoordinatorClientSocket;
import paxosImp.PaxosServerNodeImpl;
import paxosImp.ParticipantSocket;

public class Participant2 {
    private static final int clientPort = 8084;
    private static final int paxosPort = 8085;
    private static final int balance = 1000;
    
    public static void main(String[] args) {
    	System.out.println("Starting participant2");
    	PaxosServerNodeImpl paxosServerNodeImpl = new PaxosServerNodeImpl(3);
    	
        ////ClientSocket clientSocket = new ClientSocket(paxosServerNodeImpl, clientPort, paxosPort);
        ParticipantSocket paxosSocket = new ParticipantSocket(paxosServerNodeImpl, clientPort, paxosPort, balance);
        
        System.out.println("Starting threads for Participant 2");
        
       //Thread clientThread = new Thread(clientSocket);
        Thread paxosThread = new Thread(paxosSocket);
        
        //clientThread.start();
        paxosThread.start();
    }
} 