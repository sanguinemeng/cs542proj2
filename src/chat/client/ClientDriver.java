package chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import chat.util.ArgumentChecker;
import chat.util.Checker;
import chat.util.ClientData;
import chat.util.ConnHelper;
import chat.util.DeviceType;
import chat.util.Logger;
import chat.util.MenuChecker;
import chat.util.PortChecker;
import chat.util.SafeSocket;

/**
 * client driver
 */
public class ClientDriver {
	
	private static String name = null;
    private static String message = null;

    public static void main(String[] args) {
    	
    	// check validation of input arguments
    	ArgumentChecker argChecker = new ArgumentChecker(DeviceType.client);
    	argChecker.check(args);

    	String hostName = args[0];	// localhost
    	
    	// check port and menu
    	Checker checker = new PortChecker();
    	int port = checker.check(args[1]);
    	Checker menuChecker = new MenuChecker();
    	
    	while (true) {
			try {
				// start socket
				ConnHelper helper = new SafeSocket();
				Socket socket = helper.returnSafeConnection(hostName, port);
				// input and output
				BufferedReader input = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				ObjectOutputStream output = new ObjectOutputStream(
						socket.getOutputStream());
				// menu scanner
				Scanner menu = new Scanner(System.in);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						System.in));
				System.out.println("MENU:");
				System.out.println("1. Give me a name");
				System.out.println("2. Quit");
				boolean flag = true;
				String menuIndex;
				int checkedIndex;
				// keep showing menu if not quit
				while (flag) {
					menuIndex = menu.nextLine();
					try {
						// check intput menu index
						checkedIndex = menuChecker.check(menuIndex);
					} catch (NumberFormatException e) {
						System.out
								.println("Warning: Menu index is an integer, please select again.");
						continue;
					}

					switch (checkedIndex) {
					case 1: // give a name
						System.out.print("Enter your name: ");
						name = in.readLine();
						ClientData clientData = new ClientData();
						clientData.setName(name);

						// output to server by clientData
						output.writeObject(clientData);
						output.flush();
						flag = false;
						break;
					case 2: // quit
						quit(socket, input, output, menu, in);
					default: // invalid selection
						System.out
								.println("Warning: Invalid choice, please enter an valid index.");
						break;
					}
				}
				while (true) {

					System.out.println("MENU:");
					System.out.println("1. Send message to server");
					System.out.println("2. Print message from server");
					System.out.println("3. Quit");

					menuIndex = menu.nextLine();
					try {
						checkedIndex = menuChecker.check(menuIndex);
					} catch (NumberFormatException e) {
						System.out
								.println("Warning: Menu index is an integer, please select again.");
						continue;
					}

					switch (checkedIndex) {

					case 1: // send message to server
						System.out.print("Enter a message to server: ");
						message = in.readLine();
						ClientData clientData = new ClientData();
						clientData.setName(name);
						clientData.setMessage(message);
						output.writeObject(clientData);
						output.flush();

						// send backup message to server to get history records from server
						if ("BACKUP".equalsIgnoreCase(message)) {

							String historyRecords = null;
							StringBuilder sb = new StringBuilder();
							while ((historyRecords = input.readLine()).length() > 0) {
								sb.append(historyRecords);
								sb.append(System.getProperty("line.separator"));
							}
							Logger.dump(name, sb.toString()); // dump the records to backup file
							System.out.println("Backup finished!");
						}

						break;

					case 2: // print message from server
						String serverMsg = input.readLine();
						System.out.println("Server said: " + serverMsg);
						break;

					case 3: // quit
						quit(socket, input, output, menu, in);

					default: // invalid selection
						System.out
								.println("Warning: Invalid choice, please enter an valid index.");
						break;
					}
				}
			} catch (IOException e) {   // handle the situation where server quits before the clients
				System.err.println("Lost the server...");
			}
		}
    }

    

	/**
	 * quit server, close all input and output
	 * @param socket
	 * @param input
	 * @param output
	 * @param menu
	 * @param in
	 * @throws IOException
	 */
	private static void quit(Socket socket, BufferedReader input,
			ObjectOutputStream output, Scanner menu, BufferedReader in)
			throws IOException {
		
		ClientData clientData = new ClientData();
		clientData.setName(name);
		clientData.setCloseRequest(true);
		output.writeObject(clientData);
		output.flush();
		
		// close scanner
		if (in != null) {
			in.close();
		}
		
		// close menu scanner
		if (menu != null) {
			menu.close();
		}
		
		// close output
		if (output != null) {
			output.close();
		}
		
		// close input
		if (input != null) {
			input.close();
		}
		
		// close socket
		if (!socket.isClosed()) {
			socket.close();
		}
		System.exit(0);
	}
	
}
