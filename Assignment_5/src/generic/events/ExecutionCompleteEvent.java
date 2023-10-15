package generic.events;

import generic.Element;
import generic.Event;

public class ExecutionCompleteEvent extends Event {
	
	public ExecutionCompleteEvent(long eventTime, Element requestingElement, Element processingElement)
	{
		super(eventTime, EventType.ExecutionComplete, requestingElement, processingElement);
	}

}
