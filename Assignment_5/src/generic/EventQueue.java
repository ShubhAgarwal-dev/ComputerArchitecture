package generic;

import java.util.Comparator;
import java.util.PriorityQueue;

import processor.Clock;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	
	public EventQueue()
	{
		this.queue = new PriorityQueue<Event>(new EventComparator());
	}
	
	public void addEvent(Event event)
	{
		this.queue.add(event);
	}

	public void removeEvent(Event event){this.queue.remove(event);}

	public void processEvents()
	{
		while(!this.queue.isEmpty() && this.queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			Event event = this.queue.poll();
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
