package processor.pipeline.latch;

public class IF_OF_LatchType {

    boolean OF_enable;
    int instruction, currentPC;
    boolean isNop, isStall;


    boolean isOFBusy, isValidInst;

    public IF_OF_LatchType() {
        OF_enable = false;
        isNop = isStall = false;
        isOFBusy = isValidInst = false;
    }

    public boolean isOF_enable() {
        return OF_enable;
    }

    public void setOF_enable(boolean oF_enable) {
        OF_enable = oF_enable;
    }

    public int getInstruction() {
        return instruction;
    }

    public void setInstruction(int instruction) {
        this.instruction = instruction;
    }



    public int getCurrentPC() {
        return currentPC;
    }

    public void setCurrentPC(int currPC) {
        currentPC = currPC;
    }

    public boolean getNop() {
        return this.isNop;
    }

    public void setNop(boolean isNop) {
        this.isNop = isNop;
    }

    public boolean getStall() {
        return this.isStall;
    }

    public void setStall(boolean isStall) {
        this.isStall = isStall;
    }

    public boolean isOFBusy() {
        return isOFBusy;
    }

    public void setOFBusy(boolean isOFBusy) {
        this.isOFBusy = isOFBusy;
    }

    public boolean isValidInst() {
        return isValidInst;
    }

    public void setValidInst(boolean isValidInst) {
        this.isValidInst = isValidInst;
    }

}
