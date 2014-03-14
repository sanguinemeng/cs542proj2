CS542 Design Patterns
Spring 2014
PROJECT 2 README FILE

Due Date: Thursday, March 13, 2014
Submission Date: Thursday, March 13, 2014
Grace Period Used This Project: 0 Days
Grace Period Remaining: 0 Days
Author(s): Lingjie Meng
e-mail(s): lmeng4@binghamton.edu


PURPOSE:


  Design a multi-threaded chat service.


PERCENT COMPLETE:


  I believe I have completed 100% of this project.
  
  
PARTS THAT ARE NOT COMPLETE:


    None.


BUGS:


	None.


FILES:


  Included with this project are 17 files:

  ClientDriver.java, the client driver file
  ServerDriver.java, the server driver file
  ServerThread.java, the file to separate input and output of server
  ThreadPool.java, the file of thread pool of singleton pattern
  ServerHandler.java, the file handles multiple threads of server
  ClientData.java, the file defines the data structure to store records
  Searcher.java, the interface of NameSearcher.java
  NameSearcher.java, the file to search and print online clients names
  DeviceType.java, two types defined - server and client
  Checker.java, the interface to check validation of input data
  ArgumentChecker.java, the file to check if the argument is in correct format
  MenuChecker.java, the file to check is the input menu index is valid
  PortChecker.java, the file to check if the port number is out of boundary
  Logger.java, the file that stores the chat history of a client to a file
  README.txt, the text file you are presently reading
  build.xml, the buildfile
  run.sh, script file to run the drivers


SAMPLE OUTPUT:


	server:
		remote02:~/cs542/assign2/chat> ./run.sh server 9191
		/usr/bin/java -cp ./build/classes:lib/*/*.jar: chat.server.ServerDriver 9191
		MENU:
		1. Broadcast
		2. Send message to a client
		3. Print message of a client
		4. Quit

	client:
		remote02:~/cs542/assign2/chat> ./run.sh client localhost 9191
		/usr/bin/java -cp ./build/classes:lib/*/*.jar: chat.client.ClientDriver localhost 9191
		MENU:
		1. Give me a name
		2. Quit

TO COMPILE:


  Just extract the files and then do a "ant compile".


TO RUN:


  Please run as: 
  		server: ./run.sh server portNumber
  		client: ./run.sh client serverName portNumber
  		 
  For example:   
		server: ./run.sh server 9191
		client: ./run.sh client localhost 9191


EXTRA CREDIT:


  get both broadcasting message and send to a specific client working



BIBLIOGRAPHY:

This serves as evidence that we are in no way intending Academic Dishonesty.
Lingjie Meng


  * http://www.cnblogs.com/phinecos/archive/2008/07/19/1246623.html


ACKNOWLEDGEMENT:


  During the coding process one or more of the following people may have been
  consulted. Their help is greatly appreciated.

  D. Han

