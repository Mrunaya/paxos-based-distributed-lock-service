package paxosImp;

import java.io.IOException;
import java.net.UnknownHostException;

import paxosImp.dto.PrepareResponse;
import paxosImp.dto.ProposeResponse;

public interface PaxosServerNode {
	
	/* Phase 1 */
	
	/**
	 * Proposer send PREPARE to acceptor
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * */
	abstract void prepare(int propsalPort) throws UnknownHostException, IOException;
	
	/**
	 * Acceptor accepts the PREPARE call of prosper 
	 * Tells Proposer, that I am ready or not ready
	 * 
	 * @return PrepareResponse ready=true if ACCEPTORS recentproposalID is smaller than proposalID
	 * */
	abstract PrepareResponse respondPrepare(int proposalID);
	
	/* Phase 2 */
	
	/**
	 * Proposer send values if it gets positive response from maximum acceptors
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * */
	abstract void commit(int proposalID, String value, int propsalPort) throws UnknownHostException, IOException;
	
	/**
	 * Acceptor accepts the value of the proposer
	 * Tells Proposer, if I have accepted the value or not
	 * @return if value is accepted or not, tells proposer and if accepted also to learner
	 * */
	abstract ProposeResponse respondCommit(int proposalID, String value);

	

	
	
}
