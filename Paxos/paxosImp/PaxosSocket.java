package paxosImp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import paxosImp.dto.PrepareResponse;
public class PaxosSocket implements Runnable {
    
	private int clientPort;
    private int paxosPort;
    
	private PaxosServerNodeImpl paxosServerNode;
	 
    public PaxosSocket(PaxosServerNodeImpl paxosServerNodeImpl, int clientport, int paxosport) {
    	this.paxosServerNode = paxosServerNodeImpl;
		this.clientPort = clientport;
		this.paxosPort = paxosport;
	}

	public void run() {
        try
        {
        	System.out.println("PaxosSocket Thread Started. Paxos Port : " + paxosPort);
        	ServerSocket serverSock = new ServerSocket(paxosPort);
        	int respondedNodes = 0;
        	while(true)
        	{
        		Socket socket = serverSock.accept();

        		ObjectInputStream inputStreamFromClient = new ObjectInputStream(socket.getInputStream());
        		HashMap<String, Object> message = (HashMap) inputStreamFromClient.readObject();


        		if(message.containsKey("PropsalId")) {
        			System.out.println("The proposalID for 8081 is "+message.get("PropsalId"));
        			PrepareResponse  response = paxosServerNode.respondPrepare((int) message.get("PropsalId"));
        			
        			//acceptor sends the response to proposer
        			Socket proposerSocket = new Socket("localhost", (int) message.get("Sender"));
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(proposerSocket.getOutputStream());
        			message = new HashMap<>();
        			
        			message.put("Response", response.isReady());
        			
        			outputStream1.writeObject(message);

        		} else {
        			if((boolean) message.get("Response")) {
        				message.clear();
        				respondedNodes++;
        			}
        			if(respondedNodes > 2) {
    
                		
        			}else {
        				//request discarded so server has to create new request with greater proposalID
        			}
        			

        		}
        		System.out.println("respondedNodes 8081 is "+respondedNodes);
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
            System.out.println("Error2");
        }
    }
}