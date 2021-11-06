package paxosImp.dto;

public class PrepareResponse {

	private boolean ready;

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public PrepareResponse(boolean ready) {
		this.ready = ready;
	}


}
