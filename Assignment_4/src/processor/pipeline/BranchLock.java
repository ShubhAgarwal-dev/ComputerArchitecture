package processor.pipeline;

import processor.Processor;

public class BranchLock {
    Processor containingProcessor;

    public BranchLock(Processor containingProcessor) {
        this.containingProcessor = containingProcessor;
    }

    public void checkBranchHazard() {
        if (this.containingProcessor.isBranchTaken()) {
            System.out.println("DOING BRANCH LOCK");
            this.containingProcessor.IF_OF_Latch().setInstruction(0);
            this.containingProcessor.OF_EX_Latch().setOpCode(0);
            this.containingProcessor.OF_EX_Latch().setOp1(0);
            this.containingProcessor.OF_EX_Latch().setOp2(0);
            this.containingProcessor.OF_EX_Latch().setR31(0);
            this.containingProcessor.OF_EX_Latch().setRd(0);
        }
    }
}