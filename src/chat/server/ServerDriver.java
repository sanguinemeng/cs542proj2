package chat.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import chat.util.Checker;
import chat.util.ClientData;
import chat.util.MenuChecker;
import chat.util.NameSearcher;
import chat.util.PortChecker;
import chat.util.Searcher;
import chat.util.ServerHandler;
import chat.util.ThreadPool;

public class ServerDriver {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
    	boolean quitLoop = false;
    	Checker checker = new PortChecker();
    	int port = checker.check(args[0]);
    	Checker menuChecker = new MenuChecker();
    	ThreadPool threadPool = ThreadPool.getInstance();
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
		
		while(!quitLoop){
    		System.out.println("MENU:");
    		System.out.println("1. Broadcast");
    		System.out.println("2. Send message to a client");
    		System.out.println("3. Print message of a client");
    		System.out.println("4. Quit");
    		
        	menuIndex = menu.nextLine();
			try {
				checkedIndex = menuChecker.check(menuIndex);
			} catch (NumberFormatException e) {
				System.out.println("Warning: Menu index is an integer, please select again.");
				continue;
			}
			
    		switch (checkedIndex) {
    		case 1:
    			System.out.print("Enter broadcast message: ");
    			
    			{		
	    			List<ServerHandler> handlers = serverThread.getHandlers();
	    			s = new Scanner(System.in);
	    			String serverOutput = s.nextLine();
	        		for (ServerHandler sh : handlers) {
	        			if (!sh.isStopThread()) {
	        				sh.sendToClient(serverOutput);
	        			}
	        		}
    			}
    			
    			break;
    		case 2:
    			System.out.println("Online clients:");
    			onlineNames = nameSearcher.searchAndPrint(serverThread.getRecords());  // print online clients' list
    			System.out.print("To whom? Enter a name: ");
    			name = scanName.nextLine();
    			System.out.print("Enter a message: ");
    			if (onlineNames.contains(name)) {
    				List<ServerHandler> handlers = serverThread.getHandlers();
    				for (ServerHandler sh : handlers) {
    					if (sh.getName().equals(name)) {
    						s = new Scanner(System.in);
    						String serverOutput = s.nextLine();
    						sh.sendToClient(serverOutput);
    						break;
    					}
    				}
    			} else {
    				System.out.println("Warning: Invalid name!");
    			}
    			break;
    		case 3:
    			System.out.println("Online clients:");
    			records = serverThread.getRecords();
    			onlineNames = nameSearcher.searchAndPrint(records);
    			System.out.print("Whose message to print? Enter a name: ");
    			name = scanName.nextLine();
    			if (onlineNames.contains(name)) {
    				System.out.println(records.get(name));
    			} else {
    				System.out.println("Warning: Invalid name!");
    			}
    			break;
    		case 4:
    			quitLoop = true;
    			break;
    		default:
    			System.out.println("Warning: Invalid choice, please enter an valid index.");
    			break;
    		}
    	}
    	
		if (s !=null) {
			s.close();
		}
		
		if (scanName != null) {
			scanName.close();
		}
		
		if (menu != null) {
			menu.close();
		}
		
		
		// quit the server thread
		serverThread.stopServerThread();
		thread.interrupt();
		
    	System.exit(0);
    }
   
}
