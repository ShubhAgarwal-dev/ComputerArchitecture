package generic;

import processor.Processor;

public class Misc {
	
	public static void printErrorAndExit(String message)
	{
		System.err.println(message);
		System.exit(1);
	}


	// Get PC directly
	public static int getPC(Processor processor){
		return  processor.getRegisterFile().getProgramCounter();
	}


}

