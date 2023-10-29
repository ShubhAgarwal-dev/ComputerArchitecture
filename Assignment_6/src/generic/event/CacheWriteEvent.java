package generic.event;

public class CacheWriteEvent extends Event {

    int addr;
    int val;

    public CacheWriteEvent(long eventTime, Element requestingElement, Element processingElement,
                           int address, int val) {
        super(eventTime, EventType.CacheWrite, requestingElement, processingElement);
        this.addr = address;
        this.val = val;
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

}
