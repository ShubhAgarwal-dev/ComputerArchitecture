package generic;


public class CacheReadEvent extends Event {

    int addr;


    public CacheReadEvent(long eventTime, Element requestingElement, Element processingElement,
                          int address) {
        super(eventTime, EventType.CacheRead, requestingElement, processingElement);
        this.addr = address;
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }
}
