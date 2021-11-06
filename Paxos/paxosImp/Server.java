package paxosImp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Socket1 implements Runnable
{
    public void run()
    {
        try
        {
            ServerSocket serverSock=new ServerSocket(8080);
            while(true)
            {
                Socket sock=serverSock.accept();
                ObjectInputStream inputStreamFromClient = 
        				new ObjectInputStream(sock.getInputStream());
        		
        		String command = (String) inputStreamFromClient.readObject();
        		System.out.println(command);
            }
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
    }
}

class Socket2 implements Runnable
{
    public void run()
    {
        try
        {
            ServerSocket serverSock=new ServerSocket(8081);
            while(true)
            {
                Socket sock=serverSock.accept();
                ObjectInputStream inputStreamFromClient = 
        				new ObjectInputStream(sock.getInputStream());
        		
        		String command = (String) inputStreamFromClient.readObject();
        		System.out.println(command);
            }
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
    }
}

public class Server
{
    public static void main(String[] args)
    {
        Socket1 s1=new Socket1();
        Socket2 s2=new Socket2();
        Thread t1=new Thread(s1);
        Thread t2=new Thread(s2);
        t1.start();
        t2.start();
    }
} 