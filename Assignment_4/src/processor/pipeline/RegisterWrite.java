package processor.pipeline;

import generic.Misc;
import processor.Processor;

import static generic.Simulator.setSimulationComplete;

public class RegisterWrite {
    Processor containingProcessor;
    MA_RW_LatchType MA_RW_Latch;
    IF_EnableLatchType IF_EnableLatch;

    public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
        this.containingProcessor = containingProcessor;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_EnableLatch = iF_EnableLatch;
    }

    public void performRW() {
        if (MA_RW_Latch.isRW_enable()) {

            // if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);

            int opCode = MA_RW_Latch.getOpCode();
            int rd = MA_RW_Latch.getRd();
            int loadResult = MA_RW_Latch.getLoadResult();
            int opResult = MA_RW_Latch.getOpResult();

            if (opCode >= 0 && opCode <= 21) {
                containingProcessor.getRegisterFile().setValue(rd, opResult);
                System.out.println("[Debug] (RW) Write to register " + rd + " data " + opResult);

            }

            if (opCode == 22) {
				containingProcessor.getRegisterFile().setValue(rd,loadResult);
                System.out.println("[Debug] (RW) Write register " + rd + " to " + loadResult);

            }

			if(opCode>=0 && opCode<=21 && MA_RW_Latch.getR31()!=-1){
				containingProcessor.getRegisterFile().setValue(31, MA_RW_Latch.getR31());
                System.out.println("[Debug] (RW) Write to register 31 data " + MA_RW_Latch.getR31());
			}

			if(opCode==29){
//                System.out.println("End Instruction Detected");
				containingProcessor.getRegisterFile().setProgramCounter(Misc.getPC(containingProcessor)+1);
				setSimulationComplete(true);
//                System.out.println("[Debug] (RW) End instruction detected");

			}

//            MA_RW_Latch.setRW_enable(false);
//            IF_EnableLatch.setIF_enable(true);
        }
    }

}
