/**
 * 
 */
package chat.util;

/**
 * @author lingjiemeng
 *
 */
public class MenuChecker implements Checker {

	@Override
	public int check(String str) throws NumberFormatException {
		int index = Integer.parseInt(str);
		return index;
	}

}
