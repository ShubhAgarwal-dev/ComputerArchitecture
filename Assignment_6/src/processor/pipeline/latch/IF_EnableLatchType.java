package processor.pipeline.latch;

public class IF_EnableLatchType {

    boolean IF_enable;

    boolean isStall;

    boolean isIFBusy;

    public IF_EnableLatchType() {
        IF_enable = true;
        isStall = false;
        isIFBusy = false;
    }

    public boolean isIF_enable() {
        return IF_enable;
    }

    public void setIF_enable(boolean iF_enable) {
        IF_enable = iF_enable;
    }

    public boolean getStall() {
        return this.isStall;
    }

    public void setStall(boolean isStall) {
        this.isStall = isStall;
    }

    public boolean isIFBusy() {
        return isIFBusy;
    }

    public void setIFBusy(boolean isIFBusy) {
        this.isIFBusy = isIFBusy;
    }

}
