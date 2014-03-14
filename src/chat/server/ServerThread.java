package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.util.ClientData;
import chat.util.ServerHandler;
import chat.util.ThreadPool;

/**
 * multiple thread to handle input and output
 */
public class ServerThread implements Runnable {
	private boolean isStop = false;
	private ServerSocket listener;
	private ThreadPool threadPool;
	private Map<String, ClientData> records;
	private List<ServerHandler> handlers;

	/**
	 * constructor
	 * @param port - port number
	 * @param threadPool
	 * @throws IOException
	 */
	public ServerThread(int port, ThreadPool threadPool) throws IOException {
		listener = new ServerSocket(port);	//port: 9191
		records = Collections.synchronizedMap(new HashMap<String, ClientData>());
		handlers = Collections.synchronizedList(new ArrayList<ServerHandler>());		
		this.threadPool = threadPool;
	}

	@Override
	public void run() {
		while (!isStop) {
			try {
				// keep accepting client massage
				Socket clientSocket = listener.accept();
	    		ServerHandler serverHandler = new ServerHandler(clientSocket, records);
	    		handlers.add(serverHandler);
	    		
	    		// start a new handler
	    		threadPool.start(serverHandler);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}
		
		// close listener
		if (!listener.isClosed()) {
    		try {
				listener.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
    	}

	}
	
	
	/**
	 * stop server
	 */
	public void stopServerThread() {
		isStop = true;
	}
	
	
	/**
	 * @return thread pool
	 */
	public ThreadPool getThreadPool() {
		return threadPool;
	}

	
	/**
	 * @return records
	 */
	public Map<String, ClientData> getRecords() {
		return records;
	}

	
	/**
	 * @return handlers
	 */
	public List<ServerHandler> getHandlers() {
		return handlers;
	}

	
}
