package paxosImp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import paxosImp.dto.PrepareResponse;
import paxosImp.dto.ProposeResponse;
public class ParticipantSocket implements Runnable {
    
	private int clientPort;
    private int paxosPort;
    private int balance;
	private PaxosServerNodeImpl paxosServerNode;
	 
    public ParticipantSocket(PaxosServerNodeImpl paxosServerNodeImpl, int clientPort, int paxosport, int balance) {
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
        		HashMap<String, Object> message = (HashMap) inputStreamFromClient.readObject();


        		if(message.containsKey("Prepare")) {
        			PrepareResponse  response = paxosServerNode.respondVoteRequest((int) message.get("TransactionID"));
        			
        			//Participants sends the response to coordinator
        			Socket proposerSocket = new Socket("localhost", (int) message.get("CoordinatorPort"));
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(proposerSocket.getOutputStream());
        			message = new HashMap<>();
        			
        			message.put("ResponsePrepare", response.isReady());
        			
        			outputStream1.writeObject(message);
        			

        		} 
        		else if(message.containsKey("voteCommit")) {
        			ProposeResponse  response = paxosServerNode.respondCommit((int)message.get("TransactionID"));
        			
        			//acceptor sends the response to proposer
        			Socket CoordinatorPort = new Socket("localhost", (int) message.get("CoordinatorPort"));
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(CoordinatorPort.getOutputStream());
        			message = new HashMap<>();
        			
        			message.put("Action", response.isValueAccepted());
        			
        			outputStream1.writeObject(message);
        			
        			CoordinatorPort.close();
        			

        		}
        		else if(message.containsKey("CoordinatorMessage")) {
        			System.out.println("Transaction Commited !!");
        				
        			
        		}
        		 /*else if(message.containsKey("Consensus")) { 
        			if((boolean) message.get("Consensus")) {
        				respondedNodesForPropose++;
        				if(respondedNodesForPropose > 2) {
            				
        					System.out.println("Consensus reached on value "+paxosServerNode.getPropsedValue()+" !!");
        					paxosServerNode.accept(paxosServerNode.transactionID, paxosServerNode.getPropsedValue(), paxosPort);

            			}else {
            				//request discarded so server has to create new request with greater proposalID
            			}
        				
        			}
        		}else if(message.containsKey("ConsensusValue")) { 
        			String ConsensusValue = (String) message.get("ConsensusValue");
        			if(ConsensusValue.contains("Deposit")) {
        				int value =  Integer.parseInt(ConsensusValue.split(" ")[1]);
        				balance = balance + value;
        				System.out.println("balance is "+ balance);
        			}else if(ConsensusValue.contains("Credit")) {
        				int value =  Integer.parseInt(ConsensusValue.split(" ")[1]);
        				balance = balance - value;
        				System.out.println("balance is "+ balance);
        			}else if(ConsensusValue.contains("Show Balance")) {
        				
        			}
        		}*/
        		
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
            System.out.println("Error2");
        }
    }
}