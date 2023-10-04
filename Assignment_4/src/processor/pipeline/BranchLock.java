package processor.pipeline;

import processor.Processor;

public class BranchLock {
	Processor containingProcessor;
	boolean insert_bubble;

	public void checkBrachHazard(){
		if (this.containingProcessor.isBranchTaken()){
			containingProcessor.IF_OF_Latch().setBubble(true);
			containingProcessor.OF_EX_Latch().setBubble(true);
		}
	}
}
