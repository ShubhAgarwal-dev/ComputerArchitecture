package processor.pipeline;

import processor.Processor;

public class MemoryAccess {
    Processor containingProcessor;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;

    public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
        this.containingProcessor = containingProcessor;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
    }

    public void performMA() {
        if (EX_MA_Latch.isMA_enable()) {
            int opCode = EX_MA_Latch.getOpCode();
            if (opCode == 30) {
                containingProcessor.getDataLockUnit().setInstEXString(Integer.toBinaryString(opCode) + "0".repeat(32 - Integer.toBinaryString(opCode).length()));
            }
            if (opCode != 30) {
                containingProcessor.getDataLockUnit().setInstMAString(EX_MA_Latch.getInstruction());
                MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
                if (opCode == 22) {
                    int memoLocation = EX_MA_Latch.getOpRes();
                    int loadData = containingProcessor.getMainMemory().getWord(memoLocation);
                    MA_RW_Latch.setLoadResult(loadData);
                    // System.out.println("[Debug] (MA) Load from address " + memoLocation + " data
                    // " + loadData);

                } else if (opCode == 23) {
                    int data = EX_MA_Latch.getOp1();
                    int memoLocation = EX_MA_Latch.getOpRes();
                    containingProcessor.getMainMemory().setWord(memoLocation, data);
                    // System.out.println("[Debug] (MA) Store to address " + memoLocation + " data "
                    // + data);

                }
                MA_RW_Latch.setOpResult(EX_MA_Latch.getOpRes());
                MA_RW_Latch.setR31(EX_MA_Latch.getR31());
                MA_RW_Latch.setRd(EX_MA_Latch.getRd());
            }
            // disable and enable
            MA_RW_Latch.setOpCode(EX_MA_Latch.getOpCode());
            EX_MA_Latch.setMA_enable(false);
            MA_RW_Latch.setRW_enable(true);
        }
    }
}
