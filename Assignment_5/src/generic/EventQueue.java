package generic;

import java.util.Comparator;
import java.util.PriorityQueue;

import processor.Clock;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	
	public EventQueue()
	{
		queue = new PriorityQueue<Event>(new EventComparator());
	}
	
	public void addEvent(Event event)
	{
		queue.add(event);
	}

	public void removeEvent(Event event){queue.remove(event);}

	public void processEvents()
	{
		while(!queue.isEmpty() && queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			Event event = queue.poll();
			if(event!=null) {
				event.getProcessingElement().handleEvent(event);
			}
		}
	}
}

class EventComparator implements Comparator<Event>
{
	@Override
    public int compare(Event x, Event y)
    {
		return Long.compare(x.getEventTime(), y.getEventTime());
    }
}
