package chat.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * thread pool of Singleton Pattern
 */
public class ThreadPool {
	private volatile static ThreadPool thrdPlInst;

	// list of idle threads
	private List<WorkerThread> idleThreads = 
				Collections.synchronizedList(new LinkedList<WorkerThread>());

	
	/**
	 * private constructor
	 */
	private ThreadPool(){
	}

	
	/**
	 * double-checked locking for Singleton
	 */
	public static ThreadPool getInstance(){
		if(thrdPlInst == null){
			synchronized(ThreadPool.class){
				if(thrdPlInst == null){
					thrdPlInst = new ThreadPool();
				}
			}
		}
		return thrdPlInst;
	}
	
	
	/**
	 * borrow thread from idle thread list
	 */
	public synchronized WorkerThread borrowThread() {
		if (idleThreads.isEmpty()) {
			return null;
		}
		
		// if there are idle threads available in the 
		// list, get the first one from the list
		return idleThreads.remove(0);
	}

	
	/**
	 * return idle threads
	 * @param idleThread
	 */
	public synchronized void returnThread(WorkerThread idleThread) {
		idleThreads.add(idleThread);
	}
	
	
	/**
	 * new thread for a new client
	 * @param serverHandler
	 */
	public void start(ServerHandler serverHandler){
		if (!idleThreads.isEmpty()) {
			WorkerThread borrowedThread = this.borrowThread();
			borrowedThread.setTask(serverHandler);
		} else {
			WorkerThread newThread = new WorkerThread(serverHandler);
			newThread.start();
		}
	}
	
	
	
	/**
	 * worker thread for handler
	 */
	private class WorkerThread extends Thread {
		private ServerHandler serverHandler;
		
		/**
		 * constructor
		 * @param serverHandler
		 */
		public WorkerThread(ServerHandler serverHandler){
			this.serverHandler = serverHandler;
		}

		
		@Override
		public void run() {
			while(true){
				if (serverHandler!=null) {
					serverHandler.run();	//core service
				}
				thrdPlInst.returnThread(this);
			
				synchronized(this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						System.err.println(e.getMessage());
						System.exit(1);
					}
				}
			}
		}
		
		
		// set a new task to a newly borrowed thread
		private synchronized void setTask(ServerHandler serverHandler) {
			this.serverHandler = serverHandler;
			this.notifyAll();
		}
	}

}
