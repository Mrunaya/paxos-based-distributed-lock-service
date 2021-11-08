package paxosImp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client1 {
	public static void main( String[] args ) throws IOException
    {
		try {
			Socket socket = new Socket("localhost", 8080);
			System.out.println("connected to server");
			
			ObjectOutputStream outputStream = new  ObjectOutputStream(socket.getOutputStream());
	
			outputStream.writeObject(1);
			socket.close();
		
    }catch(Exception e) {
    	
    }
}
}
