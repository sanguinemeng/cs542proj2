package chat.util;

/**
 * check validation of input port number 
 */
public class PortChecker implements Checker {

	@Override
	public int check(String ... str) {
    	String portStr = str[0];
		int port = 0;
    	
    	// check if port number is an integer
    	try {
			port = Integer.parseInt(portStr);
		} catch (NumberFormatException e) {
			System.out.println("Warning: port number is an integer!");
			System.exit(1);
		}
    	
    	try {
    		
    		// boundary of port number
    		if (port<1024 || port>65535) {
        		throw new IllegalArgumentException();
        	}
		} catch (IllegalArgumentException e) {
			System.out.println("Warning: Port number is from 1024 to 65535!");
			System.exit(1);
		}
    	return port;
	}
	
}
