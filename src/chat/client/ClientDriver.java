package chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import chat.util.Checker;
import chat.util.ClientData;
import chat.util.Logger;
import chat.util.MenuChecker;
import chat.util.PortChecker;

public class ClientDriver {
	private static String name = null;
    private static String message = null;

    public static void main(String[] args) throws IOException {

    	String hostName = args[0];	//localhost
    	
    	Checker checker = new PortChecker();
    	int port = checker.check(args[1]);
    	Checker menuChecker = new MenuChecker();
    	
        Socket socket = new Socket(hostName, port);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        
        Scanner menu = new Scanner(System.in);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        
        System.out.println("MENU:");
        System.out.println("1. Give me a name");
        System.out.println("2. Quit");       
        
        boolean flag = true;
        String menuIndex;
        int checkedIndex;
        while (flag) {
        	menuIndex = menu.nextLine();
			try {
				checkedIndex = menuChecker.check(menuIndex);
			} catch (NumberFormatException e) {
				System.out.println("Warning: Menu index is an integer, please select again.");
				continue;
			}
			
			switch (checkedIndex) {
			case 1:
				System.out.print("Enter your name: ");
				name = in.readLine();
				ClientData clientData = new ClientData();
				clientData.setName(name);
				/* here */
				output.writeObject(clientData);
            	output.flush();
				flag = false;
				break;
			case 2:
				quit(socket, input, output, menu, in);
			default:
				System.out.println("Warning: Invalid choice, please enter an valid index.");
				break;
			}
		}
        
		while (true) {
			
	        System.out.println("MENU:");
			System.out.println("1. Send message to server");
			System.out.println("2. Print message from server");
			System.out.println("3. Quit");
			
        	menuIndex = menu.nextLine();
			try {
				checkedIndex = menuChecker.check(menuIndex);
			} catch (NumberFormatException e) {
				System.out.println("Warning: Menu index is an integer, please select again.");
				continue;
			}
	        switch(checkedIndex){
	        	case 1:	
	        		System.out.print("Enter a message to server: ");
	            	message = in.readLine();
	            	ClientData clientData = new ClientData();
	            	clientData.setName(name);
	            	clientData.setMessage(message);
	            	output.writeObject(clientData);
	            	output.flush();
	            	
	            	if ("BACKUP".equalsIgnoreCase(message)) {
	            		
	            		String historyRecords = null;
	            		StringBuilder sb = new StringBuilder();
	            		while ((historyRecords = input.readLine()).length() > 0) {
							sb.append(historyRecords);
							sb.append(System.getProperty("line.separator"));
						}
	            		Logger.dump(name, sb.toString());
	            		System.out.println("Backup finished!");
	            	}
	            	
	            	break;
	        	case 2: 
	        		String serverMsg = input.readLine();
	        		System.out.println("Server said: "+serverMsg);
	        		break;
	        	case 3:	
					quit(socket, input, output, menu, in);
				default:
					System.out.println("Warning: Invalid choice, please enter an valid index.");
					break;
	        }
		}
    }

    

	/**
	 * @param socket
	 * @param input
	 * @param output
	 * @param menu
	 * @param in
	 * @throws IOException
	 */
	private static void quit(Socket socket, BufferedReader input,
			ObjectOutputStream output, Scanner menu, BufferedReader in)
			throws IOException {
		
		ClientData clientData = new ClientData();
		clientData.setName(name);
		clientData.setCloseRequest(true);
		output.writeObject(clientData);
		output.flush();
		
		if (in != null) {
			in.close();
		}
		
		if (menu != null) {
			menu.close();
		}
		
		if (output != null) {
			output.close();
		}
		
		if (input != null) {
			input.close();
		}
		
		if (!socket.isClosed()) {
			socket.close();
		}
		System.exit(0);
	}
	
}
