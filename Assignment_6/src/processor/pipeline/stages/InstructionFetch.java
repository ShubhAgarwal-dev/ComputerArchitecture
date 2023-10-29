package processor.pipeline.stages;

import generic.*;
import generic.event.CacheReadEvent;
import generic.event.CacheResponseEvent;
import generic.event.Element;
import generic.event.Event;
import processor.Clock;
import processor.Processor;
import processor.pipeline.latch.EX_IF_LatchType;
import processor.pipeline.latch.IF_EnableLatchType;
import processor.pipeline.latch.IF_OF_LatchType;


public class InstructionFetch implements Element {
    Processor containingProcessor;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    EX_IF_LatchType EX_IF_Latch;

    int previousPC;

    public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
                            IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
        this.containingProcessor = containingProcessor;
        this.IF_EnableLatch = iF_EnableLatch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.EX_IF_Latch = eX_IF_Latch;

        previousPC = 0;
    }


    public void performIF() {
        if (IF_EnableLatch.isIF_enable()) {
            if (!IF_EnableLatch.isIFBusy()) {
                if (!IF_EnableLatch.getStall()) {
                    if (!containingProcessor.getL1iCache().isCacheBusy()) {
                        if (EX_IF_Latch.getIsBranchTaken()) {
                            containingProcessor.getRegisterFile()
                                    .setProgramCounter(EX_IF_Latch.getBranchPC());
                            EX_IF_Latch.setIsBranchTaken(false);
                        }
                        int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
                        CacheReadEvent newCacheReadEvent = new CacheReadEvent(Clock.getCurrentTime(), this,
                                containingProcessor.getL1iCache(), currentPC);
                        Simulator.getEventQueue()
                                .addEvent(newCacheReadEvent);
                        IF_EnableLatch.setIFBusy(true);
                        previousPC = currentPC;
                        containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
                        Simulator.updateNumberOfInstructions();
                    }
                }
            }
            IF_OF_Latch.setOF_enable(true);
        }
    }


    @Override
    public void handleEvent(Event e) {
        if (IF_OF_Latch.isOFBusy()) {
            e.setEventTime(Clock.getCurrentTime() + 1);
            Simulator.getEventQueue().addEvent(e);
        } else if (IF_OF_Latch.getNop()) {
            containingProcessor.getL1iCache().setCacheBusy(false);
            containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getBranchPC());
            mountNOP();
        } else {
            CacheResponseEvent event = (CacheResponseEvent) e;
            containingProcessor.getL1iCache().setCacheBusy(false);
            mountLatch(event);
        }
    }

    private void mountLatch(CacheResponseEvent event) {
        IF_EnableLatch.setIFBusy(false);
        IF_OF_Latch.setInstruction(event.getVal());
        IF_OF_Latch.setValidInst(true);
        IF_OF_Latch.setNop(false);
        IF_OF_Latch.setCurrentPC(previousPC);
        IF_OF_Latch.setOF_enable(true);
        Statistics.setDynamicInstCount(Statistics.getDynamicInstCount() + 1);
    }

    private void mountNOP() {
        IF_EnableLatch.setIFBusy(false);
        IF_OF_Latch.setValidInst(false);
        IF_OF_Latch.setNop(false);
        IF_OF_Latch.setOF_enable(true);
        EX_IF_Latch.setIsBranchTaken(false);
    }

}
