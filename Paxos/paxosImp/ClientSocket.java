package paxosImp;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("resource")
public class ClientSocket implements Runnable {
    private int clientPort;
    private int paxosPort;
    private PaxosServerNodeImpl paxosServerNode;
    
	public ClientSocket(PaxosServerNodeImpl paxosServerNodeImpl, int clientport, int paxosport) {
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

        		Integer valueToAccept = (Integer) inputStreamFromClient.readObject();
        		System.out.println(valueToAccept);
        		paxosServerNode.setPropsedValue(valueToAccept);
        		paxosServerNode.prepare(paxosPort);
        	}
        	
        }
        catch(Exception e)
        {
            System.out.println("Error1");
        }
    }
}