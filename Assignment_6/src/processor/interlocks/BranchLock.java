package processor.interlocks;

import generic.Simulator;
import processor.pipeline.latch.EX_IF_LatchType;
import processor.pipeline.latch.IF_OF_LatchType;

public class BranchLock {
    IF_OF_LatchType IF_OF_Latch;
    EX_IF_LatchType EX_IF_Latch;

    public BranchLock(IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
        this.IF_OF_Latch = iF_OF_Latch;
        this.EX_IF_Latch = eX_IF_Latch;
    }

    public void validate() {
        if (EX_IF_Latch.getIsBranchTaken()) {
            IF_OF_Latch.setNop(true);
            Simulator.incNop();
        }
    }

}
