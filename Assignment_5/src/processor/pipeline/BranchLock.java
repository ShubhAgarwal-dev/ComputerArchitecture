package processor.pipeline;

import generic.Element;
import generic.Event;
import generic.Statistics;
import processor.Processor;

public class BranchLock implements Element {
    Processor containingProcessor;

    @Override
    public void handleEvent(Event event){
    }

    public BranchLock(Processor containingProcessor) {
        this.containingProcessor = containingProcessor;
    }

    public void checkBranchHazard() {
        if (this.containingProcessor.isBranchTaken()) {
            Statistics.setNumBranchHazards(Statistics.getNumBranchHazards() + 1);
            System.out.println("\t\t[Debug] (BL) DOING BRANCH LOCK");
            if (this.containingProcessor.DataLockUnit().dataLockDone > 0){
                System.out.println("\t\t[Debug] (BL) IS BEING DONE WHILE DL IS GOING ON.");
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