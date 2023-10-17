package generic.events;

import generic.Element;
import generic.Event;

public class OFEvent extends Event {
    int instruction;

    public int getInstruction() {
        return instruction;
    }

    public void setInstruction(int instruction) {
        this.instruction = instruction;
    }

    public OFEvent(long eventTime, Element requestingElement, Element processingElement,int instruction){
        super(eventTime,EventType.OFEvent,requestingElement,processingElement);
        this.instruction = instruction;
    }
}
