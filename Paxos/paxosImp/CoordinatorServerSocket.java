package paxosImp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
public class CoordinatorServerSocket implements Runnable {
    
	private int clientPort;
    private int paxosPort;
    private int balance;
	private PaxosServerNodeImpl paxosServerNode;
	FileOutputStream fos;
	 
    public CoordinatorServerSocket(PaxosServerNodeImpl paxosServerNodeImpl, int clientPort, int paxosport) throws IOException {
    	this.paxosServerNode = paxosServerNodeImpl;
		this.clientPort = clientPort;
		this.paxosPort = paxosport;
		this.balance = balance;
		fos = new FileOutputStream("Coordinator_log_" + clientPort +".txt", true);
		writeToLog("START_2PC");
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

            	
        			if(messageFromParticipant.containsKey("VoteCommit")) {
        				if((boolean) messageFromParticipant.get("VoteCommit")) {
        					messageFromParticipant = new HashMap<>();
        					respondedNodesForPrepare++;
        				}
        				//wait for incoming responce from all participants
        				// if timeout write golabl abort and multicast
        				else if(!(boolean) messageFromParticipant.get("VoteCommit")) {
        					System.out.println("Responces received from all participants. Aborting the transaction ");
        					writeToLog("GLOBAL_COMMIT");
         					paxosServerNode.globalCommitOrAbort(paxosServerNode.transactionID, "Abort", paxosPort);

        				}

        				if(respondedNodesForPrepare == 2) {
        					System.out.println("Responces received from all participants. Phase 1 compleleted. ");
        					writeToLog("GLOBAL_COMMIT");
        					paxosServerNode.globalCommitOrAbort(paxosServerNode.transactionID, "Commit", paxosPort);
        				}
        			}
        		    
        		 
            	
        		
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
            System.out.println("Error2");
        } finally {
        	try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
	
	private void writeToLog(String log) throws IOException {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String logStmt = "\n" + timeStamp +" - " + log;
		
	    fos.write(logStmt.getBytes());
	}
}