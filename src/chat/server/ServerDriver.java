package chat.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import chat.util.ArgumentChecker;
import chat.util.Checker;
import chat.util.ClientData;
import chat.util.DeviceType;
import chat.util.MenuChecker;
import chat.util.NameSearcher;
import chat.util.PortChecker;
import chat.util.Searcher;
import chat.util.ServerHandler;
import chat.util.ThreadPool;

/**
 * server driver
 */
public class ServerDriver {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
    	
    	// check validation of arguments
    	ArgumentChecker argChecker = new ArgumentChecker(DeviceType.server);
    	argChecker.check(args);
    	
    	boolean quitLoop = false;
    	
    	// check port
    	Checker checker = new PortChecker();
    	int port = checker.check(args[0]);
    	Checker menuChecker = new MenuChecker();
    	
    	// start thread pool
    	ThreadPool threadPool = ThreadPool.getInstance();
		
    	// scan menu index and client's name
    	Scanner menu = new Scanner(System.in);
		Scanner scanName = new Scanner(System.in);
		String name = null;
		Searcher nameSearcher = new NameSearcher();
		
		String menuIndex = null;
		int checkedIndex;
		
		Set<String> onlineNames = null;
		Map<String, ClientData> records;
    	
    	ServerThread serverThread = new ServerThread(port, threadPool);
		Thread thread = new Thread(serverThread);
		thread.start();
		Scanner s = null;
		
		
		// keep showing the menu if not quit
		while(!quitLoop){
    		System.out.println("MENU:");
    		System.out.println("1. Broadcast");
    		System.out.println("2. Send message to a client");
    		System.out.println("3. Print message of a client");
    		System.out.println("4. Quit");
    		
    		
    		// check input menu index
        	menuIndex = menu.nextLine();
			try {
				checkedIndex = menuChecker.check(menuIndex);
			} catch (NumberFormatException e) {
				System.out.println("Warning: Menu index is an integer, please select again.");
				continue;
			}
			
			
    		switch (checkedIndex) {	
    		case 1:	// broadcast
    			System.out.print("Enter broadcast message: ");
    			
    			{		
	    			List<ServerHandler> handlers = serverThread.getHandlers();
	    			s = new Scanner(System.in);
	    			
	    			// set broadcast message
	    			String serverOutput = s.nextLine();
	        		for (ServerHandler sh : handlers) {
	        			if (!sh.isStopThread()) {
	        				
	        				//broadcast message to client
	        				sh.sendToClient(serverOutput);
	        			}
	        		}
    			}
    			
    			break;
    		case 2:	// send message to a online client
    			System.out.println("Online clients:");
    			
    			// print online client list
    			onlineNames = nameSearcher.searchAndPrint(serverThread.getRecords()); 
    			System.out.print("To whom? Enter a name: ");
    			name = scanName.nextLine();	// choose an online client to send message
    			System.out.print("Enter a message: ");
    			if (onlineNames.contains(name)) {	// if name is valid
    				List<ServerHandler> handlers = serverThread.getHandlers();
    				for (ServerHandler sh : handlers) {
    					
    					if (sh.getName().equals(name)) {
    						
    						s = new Scanner(System.in);
    						String serverOutput = s.nextLine();	// set message
    						sh.sendToClient(serverOutput);	// send to client
    						break;
    						
    					}
    					
    				}
    			} else {	// if name is invalid
    				System.out.println("Warning: Invalid name!");
    			}
    			
    			break;
    			
    		case 3:	// print message from a client
    			System.out.println("Online clients:");
    			records = serverThread.getRecords();
    			
    			// print list of online clients
    			onlineNames = nameSearcher.searchAndPrint(records);
    			System.out.print("Whose message to print? Enter a name: ");
    			name = scanName.nextLine();
    			if (onlineNames.contains(name)) {	// if name is valid
    				System.out.println(records.get(name));
    			} else {	// if name is invalid
    				System.out.println("Warning: Invalid name!");
    			}
    			
    			break;
    			
    		case 4:	// quit server
    			quitLoop = true;
    			break;
    		
    		default:
    			System.out.println("Warning: Invalid choice, please enter an valid index.");
    			break;
    		}
    	}
    	
		
		// close scanner
		if (s !=null) {
			s.close();
		}
		
		
		// close name scanner
		if (scanName != null) {
			scanName.close();
		}
		
		
		// close menu scanner
		if (menu != null) {
			menu.close();
		}
		
		
		// quit the server thread
		serverThread.stopServerThread();
		thread.interrupt();
		
    	System.exit(0);
    }
   
}
