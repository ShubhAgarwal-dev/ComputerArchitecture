package processor.pipeline.latch;

import generic.Instruction;

public class OF_EX_LatchType {

    boolean EX_enable;
    Instruction inst;

    int operand1, operand2, immediate, branchTarget;

    boolean isImmediate;




    boolean isEXBusy, isEXMABusy, isValidInst;

    public OF_EX_LatchType() {
        EX_enable = false;
        inst = null;
        operand1 = operand2 = immediate = branchTarget = 0;
        isImmediate = false;
        isEXBusy = isEXMABusy = isValidInst = false;
    }

    public boolean isEX_enable() {
        return EX_enable;
    }

    public void setEX_enable(boolean eX_enable) {
        EX_enable = eX_enable;
    }



    public Instruction getInstruction() {
        return inst;
    }

    public void setInstruction(Instruction newInst) {
        inst = newInst;
    }

    public int getOperand1() {
        return operand1;
    }

    public void setOperand1(int op1) {
        operand1 = op1;
    }

    public int getOperand2() {
        return operand2;
    }

    public void setOperand2(int op2) {
        operand2 = op2;
    }

    public int getImmediate() {
        return immediate;
    }

    public void setImmediate(int imm) {
        immediate = imm;
    }

    public int getBranchTarget() {
        return branchTarget;
    }

    public void setBranchTarget(int target) {
        branchTarget = target;
    }

    public boolean getIsImmediate() {
        return isImmediate;
    }

    public void setIsImmediate(boolean isImm) {
        isImmediate = isImm;
    }

    public boolean isEXBusy() {
        return isEXBusy;
    }

    public void setEXBusy(boolean isEXBusy) {
        this.isEXBusy = isEXBusy;
    }

    public boolean isEXMABusy() {
        return isEXMABusy;
    }

    public void setEXMABusy(boolean isEXMABusy) {
        this.isEXMABusy = isEXMABusy;
    }

    public boolean isValidInst() {
        return isValidInst;
    }

    public void setValidInst(boolean isValidInst) {
        this.isValidInst = isValidInst;
    }

}
