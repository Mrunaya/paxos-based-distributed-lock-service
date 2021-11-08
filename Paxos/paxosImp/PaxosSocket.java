package paxosImp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import paxosImp.dto.PrepareResponse;
import paxosImp.dto.ProposeResponse;
public class PaxosSocket implements Runnable {
    
	private int clientPort;
    private int paxosPort;
    
	private PaxosServerNodeImpl paxosServerNode;
	 
    public PaxosSocket(PaxosServerNodeImpl paxosServerNodeImpl, int clientPort, int paxosport) {
    	this.paxosServerNode = paxosServerNodeImpl;
		this.clientPort = clientPort;
		this.paxosPort = paxosport;
	}

	public void run() {
        try
        {
        	System.out.println("PaxosSocket Thread Started. Paxos Port : " + paxosPort);
        	ServerSocket serverSock = new ServerSocket(paxosPort);
        	ClientSocket cl = new ClientSocket(this.paxosServerNode, clientPort, paxosPort);
        	int respondedNodesForPrepare = 0;
        	int respondedNodesForPropose = 0;
        	while(true)
        	{
        		Socket socket = serverSock.accept();

        		ObjectInputStream inputStreamFromClient = new ObjectInputStream(socket.getInputStream());
        		HashMap<String, Object> message = (HashMap) inputStreamFromClient.readObject();


        		if(message.containsKey("PrepareSender")) {
        			PrepareResponse  response = paxosServerNode.respondPrepare((int) message.get("PropsalId"));
        			
        			//acceptor sends the response to proposer
        			Socket proposerSocket = new Socket("localhost", (int) message.get("PrepareSender"));
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(proposerSocket.getOutputStream());
        			message = new HashMap<>();
        			
        			message.put("Response", response.isReady());
        			
        			outputStream1.writeObject(message);
        			

        		} else if(message.containsKey("Response")) {
        			if((boolean) message.get("Response")) {
        				message = new HashMap<>();
        				respondedNodesForPrepare++;
        			}

        			if(respondedNodesForPrepare > 2) {
        				paxosServerNode.propose(paxosServerNode.proposalID, paxosServerNode.getPropsedValue(), paxosPort);

        			}else {
        				//request discarded so server has to create new request with greater proposalID
        			}


        		}
        		else if(message.containsKey("ProposeSender")) {
        			ProposeResponse  response = paxosServerNode.respondPropose((int)message.get("PropsalId"),(int)message.get("ValueToAccept"));
        			
        			//acceptor sends the response to proposer
        			Socket proposerSocket = new Socket("localhost", (int) message.get("ProposeSender"));
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(proposerSocket.getOutputStream());
        			message = new HashMap<>();
        			
        			message.put("Consensus", response.isValueAccepted());
        			
        			outputStream1.writeObject(message);
        			
        			proposerSocket.close();
        			

        		}else if(message.containsKey("Consensus")) {
        			if((boolean) message.get("Consensus")) {
        				respondedNodesForPropose++;
        				if(respondedNodesForPropose > 2) {
            				
        					System.out.println("Consensus reached on value "+paxosServerNode.getPropsedValue()+" !!");

            			}else {
            				//request discarded so server has to create new request with greater proposalID
            			}
        				
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