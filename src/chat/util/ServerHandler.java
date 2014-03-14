package chat.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

/**
 * Handler to handle multi-threaded server
 */
public class ServerHandler implements Runnable{
	
	private Socket clientSocket;
	
	private ObjectInputStream input;	// input for server
	private PrintWriter output;			// output for server
	
	// data structure to store history messages from each client
	private Map<String, ClientData> records;
	
	private String name;
	private volatile String serverMessage;
	private volatile boolean stopThread; // if server stops connection

	/**
	 * @return the stopThread
	 */
	public boolean isStopThread() {
		return stopThread;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param serverMessage - message sent from server
	 */
	public void sendToClient(String serverMessage) {
		this.serverMessage = serverMessage;
	}


	/**
	 * constructor
	 */
	public ServerHandler(Socket clientSocket, Map<String, ClientData> records) {
		init(clientSocket, records);
	}


	/**
	 * initialize serverHandler
	 * @param clientSocket
	 */
	private void init(Socket clientSocket, Map<String, ClientData> records) {
		this.records = records;
		this.clientSocket = clientSocket;
		try {
			// get messages sent from clients
			input = new ObjectInputStream(this.clientSocket.getInputStream());
			
			// send message to clients
			output = new PrintWriter(this.clientSocket.getOutputStream());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}


	@Override
	public void run() {
		Thread writeThread = new ServerHandlerWriteThread();
		writeThread.start();
		
		// accept data from client as soon as a client send a message
		while (true) {
			
			try {
				ClientData clientData = (ClientData) input.readObject();
				
				if (clientData != null) {
					
					// get client information from ClientData object just accepted
					name = clientData.getName();
					String message = clientData.getMessage();
					boolean quitRequest = clientData.isCloseRequest();

					ClientData storedClientData = null;
					
					// client quit and has given a name of itself
					if (quitRequest) {
						if (name != null) {
							
							// store the data from the client to map
							storedClientData = records.get(name);
							storedClientData.setCloseRequest(true);
						}
						break;
					}
					
					// if the client's name is already in the map
					if (records.containsKey(name)) {
						
						storedClientData = records.get(name);
						
						// get the history records of the client
						String oldMessage = storedClientData.getMessage();
						
						// if the client has given a name but not yet sent a message
						if (oldMessage.length() == 0 ) {
							storedClientData.setMessage(message);
							storedClientData.setName(name);
							storedClientData.setCloseRequest(quitRequest);
							
						} else {	// the client has sent messages before
							
							// add new message to map together with old message
							String newMessage = oldMessage
									+ System.getProperty("line.separator")
									+ message;
							storedClientData.setMessage(newMessage);
							storedClientData.setName(name);
							storedClientData.setCloseRequest(quitRequest);
						}
						
						// if the client want to backup the message,
						// send the message of a client back to the client
						if ("BACKUP".equalsIgnoreCase(message)) {
							output.println(records.get(name).getMessage());
							output.println("");   // mark the end of the records
							output.flush();
						}
					} else {	
						// if a new client sent a message and its name is not 
						// in the map, put the name in the map
						records.put(name, clientData);
					}
				}
				
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			} catch (ClassNotFoundException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
			
		}
		
		try {
			
			// stop ServerHandlerWriteThread
			stopThread = true;   
			
			// close output for server
			if (output != null) {
				output.close();
			}
			
			// close input for server
			if (input != null) {
				input.close();
			}
			
			// close clientSocket
			if (!clientSocket.isClosed()){
				clientSocket.close();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		
	}
	
	private class ServerHandlerWriteThread extends Thread {

		@Override
		public void run() {
			while (!stopThread) {
				
				// send message to a specific client
				if (serverMessage != null) {
					output.println(serverMessage);
					output.flush();
					serverMessage = null;
				}
			}
		}
		
	}

}
