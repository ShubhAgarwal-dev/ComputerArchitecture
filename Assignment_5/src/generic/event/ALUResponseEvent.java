package generic.event;

public class ALUResponseEvent extends Event {
    public ALUResponseEvent(long eventTime, Element requestingElement, Element processingElement){
        super(eventTime,EventType.ALUResponse,requestingElement,processingElement);
    }
}