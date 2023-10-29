package processor.pipeline.latch;

import generic.Instruction;

public class EX_MA_LatchType {

    boolean MA_enable;
    Instruction inst;
    int aluResult, excess, op;



    boolean isMABusy, isValidInst;

    public EX_MA_LatchType() {
        MA_enable = false;
        inst = null;
        aluResult = op = excess = 0;
        isMABusy = isValidInst = false;
    }

    public boolean isMA_enable() {
        return MA_enable;
    }

    public void setMA_enable(boolean mA_enable) {
        MA_enable = mA_enable;
    }



    public Instruction getInstruction() {
        return inst;
    }

    public void setInstruction(Instruction newIns) {
        inst = newIns;
    }

    public int getAluResult() {
        return aluResult;
    }

    public void setAluResult(int result) {
        aluResult = result;
    }

    public int getExcess() {
        return excess;
    }

    public void setExcess(int exc) {
        excess = exc;
    }

    public int getOperand() {
        return op;
    }

    public void setOperand(int operand) {
        op = operand;
    }

    public boolean isMABusy() {
        return isMABusy;
    }

    public void setMABusy(boolean isMABusy) {
        this.isMABusy = isMABusy;
    }

    public boolean isValidInst() {
        return isValidInst;
    }

    public void setValidInst(boolean isValidInst) {
        this.isValidInst = isValidInst;
    }

}
