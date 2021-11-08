package paxosImp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import paxosImp.dto.PrepareResponse;
import paxosImp.dto.ProposeResponse;

public class PaxosServerNodeImpl implements PaxosServerNode {
	
	
	int paxosNodeId;
	int proposalID = 0;
	int maxId = 0;
	int promise = 0;
	
	Integer currentKey = null;
	Integer propsedValue = null;
	
	boolean proposal_accepted = false;
	
	public PaxosServerNodeImpl(int i) {
		this.paxosNodeId = i;
	}

	@Override
	public void prepare(int propsalPort) throws UnknownHostException, IOException {

		proposalID = proposalID + 1;
		sendPrepareToAcceptors(propsalPort);
	}
	
	@Override
	public void propose(int proposalID, int value, int propsalPort) throws UnknownHostException, IOException{
		HashMap<String, Object> message = new HashMap<>();
		message.put("PropsalId", proposalID);
		message.put("ProposeSender", propsalPort);
		message.put("ValueToAccept", value);
		
		print("PropsalId is "+ proposalID);
		broadcastMessageToAllNodes(message);
	}

	@Override
	public PrepareResponse respondPrepare(int proposalID) {
		PrepareResponse response;
		
		print("proposalID is "+ proposalID);
		print("maxId is "+ proposalID);
		
		if (maxId < proposalID) {
			// i am ready
			print("response is ready ");
			response = new PrepareResponse(true);
		} else {
			// i am not
			print("response is not ready ");
			response = new PrepareResponse(false);
		}
		return response;
	}

	@Override
	public ProposeResponse respondPropose(int proposalID, int value) {
		ProposeResponse response;
		if (maxId < proposalID) {
			//accept this value;
			response = new ProposeResponse(true);
			accept();
		} else {
			// didn't accept the value
			response = new ProposeResponse(false);
		}
		
		return response;
	}
	
	public Integer getCurrentKey() {
		return currentKey;
	}

	public void setCurrentKey(Integer currentKey) {
		this.currentKey = currentKey;
	}
	
	public Integer getPropsedValue() {
		return this.propsedValue;
	}

	public void setPropsedValue(Integer propsedValue) {
		this.propsedValue = propsedValue;
	}
	
	public void clearCurrentValue() {
		this.currentKey = null;
		this.propsedValue = null;
	}

	
	private void accept() {
		// TODO Send the updated value to learner
		
	}

	private void sendPrepareToAcceptors(int propsalPort) throws UnknownHostException, IOException {
		HashMap<String, Object> message = new HashMap<>();

		message.put("PropsalId", proposalID);
		message.put("PrepareSender", propsalPort);
		
		print("PropsalId is "+ proposalID);
		broadcastMessageToAllNodes(message);
		
	}
	
	private void broadcastMessageToAllNodes(HashMap<String, Object> message) throws IOException {
		Socket socket1 = new Socket("localhost", 8081);
		Socket socket2 = new Socket("localhost", 8083);
		Socket socket3 = new Socket("localhost", 8085);
		
		ObjectOutputStream outputStream1 = new ObjectOutputStream(socket1.getOutputStream());
		ObjectOutputStream outputStream2 = new ObjectOutputStream(socket2.getOutputStream());
		ObjectOutputStream outputStream3 = new ObjectOutputStream(socket3.getOutputStream());

		outputStream1.writeObject(message);
		outputStream2.writeObject(message);
		outputStream3.writeObject(message);
		
	
		
	}
	
	private void print(String msg) {
		System.out.println("PaxosServer Id : " + this.paxosNodeId + " Message : " + msg);
	}
	
	
}
