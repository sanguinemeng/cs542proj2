package chat.util;

import java.util.Map;
import java.util.Set;

/**
 * search records from map
 */
public interface Searcher {

	/**
	 * search for online clients from map
	 * @param records
	 * @return online name set
	 */
	public Set<String> searchAndPrint(Map<String, ClientData> records);
	
}
