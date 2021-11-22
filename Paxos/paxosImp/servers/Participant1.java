package paxosImp.servers;

import java.io.IOException;

import paxosImp.CoordinatorClientSocket;
import paxosImp.PaxosServerNodeImpl;
import paxosImp.ParticipantSocket;

public class Participant1 {
    private static final int clientPort = 8082;
    private static final int paxosPort = 8083;
    private static final int balance = 1000;
    
    public static void main(String[] args) throws IOException {
    	System.out.println("Starting Participant 1");
    	PaxosServerNodeImpl paxosServerNodeImpl = new PaxosServerNodeImpl(2);
    	
        //ClientSocket clientSocket = new ClientSocket(paxosServerNodeImpl, clientPort, paxosPort);
        ParticipantSocket paxosSocket = new ParticipantSocket(paxosServerNodeImpl, clientPort, paxosPort, balance);
        
        System.out.println("Starting threads for Participan 1");
        
        //Thread clientThread = new Thread(clientSocket);
        Thread paxosThread = new Thread(paxosSocket);
        
        //clientThread.start();
        paxosThread.start();
    }
} 