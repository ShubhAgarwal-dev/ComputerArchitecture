package processor.locks;

import generic.Element;
import generic.Event;
import generic.Simulator;
import generic.Statistics;
import generic.events.MemoryResponseEvent;
import processor.Clock;
import processor.Processor;

public class BranchLock implements Element {
    Processor containingProcessor;

    public BranchLock(Processor containingProcessor) {
        this.containingProcessor = containingProcessor;
    }

    @Override
    public void handleEvent(Event event) {
    }

    public void checkBranchHazard() {
        if (this.containingProcessor.isBranchTaken()) {
            Statistics.setNumBranchHazards(Statistics.getNumBranchHazards() + 1);
            System.out.println("\t\t[Debug][BL] DOING BRANCH LOCK");
            if (this.containingProcessor.DataLockUnit().dataLockDone > 0) {
                System.out.println("\t\t[Debug][BL] IS BEING DONE WHILE DL IS GOING ON.");
            }
//            this.containingProcessor.IF_OF_Latch().setInstruction(0);
            Simulator.getEventQueue().addEvent(new MemoryResponseEvent(Clock.getCurrentTime()-10, this, containingProcessor.getIFUnit(), 0));
//            this.containingProcessor.OF_EX_Latch().setOpCode(0);
//            this.containingProcessor.OF_EX_Latch().setOp1(0);
//            this.containingProcessor.OF_EX_Latch().setOp2(0);
//            this.containingProcessor.OF_EX_Latch().setR31(0);
//            this.containingProcessor.OF_EX_Latch().setRd(0);
        }
    }
}