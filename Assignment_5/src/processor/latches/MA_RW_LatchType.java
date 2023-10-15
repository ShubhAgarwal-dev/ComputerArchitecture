package processor.latches;

public class MA_RW_LatchType {

    boolean RW_enable;

    boolean RW_Buzy;

    public boolean isRW_Buzy() {
        return RW_Buzy;
    }

    public void setRW_Buzy(boolean RW_Buzy) {
        this.RW_Buzy = RW_Buzy;
    }

    private int loadResult, opResult, opCode, rd, r31;


    public MA_RW_LatchType() {
        RW_enable = false;
    }

    public int getLoadResult() {
        return loadResult;
    }

    public void setLoadResult(int loadResult) {
        this.loadResult = loadResult;
    }

    public int getOpResult() {
        return opResult;
    }

    public void setOpResult(int opResult) {
        this.opResult = opResult;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public int getRd() {
        return rd;
    }

    public void setRd(int rd) {
        this.rd = rd;
    }

    public int getR31() {
        return r31;
    }

    public void setR31(int r31) {
        this.r31 = r31;
    }

    public boolean isRW_enable() {
        return RW_enable;
    }

    public void setRW_enable(boolean rW_enable) {
        RW_enable = rW_enable;
    }

}
