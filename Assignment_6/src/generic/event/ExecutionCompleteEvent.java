package generic.event;

import generic.Instruction;

public class ExecutionCompleteEvent extends Event {


    Instruction inst;
    int aluResult, excess, op;
    boolean isBranchTaken;
    int branchPC;

    public ExecutionCompleteEvent(long eventTime, Element requestingElement,
                                  Element processingElement, Instruction inst, int aluResult, int excess, int op,
                                  boolean isBranchTaken, int branchPC) {
        super(eventTime, EventType.ExecutionComplete, requestingElement, processingElement);
        this.inst = inst;
        this.aluResult = aluResult;
        this.excess = excess;
        this.op = op;
        this.isBranchTaken = isBranchTaken;
        this.branchPC = branchPC;
    }


    public Instruction getInst() {
        return inst;
    }

    public int getAluResult() {
        return aluResult;
    }

    public int getExcess() {
        return excess;
    }

    public int getOp() {
        return op;
    }

    public boolean isBranchTaken() {
        return isBranchTaken;
    }

    public int getBranchPC() {
        return branchPC;
    }

}
