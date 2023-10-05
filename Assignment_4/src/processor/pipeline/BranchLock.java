package processor.pipeline;

import processor.Processor;

public class BranchLock {
    Processor containingProcessor;
//	boolean insert_bubble;

    public BranchLock(Processor containingProcessor) {
        this.containingProcessor = containingProcessor;
    }

    public void checkBrachHazard() {
        if (this.containingProcessor.isBranchTaken()) {
//			containingProcessor.IF_OF_Latch().setBubble(true);
//			containingProcessor.OF_EX_Latch().setBubble(true);
            containingProcessor.IF_OF_Latch().setInstruction(0);
            containingProcessor.OF_EX_Latch().setOpCode(0);
            containingProcessor.OF_EX_Latch().setR1(0);
            containingProcessor.OF_EX_Latch().setR2(0);
            containingProcessor.OF_EX_Latch().setR31(0);
            containingProcessor.OF_EX_Latch().setRd(0);
        }
    }
}
