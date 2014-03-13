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


    None.	// 1. have not handle port input boundary
    		// 2. cannot delete *.backup(solved)


BUGS:


	None.


FILES:


  Included with this project are 17 files:

  Logger.java, the file that stores the chat history of a client to a file
  ClientDriver.java the client driver file
  ServerDriver.java, the server driver file
  README.txt, the text file you are presently reading
  build.xml, the buildfile
  run.sh, script file to run the drivers


SAMPLE OUTPUT:


  bingsuns2% ant run -Darg0=dataFile -Darg1=3 -Darg2=searchFile -Darg3=4 -Darg4=4
  SOME OUTPUT
  WAHT CONSTRUCTORS ARE CALLED
  ===========Results from Driver===========
  SEARCH RESULTS
  BUILD SUCCESSFUL
  Total time: 9 seconds
  bingsuns2% 


TO COMPILE:


  Just extract the files and then do a "ant compile".


TO RUN:


  Please run as: ant run -Darg0=<DATAFILE> -Darg1=<THRDNUM1> -Darg2=<SRCHFILE> -Darg=<THRDNUM2> -Darg4=<DEBUGVALUE> 
  For example:   ant run -Darg0=dataFile -Darg1=1 -Darg2=searchFile -Darg3=5 -Darg4=4


EXTRA CREDIT:


  N/A



BIBLIOGRAPHY:

This serves as evidence that we are in no way intending Academic Dishonesty.
Lingjie Meng


  * http://ant.apache.org/manual/using.html

  * http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html


ACKNOWLEDGEMENT:


  During the coding process one or more of the following people may have been
  consulted. Their help is greatly appreciated.

  D. Han

