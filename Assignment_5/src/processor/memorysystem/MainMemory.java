package processor.memorysystem;

import generic.event.Element;
import generic.event.Event;

public class MainMemory implements Element {
	int[] memory;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent(Event event) {
	}
}
