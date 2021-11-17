package paxosImp;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("resource")
public class CoordinatorClientSocket implements Runnable {
    private int clientPort;
    private int paxosPort;
    private PaxosServerNodeImpl paxosServerNode;
    
	public CoordinatorClientSocket(PaxosServerNodeImpl paxosServerNodeImpl, int clientport, int paxosport) {
		this.paxosServerNode = paxosServerNodeImpl;
		this.clientPort = clientport;
		this.paxosPort = paxosport;
		
	}

	public void run()
    {
		System.out.println("ClientSocket Thread Started. Port : " + clientPort);
        try {
			ServerSocket serverSock = new ServerSocket(clientPort);
        	while(true) {
        		
        		Socket sock = serverSock.accept();
        		ObjectInputStream inputStreamFromClient = new ObjectInputStream(sock.getInputStream());

        		String valueToAccept = (String) inputStreamFromClient.readObject();
        		System.out.println(valueToAccept);
        		paxosServerNode.setPropsedValue(valueToAccept);
        		paxosServerNode.voteRequest(paxosPort); 
        	}
        	
        }
        catch(Exception e)
        {
            System.out.println("Error1");
        }
    }
}