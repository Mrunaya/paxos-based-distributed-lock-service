package paxosImp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import paxosImp.dto.PrepareResponse;


class Socket5 extends PaxosServerNodeImpl implements Runnable
{
    public void run()
    {
        try
        {
            ServerSocket serverSock = new ServerSocket(8084);
            while(true)
            {
                Socket sock = serverSock.accept();
                ObjectInputStream inputStreamFromClient = new ObjectInputStream(sock.getInputStream());
        		
        		String command = (String) inputStreamFromClient.readObject();
        		System.out.println(command);
        		Socket socket1 = new Socket("localhost", 8081);
    			Socket socket2 = new Socket("localhost", 8083);
    			Socket socket3 = new Socket("localhost", 8085);
    			
    			ObjectOutputStream outputStream1 = new  ObjectOutputStream(socket1.getOutputStream());
    			ObjectOutputStream outputStream2 = new  ObjectOutputStream(socket2.getOutputStream());
    			ObjectOutputStream outputStream3 = new  ObjectOutputStream(socket3.getOutputStream());
    			outputStream1.writeObject("server 1 writing");
    			outputStream2.writeObject("server 2 writing");
    			outputStream3.writeObject("server 3 writing");
            }
        }
        catch(Exception e)
        {
            System.out.println("Error5");
        }
    }
}

class Socket6 extends PaxosServerNodeImpl implements Runnable
{
    public void run()
    {
        try
        {
            ServerSocket serverSock = new ServerSocket(8085);
            while(true)
            {
            	 Socket sock = serverSock.accept();
                 
                 ObjectInputStream inputStreamFromClient = new ObjectInputStream(sock.getInputStream());
                 
         		HashMap accepter = (HashMap) inputStreamFromClient.readObject();
         		System.out.println("The proposalID for 8085 is "+accepter.get("proposalID"));
         		PrepareResponse  response=respondPrepare((int) accepter.get("proposalID"));
         		
         		//acceptor sends the response to proposer
         		Socket proposerSocket = new Socket("localhost", (int) accepter.get("Proposer"));
     			ObjectOutputStream outputStream1 =new  ObjectOutputStream(proposerSocket.getOutputStream());
     			accepter.clear();
        		accepter.put("Response", response.isReady());
     			outputStream1.writeObject(accepter);

            }
        }
        catch(Exception e)
        {
            System.out.println("Error6");
        }
    }
}

public class Server3  extends PaxosServerNodeImpl
{
    public static void main(String[] args)
    {
       
        Socket5 s5=new Socket5();
        Socket6 s6=new Socket6();
        
        Thread t5=new Thread(s5);
        Thread t6=new Thread(s6);
       
        t5.start();
        t6.start();
    }
} 