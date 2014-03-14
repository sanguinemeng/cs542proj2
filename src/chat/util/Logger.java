package chat.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * backup history records from clients
 */
public class Logger {
	
	/**
	 * private constructor
	 */
	private Logger(){}
	
	
	/**
	 * dump history records from clients to files
	 * @param fileName - the name of client
	 * @param fileContent - history records
	 */
	public static void dump(String fileName, String fileContent) {
		File file;
		FileWriter writer = null;
		BufferedWriter buffer = null;
		
		try {
			file = new File(fileName+".backup");
			writer = new FileWriter(file);
			buffer = new BufferedWriter(writer);
			buffer.write(fileContent);
			buffer.flush();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		
		if (buffer != null) {
			try {
				buffer.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		
	}
	
	
	
}
