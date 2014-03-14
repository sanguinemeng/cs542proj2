package chat.util;


/**
 * check validation of input arguments
 */
public class ArgumentChecker implements Checker {
	private DeviceType type;	// server or client
	
	/**
	 * constructor
	 * @param type - server or client
	 */
	public ArgumentChecker(DeviceType type) {
		this.type = type;
	}

	@Override
	public int check(String... str) {
		switch (type) {
		
		// server only take a port number as argument
		case server:	
			if (str.length != 1) {
				System.out.println("Warning: Input a port number as argument!");
				System.exit(1);
			}
			break;
			
		// client take server address and port number as arguments
		case client:	
			if (str.length != 2) {
				System.out.println("Warning: Input server address and port number as arguments!");
				System.exit(1);
			}
			break;
		default:
			break;
		}
		return 0;
	}

}
