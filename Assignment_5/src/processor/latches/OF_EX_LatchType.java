package processor.latches;

public class OF_EX_LatchType {

    boolean EX_enable;

    boolean EX_Buzy;

    public boolean isEX_Buzy() {
        return EX_Buzy;
    }

    public void setEX_Buzy(boolean EX_Buzy) {
        this.EX_Buzy = EX_Buzy;
    }

    private int opCode, rd, op1, op2, immediate, r31, r1, r2;

    public OF_EX_LatchType() {
        EX_enable = false;
    }

    public int getR1() {
        return r1;
    }

    public void setR1(int r1) {
        this.r1 = r1;
    }

    public int getR2() {
        return r2;
    }

    public void setR2(int r2) {
        this.r2 = r2;
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

    public int getOp1() {
        return op1;
    }

    public void setOp1(int op1) {
        this.op1 = op1;
    }

    public int getOp2() {
        return op2;
    }

    public void setOp2(int op2) {
        this.op2 = op2;
    }

    public int getImmediate() {
        return immediate;
    }

    public void setImmediate(int immediate) {
        this.immediate = immediate;
    }

    public int getR31() {
        return r31;
    }

    public void setR31(int r31) {
        this.r31 = r31;
    }

    public boolean isEX_enable() {
        return EX_enable;
    }

    public void setEX_enable(boolean eX_enable) {
        EX_enable = eX_enable;
    }



}
