package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.Misc;
import generic.Simulator;
import generic.events.MemoryReadEvent;
import generic.events.MemoryResponseEvent;
import processor.Clock;
import processor.Processor;
import processor.latches.EX_IF_LatchType;
import processor.latches.IF_EnableLatchType;
import processor.latches.IF_OF_LatchType;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;

public class InstructionFetch implements Element {

    public IF_OF_LatchType IF_OF_Latch;
    public int endPC;
    Processor containingProcessor;
    IF_EnableLatchType IF_EnableLatch;
    EX_IF_LatchType EX_IF_Latch;

    public MemoryReadEvent currEvent;


    public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
        this.containingProcessor = containingProcessor;
        this.IF_EnableLatch = iF_EnableLatch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.EX_IF_Latch = eX_IF_Latch;
    }

    public void performIF() {
        if (IF_EnableLatch.isIF_enable()) {
            if (IF_EnableLatch.isIF_Buzy()) {
                System.out.println("[Debug][IF] IF Busy.");
                return;
            }
            System.out.println("[Debug][IF] Running IF stage.");
            if (containingProcessor.isBranchTaken()) {
                System.out.println("[Debug][IF] BRANCH PC: " + containingProcessor.getBranchPC());
                containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getBranchPC());
                containingProcessor.setBranchTaken(false);

            } else {
                containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() + 1);

            }
            int currentPC = Misc.getPC(containingProcessor);
            currEvent = new MemoryReadEvent(
                    Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                    this,
                    containingProcessor.getMainMemory(),
                    currentPC
            );
            int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
            if ((Integer.toBinaryString(newInstruction)+"0".repeat(32-Integer.toBinaryString(newInstruction).length())).substring(0, 5).equals("11101")){
                this.endPC = currentPC;
            }
            Simulator.getEventQueue().addEvent(currEvent);
            System.out.println("[Debug][IF] Memory Read Event Added");
            System.out.println("[Debug][IF] Current PC: "+currentPC);
            IF_EnableLatch.setIF_Buzy(true);
        } else {
            this.containingProcessor.DataLockUnit().dataLockDone += 1;
            System.out.println("[Debug][IF] STALL: " + this.containingProcessor.DataLockUnit().dataLockDone);
        }
    }

    @Override
    public void handleEvent(Event event) {
        if (IF_OF_Latch.isOF_Buzy()) {
            System.out.println("[DEBUG][IF] Handling IF OF Busy");
            event.setEventTime(Clock.getCurrentTime() + 1);
            Simulator.getEventQueue().addEvent(event);
        } else {
            MemoryResponseEvent e = (MemoryResponseEvent) event;
            IF_OF_Latch.setInstruction(e.getValue());
            System.out.println("[DEBUG][IF] Handling IF Memory Response");
            System.out.println("[DEBUG][IF] Instruction: "+e.getValue());
            IF_OF_Latch.setOF_enable(true);
            IF_EnableLatch.setIF_Buzy(false);
        }
    }

}
