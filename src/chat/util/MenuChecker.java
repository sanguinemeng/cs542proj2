package chat.util;

/**
 * check validation of menu number selected by user
 */
public class MenuChecker implements Checker {

	@Override
	public int check(String ... str) throws NumberFormatException {
		
		// parse input string to integer as menu index
		int index = Integer.parseInt(str[0]);
		return index;
	}

}
