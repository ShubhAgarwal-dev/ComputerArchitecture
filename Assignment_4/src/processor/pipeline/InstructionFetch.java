package processor.pipeline;

import generic.Misc;
import processor.Processor;

public class InstructionFetch {
	
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
//				System.out.println("[Debug] (IF) Branch taken, PC updated to " + containingProcessor.getBranchPC());

			}else{
				containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter()+1);
//				System.out.println("[Debug] (IF) PC incremented to " + containingProcessor.getRegisterFile().getProgramCounter());

			}
			// Getting the PC
			int currentPC = Misc.getPC(containingProcessor);
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			System.out.println("[Debug] (IF) inst: "+newInstruction);
			if ((Integer.toBinaryString(newInstruction)+"0".repeat(32-Integer.toBinaryString(newInstruction).length())).substring(0, 5).equals("11101")){
				this.endPC = currentPC;
			}

			//Updating the processor and the latch
			IF_OF_Latch.setInstruction(newInstruction);

			// disabling and enabling latches
//			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
		} else {
			this.containingProcessor.DataLockUnit().dataLockDone += 1;
			System.out.println("[Debug] (IF) STALL: " + this.containingProcessor.DataLockUnit().dataLockDone);
		}
	}

}
