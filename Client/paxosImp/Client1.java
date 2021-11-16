package paxosImp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client1 {
	public static void main( String[] args ) throws IOException
    {
		try {
			Socket socket = new Socket("localhost", 8080);
			System.out.println("connected to server");
			
			ObjectOutputStream outputStream = new  ObjectOutputStream(socket.getOutputStream());
	
			
				Scanner sc = new Scanner(System.in);
				while(true) {
					//USER COMMANDS BEGIN FROM HERE----->
					System.out.println("What service would you want to leverage?");
					System.out.println(" 1 = Deposit Amount \n 2 = Credit amount \n 3 = Show balance \n");

					String userCmd = sc.nextLine();
					String amount = "";
					if (!userCmd.equals("3")) {
						System.out.println("Please Enter the amount");	
						amount = sc.nextLine();
					} 


					switch (userCmd) {
					case "1": //  Deposit amount
						outputStream.writeObject("Deposite "+ amount);
						break;

					case "2": // Credit amount
						outputStream.writeObject("Credit "+ amount);
						break;

					case "3": // Show balance
						outputStream.writeObject("Show Balance");
						break;

				

					default:
						System.out.println("Invalid Command");
						break;
					}

				}
				
				
			
		}catch(Exception e) {
    	
    }
}
}
