package chat.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * search for message records from map using client name
 */
public class NameSearcher implements Searcher{

	@Override
	public Set<String> searchAndPrint(Map<String, ClientData> map) {
		
		// all the clients' names that have been registered, online and off-line
		Set<String> names = map.keySet();	
		
		// only names of online clients are selected
		Set<String> onlineNames = Collections.synchronizedSet(new HashSet<String>());
		
		for (String name : names) {
			
			// if a client has not closed the connection
			if (!map.get(name).isCloseRequest()) {
				System.out.println(name);
				onlineNames.add(name);
			}
		}
		
		return onlineNames;
		
	}

}
