package paxosImp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import paxosImp.dto.PrepareResponse;
import paxosImp.dto.ProposeResponse;
public class CoordinatorServerSocket implements Runnable {
    
	private int clientPort;
    private int paxosPort;
    private int balance;
	private PaxosServerNodeImpl paxosServerNode;
	 
    public CoordinatorServerSocket(PaxosServerNodeImpl paxosServerNodeImpl, int clientPort, int paxosport) {
    	this.paxosServerNode = paxosServerNodeImpl;
		this.clientPort = clientPort;
		this.paxosPort = paxosport;
		this.balance = balance;
	}

	public void run() {
        try
        {
        	
        	System.out.println("PaxosSocket Thread Started. Paxos Port : " + paxosPort);
        	ServerSocket serverSock = new ServerSocket(paxosPort);
        	int respondedNodesForPrepare = 0;
        	int respondedNodesForPropose = 0;
        	while(true)
        	{
        		Socket socket = serverSock.accept();

        		ObjectInputStream inputStreamFromClient = new ObjectInputStream(socket.getInputStream());
        		HashMap<String, Object> messageFromParticipant = (HashMap) inputStreamFromClient.readObject();

            	
        			if(messageFromParticipant.containsKey("ResponsePrepare")) {
        				if((boolean) messageFromParticipant.get("ResponsePrepare")) {
        					messageFromParticipant = new HashMap<>();
        					respondedNodesForPrepare++;
        				}
        				else if(!(boolean) messageFromParticipant.get("Response")) {
        					System.out.println("Responces received from all participants. Aborting the transaction ");
        					//request discarded so server has to create new request with greater proposalID
        				}

        				if(respondedNodesForPrepare == 2) {
        					System.out.println("Responces received from all participants. Phase 1 compleleted. ");
        					paxosServerNode.voteCommit(paxosServerNode.transactionID, paxosServerNode.getPropsedValue(), paxosPort);

        				}
        			}
        		    
        		    else if(messageFromParticipant.containsKey("Action")) {
        				if((boolean) messageFromParticipant.get("Action")) {
        					respondedNodesForPropose++;
        				}
        				else if(!(boolean) messageFromParticipant.get("Action")) {
        					System.out.println("Transaction is Aborted");
        					paxosServerNode.accept(paxosServerNode.transactionID, "Glabal Abort", paxosPort);
        					//request discarded so server has to create new request with greater proposalID
        				}
        				if(respondedNodesForPropose == 2) {
        					System.out.println("Transaction is commited");
        					paxosServerNode.accept(paxosServerNode.transactionID, "Glabal Commit", paxosPort);
        				}
        					
        				
        			}
            	
        		
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
            System.out.println("Error2");
        }
    }
}