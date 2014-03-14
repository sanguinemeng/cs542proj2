/**
 * 
 */
package chat.util;

import java.net.Socket;

/**
 * Help to do some error-prone connections
 * @author Andy
 *
 */
public interface ConnHelper {
	
	/**
	 * Try to connect to the server, if failed, then quit the client.
	 * @return a socket whose exception is already handled
	 */
	Socket returnSafeConnection(String hostName, int port);

}
