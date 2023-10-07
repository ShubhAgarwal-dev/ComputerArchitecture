package processor.pipeline;

import processor.Processor;

public class DataLock {
//    int src11,src12, src13;
    boolean isSrc1 = true;
    int src11= 0;
//    int src21, src22, src23;
    boolean isSrc2 = false;
    int src21 = 0;
    int des1,des2,des3;
    Processor containingProcessor;

    public DataLock(Processor containingProcessor) {
        this.containingProcessor = containingProcessor;
////        this.src11 = 0;
////        this.src12 = 0;
////        this.src13 = 0;
////        this.src21 = 0;
////        this.src22 = 0;
////        this.src23 = 0;
        this.des1 =  0;
        this.des2 =  0;
        this.des3 = 0;
    }

    private void setSrcDest() {
        int opCode = this.containingProcessor.OF_EX_Latch().getOpCode();
        this.isSrc1 = true;
        this.src11 = this.containingProcessor.OF_EX_Latch().getOp1();
        this.isSrc2  = false;
        this.src21 = 0;
        this.des1 = this.containingProcessor.OF_EX_Latch().getRd();
        if ((opCode <= 21 && (opCode % 2 == 0))) {
            this.src21 = this.containingProcessor.OF_EX_Latch().getOp2();
            this.isSrc2 = true;
        } else if (opCode == 24 || opCode == 29) { // jmp and end
            this.src11 = 0;
            this.isSrc1 = false;
        } else if (opCode == 23) { // store
            this.des1 = this.src11;
            this.src11 = 0;
            this.isSrc1 = false;
        }
    }

    private boolean checkDataHazard() {
        if (this.isSrc1) {
            return this.des2 == this.src11 || this.des3 == this.src11;
        } else if (this.isSrc2) {
            return this.des2 == this.src21 || this.des3 == this.src21;
        }
        return false;
    }

    private void performAppend()  {
        this.des3 = this.des2;
        this.des2 = this.des1;
    }

    private void performLock() {
        this.containingProcessor.IF_EnableLatch().setIF_enable(false);
        this.containingProcessor.IF_OF_Latch().setOF_enable(false);
        this.containingProcessor.OF_EX_Latch().setOpCode(0);
        this.containingProcessor.OF_EX_Latch().setOp1(0);
        this.containingProcessor.OF_EX_Latch().setOp2(0);
        this.containingProcessor.OF_EX_Latch().setR31(0);
        this.containingProcessor.OF_EX_Latch().setRd(0);
    }

    public void DLU(){
        this.setSrcDest();
        if (this.checkDataHazard()) {
            this.performLock();
        }
        this.performAppend();
    }
}
