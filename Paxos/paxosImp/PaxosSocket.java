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
    private int balance;
	private PaxosServerNodeImpl paxosServerNode;
	 
    public PaxosSocket(PaxosServerNodeImpl paxosServerNodeImpl, int clientPort, int paxosport, int balance) {
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
        	ClientSocket cl = new ClientSocket(this.paxosServerNode, clientPort, paxosPort);
        	int respondedNodesForPrepare = 0;
        	int respondedNodesForPropose = 0;
        	while(true)
        	{
        		Socket socket = serverSock.accept();

        		ObjectInputStream inputStreamFromClient = new ObjectInputStream(socket.getInputStream());
        		HashMap<String, Object> message = (HashMap) inputStreamFromClient.readObject();


        		if(message.containsKey("Prepare")) {
        			PrepareResponse  response = paxosServerNode.respondPrepare((int) message.get("PropsalId"));
        			
        			//Participants sends the response to coordinator
        			Socket proposerSocket = new Socket("localhost", (int) message.get("CoordinatorPort"));
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(proposerSocket.getOutputStream());
        			message = new HashMap<>();
        			
        			message.put("Response", response.isReady());
        			
        			outputStream1.writeObject(message);
        			

        		} else if(message.containsKey("Response")) {
        			if((boolean) message.get("Response")) {
        				message = new HashMap<>();
        				respondedNodesForPrepare++;
        			}

        			if(respondedNodesForPrepare == 2) {
        				System.out.println("Responces received from all participants. Phase 1 compleleted. ");
        				paxosServerNode.commit(paxosServerNode.transactionID, paxosServerNode.getPropsedValue(), paxosPort);

        			}else {
        				System.out.println("Responces received from all participants. Aborting the transaction ");
        				//request discarded so server has to create new request with greater proposalID
        			}


        		}
        		else if(message.containsKey("ProposeSender")) {
        			ProposeResponse  response = paxosServerNode.respondCommit((int)message.get("PropsalId"),(String)message.get("ValueToAccept"));
        			
        			//acceptor sends the response to proposer
        			Socket proposerSocket = new Socket("localhost", (int) message.get("ProposeSender"));
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(proposerSocket.getOutputStream());
        			message = new HashMap<>();
        			
        			message.put("Action", response.isValueAccepted());
        			
        			outputStream1.writeObject(message);
        			
        			proposerSocket.close();
        			

        		}else if(message.containsKey("Action")) {
        			if((boolean) message.get("Action")) {
        				respondedNodesForPropose++;
        				if(respondedNodesForPropose == 2) {
            				
        					System.out.println("Transaction is commited");
        					paxosServerNode.accept(paxosServerNode.transactionID, paxosServerNode.getPropsedValue(), paxosPort);

            			}else {
            				System.out.println("Transaction is commited");
            				//request discarded so server has to create new request with greater proposalID
            			}
        				
        			}
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