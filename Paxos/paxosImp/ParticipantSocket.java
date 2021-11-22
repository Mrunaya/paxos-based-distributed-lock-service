package paxosImp;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import paxosImp.dto.PrepareResponse;
import paxosImp.dto.ProposeResponse;
public class ParticipantSocket implements Runnable {
    
	private int clientPort;
    private int paxosPort;
    private int balance;
	private PaxosServerNodeImpl paxosServerNode;
	FileOutputStream fos;
	
    public ParticipantSocket(PaxosServerNodeImpl paxosServerNodeImpl, int clientPort, int paxosport, int balance) throws IOException {
    	this.paxosServerNode = paxosServerNodeImpl;
		this.clientPort = clientPort;
		this.paxosPort = paxosport;
		this.balance = balance;
		fos = new FileOutputStream("Participant_log_" + clientPort +".txt", true);
	    writeToLog("INIT");
	}

	@SuppressWarnings("resource")
	public void run() {
        try
        {
        	
        	System.out.println("PaxosSocket Thread Started. Paxos Port : " + paxosPort);
        	ServerSocket serverSock = new ServerSocket(paxosPort);
        	while(true)
        	{
        		Socket socket = serverSock.accept();

        		ObjectInputStream inputStreamFromClient = new ObjectInputStream(socket.getInputStream());
        		HashMap<String, Object> message = (HashMap) inputStreamFromClient.readObject();


        		if(message.containsKey("VoteRequest")) {
        			PrepareResponse  response = paxosServerNode.respondVoteRequest();
        			
        			//Participants sends the response to coordinator
        			Socket proposerSocket = new Socket("localhost", (int) message.get("CoordinatorPort"));
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(proposerSocket.getOutputStream());
        			message = new HashMap<>();
        			
        			//wait for vite request ; if timeout send vote abort
        			message.put("VoteCommit", response.isReady());
        			
        			outputStream1.writeObject(message);
        			if(response.isReady()) {
        			writeToLog("VOTE_COMMIT");
        			}else if(!response.isReady()) {
        				writeToLog("VOTE_ABORT");
        			}
        			
        		} 
        		//if no gloabl commit from corrdinator within time 
        		else if(false) {
        			Socket participant2Socket = new Socket("localhost", 8085);
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(participant2Socket.getOutputStream());
        			message = new HashMap<>();
        			//wait for vite request ; if timeout send vote abort
        			message.put("Coordinator Inactive","result");
        			
        			outputStream1.writeObject(message);
        		}
        		else if(message.containsKey("Coordinator Inactive")) {
        			Socket participant1Socket = new Socket("localhost", 8083);
        			
        			ObjectOutputStream outputStream1 = new  ObjectOutputStream(participant1Socket.getOutputStream());
        			message = new HashMap<>();
        			//check log and send the if got responce from coordinator
        			message.put("ParticipantResult","LogResult");
        			
        			outputStream1.writeObject(message);
        		}
        		else if(message.containsKey("ParticipantResult")) {
        			if(message.get("ParticipantResult").equals("Commit")) {
        				writeToLog("VOTE_COMMIT");
        			}else {
        				writeToLog("VOTE_ABORT");
        			}
        		}
        		else if(message.containsKey("GlobalCommitOrAbort")) {
        			if(message.get("GlobalCommitOrAbort").equals("Commit")) {
        				writeToLog("GLOBAL_COMMIT");
        			}else {
        				writeToLog("GLOBAL_ABORT");
        			}
        			
        			
        			//write to file

        		}
        		else if(message.containsKey("CoordinatorMessage")) {
        			System.out.println("Transaction Commited !!");
        				
        			
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