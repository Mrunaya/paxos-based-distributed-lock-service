package paxosImp.dto;

public class ProposeResponse {

	private boolean valueAccepted;

	public boolean isValueAccepted() {
		return valueAccepted;
	}

	public void setValueAccepted(boolean valueAccepted) {
		this.valueAccepted = valueAccepted;
	}
	
	public ProposeResponse(boolean valueAccepted) {
		this.valueAccepted = valueAccepted;
	}
}
