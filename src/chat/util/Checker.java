package chat.util;

/**
 * check data validation
 */
public interface Checker {
	
	/**
	 * check validation of input
	 * @param str - all input arguments
	 * @return validated data
	 */
	public int check (String ... str);
}
