/**
 * 
 */
package chat.util;

import java.net.Socket;

/**
 * @author Andy
 *
 */
public class SafeSocket implements ConnHelper {

	@Override
	public Socket returnSafeConnection(String hostName, int port) {
		Socket socket = null;
		boolean goodConn = false;
		int tryReconnect = 0;
		
		while (!goodConn) {		
			try {
				socket = new Socket(hostName, port);
				goodConn = true;
				System.out.println("Succeed in connecting to the server!");
			} catch (Exception e) {
				tryReconnect++;
				
				if (tryReconnect > 5) {
					System.err.println("Cannot connect to the server, quiting the client...");
					System.exit(1);
				}
				
				System.err.println("Disconnect with the server, reconnect " + tryReconnect + "...");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					System.out.println(e1.getMessage());
					System.exit(1);
				}
			}
		}
			
		return socket;
	}

}
