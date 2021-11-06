package paxosImp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client2 {
	public static void main( String[] args ) throws IOException
    {
		try (Socket socket = new Socket("localhost", 8081)) {
			System.out.println("connected to server");
			
			
			ObjectOutputStream outputStream =
					new  ObjectOutputStream(socket.getOutputStream());
	
			outputStream.writeObject("world");
		}
    }
}
