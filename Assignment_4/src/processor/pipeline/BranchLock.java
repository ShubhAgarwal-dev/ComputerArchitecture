package processor.pipeline;

import processor.Processor;

public class BranchLock {
    Processor containingProcessor;

    public BranchLock(Processor containingProcessor) {
        this.containingProcessor = containingProcessor;
    }

    public void checkBranchHazard() {
        if (this.containingProcessor.isBranchTaken()) {
            System.out.println("\t\t[Debug] (BL) DOING BRANCH LOCK");
            if (this.containingProcessor.DataLockUnit().dataLockDone > 0){
                System.out.println("\t\t\t\t[Debug] (BL) IS BEING DONE WHILE DL IS GOING ON.");
                // What I want to do now is that I want to change the content inside the OF Stage which I can not access because of some MF
//                this.containingProcessor.getOFUnit().
            }
            this.containingProcessor.IF_OF_Latch().setInstruction(0);
            this.containingProcessor.OF_EX_Latch().setOpCode(0);
            this.containingProcessor.OF_EX_Latch().setOp1(0);
            this.containingProcessor.OF_EX_Latch().setOp2(0);
            this.containingProcessor.OF_EX_Latch().setR31(0);
            this.containingProcessor.OF_EX_Latch().setRd(0);
        }
    }
}