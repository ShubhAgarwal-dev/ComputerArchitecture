package processor.pipeline.components;

import generic.Misc;
import generic.event.Element;
import generic.event.Event;
import processor.Processor;
import processor.pipeline.latches.EX_IF_LatchType;
import processor.pipeline.latches.IF_EnableLatchType;
import processor.pipeline.latches.IF_OF_LatchType;

public class InstructionFetch implements Element {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;

	public int endPC;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable())
		{
			System.out.println("[Debug] (IF) Running IF stage.");
			if(containingProcessor.isBranchTaken()){
				System.out.println("[Debug] (IF) BRANCH PC: " + containingProcessor.getBranchPC());
				containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getBranchPC());
				containingProcessor.setBranchTaken(false);

			}else{
				containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter()+1);

			}
			int currentPC = Misc.getPC(containingProcessor);
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			System.out.println("[Debug] (IF) inst: "+newInstruction);
			if ((Integer.toBinaryString(newInstruction)+"0".repeat(32-Integer.toBinaryString(newInstruction).length())).startsWith("11101")){
				this.endPC = currentPC;
			}

			IF_OF_Latch.setInstruction(newInstruction);

			IF_OF_Latch.setOF_enable(true);
		} else {
			this.containingProcessor.DataLockUnit().dataLockDone += 1;
			System.out.println("[Debug] (IF) STALL: " + this.containingProcessor.DataLockUnit().dataLockDone);
		}
	}

	@Override
	public void handleEvent(Event event) {
	}
}
