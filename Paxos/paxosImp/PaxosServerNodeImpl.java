package paxosImp;

import java.io.IOException;
import java.net.ServerSocket;

import paxosImp.dto.PrepareResponse;
import paxosImp.dto.ProposeResponse;

public class PaxosServerNodeImpl implements PaxosServerNode{
	
	int proposalID = 0;
	int maxId = 0;
	int promise = 0;
	boolean proposal_accepted = false;
	
	@Override
	public void prepare() {
		proposalID = proposalID++;
		proposal_accepted = sendPrepareToAcceptors(proposalID);
	}
	
	@Override
	public void propose(int proposalID, String value) {
		if (proposal_accepted) {
			//send propose message to all acceptors
		}
	}

	@Override
	public PrepareResponse respondPrepare(int proposalID) {
		PrepareResponse response;
		if (maxId < proposalID) {
			// i am ready
			response = new PrepareResponse(true);
		} else {
			// i am not
			response = new PrepareResponse(false);
		}
		return response;
	}

	@Override
	public ProposeResponse respondPropose(int proposalID, String value) {
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

	
	private void accept() {
		// TODO Send the updated value to learner
		
	}

	private boolean sendPrepareToAcceptors(int proposalID2) {
		/*
		 * 
		 * respponse from all the acceptors 
		 * TODO remove this afterwards
		 * */
		PrepareResponse response = new PrepareResponse(false);
		
		return response.isReady();
		
	}
}
