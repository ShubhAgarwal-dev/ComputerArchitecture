package generic;

public class CacheResponseEvent extends Event {

    int val;

    public CacheResponseEvent(long eventTime, Element requestingElement, Element processingElement,
                              int val) {
        super(eventTime, EventType.CacheResponse, requestingElement, processingElement);
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

}
