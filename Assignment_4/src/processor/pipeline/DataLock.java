package processor.pipeline;

import processor.Processor;

public class DataLock {
    boolean isSrc1 = true;
    int src11= 0;
    boolean isSrc2 = false;
    int src21 = 0;
    int des1,des2,des3;
    Processor containingProcessor;
    public int dataLockDone;

    private void init(){
        this.src21 = 0;
        this.src11 = 0;
        this.des1 = 50;
        this.des2 = 50;
        this.des3 = 50;
    }

    public DataLock(Processor containingProcessor) {
        this.containingProcessor = containingProcessor;
        this.init();
        this.dataLockDone = 0;
    }

    public void setSrcDest() {
        int opCode = this.containingProcessor.OF_EX_Latch().getOpCode();
        this.isSrc1 = true;
        this.src11 = this.containingProcessor.OF_EX_Latch().getR1();
        this.isSrc2  = false;
        this.src21 = 0;
        this.des1 = this.containingProcessor.OF_EX_Latch().getRd();
        if ((opCode <= 21 && (opCode % 2 == 0))) {
            this.src21 = this.containingProcessor.OF_EX_Latch().getR2();
            this.isSrc2 = true;
        } else if (opCode == 24 || opCode == 29) { // jmp and end
            this.src11 = 0;
            this.isSrc1 = false;
        } else if (opCode == 23) { // store
            this.des1 = this.src11;
            this.src11 = 0;
            this.isSrc1 = false;
        } else if (opCode >= 25 && opCode <= 28 ) { // for compare instructions
            this.isSrc2 = true;
            this.src21 = this.des1;
        }
        System.out.println("[Debug] (DL) rd:" + this.des1);
        if (this.isSrc1) { System.out.println("[Debug] (DL) rs1:" + this.src11); }
        if (this.isSrc2) { System.out.println("[Debug] (DL) rs2:" + this.src21); }
//        System.out.println("\n");
    }

    private boolean checkDataHazard() {
        boolean res = false;
        if (this.isSrc1) {
            res = this.des2 == this.src11 || this.des3 == this.src11;
        }
        if (this.isSrc2) {
            return res || this.des2 == this.src21 || this.des3 == this.src21;
        }
        return res;
    }

    public void performAppend()  {
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
        System.out.println("[Debug] (DL): DATA LOCK IS EXECUTNG RN << WHY WHY WHY");
    }

    public void DLU(){
        System.out.println("[Debug] (DL): DLU is RUNNING.");
        this.setSrcDest();
        this.dataLockDone = 0;
//        if (this.checkDataHazard()) { this.performLock(); this.dataLockDone += 1; }
        if (this.checkDataHazard()) {
            this.performLock();
            this.init();
        }
        this.performAppend();
    }
}
