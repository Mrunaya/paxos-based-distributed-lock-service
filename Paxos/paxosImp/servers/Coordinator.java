package paxosImp.servers;

import paxosImp.PaxosServerNodeImpl;
import paxosImp.ParticipantSocket;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import paxosImp.CoordinatorClientSocket;
import paxosImp.CoordinatorServerSocket;

public class Coordinator{
    private static final int clientPort = 8080;
    private static final int paxosPort = 8081;
    private PaxosServerNodeImpl paxosServerNode;
    
    private static final HashMap<Integer, String> mapOfString = new HashMap<Integer, String>();
    
    public static void main(String[] args) {
    	System.out.println("Starting Coordinator");
    	PaxosServerNodeImpl paxosServerNodeImpl = new PaxosServerNodeImpl(1);
    	
    	CoordinatorClientSocket coordinatorClientSocket = new CoordinatorClientSocket(paxosServerNodeImpl, clientPort, paxosPort);
    	CoordinatorServerSocket coordinatorServerSocket = new CoordinatorServerSocket(paxosServerNodeImpl, clientPort, paxosPort);
        
    	System.out.println("Starting threads for coordinator");
        
        Thread coordinatorClientThread = new Thread(coordinatorClientSocket);
        Thread coordinatorServerThread = new Thread(coordinatorServerSocket);
         
        coordinatorServerThread.start();
        coordinatorClientThread.start();
        
    }
    
 
} 
