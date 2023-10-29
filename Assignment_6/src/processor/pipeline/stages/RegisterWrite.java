package processor.pipeline.stages;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Misc;
import generic.Simulator;
import processor.Processor;
import processor.pipeline.latch.IF_EnableLatchType;
import processor.pipeline.latch.MA_RW_LatchType;

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
            if (MA_RW_Latch.isValidInst()) {
                Instruction inst = MA_RW_Latch.getInstruction();
                MA_RW_Latch.setValidInst(false);
                if (inst != null) {
                    int ldResult = MA_RW_Latch.getLdResult();
                    int aluResult = MA_RW_Latch.getAluResult();
                    int excess = MA_RW_Latch.getExcess();
                    containingProcessor.getRegisterFile().setValue(31, excess);
                    int rd = 0;
                    if (inst.getDestinationOperand() != null) {
                        rd = inst.getDestinationOperand().getValue();
                    }

                    if (inst.getOperationType() == OperationType.add ||
                            inst.getOperationType() == OperationType.sub ||
                            inst.getOperationType() == OperationType.mul ||
                            inst.getOperationType() == OperationType.div ||
                            inst.getOperationType() == OperationType.and ||
                            inst.getOperationType() == OperationType.or ||
                            inst.getOperationType() == OperationType.xor ||
                            inst.getOperationType() == OperationType.slt ||
                            inst.getOperationType() == OperationType.sll ||
                            inst.getOperationType() == OperationType.srl ||
                            inst.getOperationType() == OperationType.sra ||
                            inst.getOperationType() == OperationType.addi ||
                            inst.getOperationType() == OperationType.subi ||
                            inst.getOperationType() == OperationType.muli ||
                            inst.getOperationType() == OperationType.divi ||
                            inst.getOperationType() == OperationType.andi ||
                            inst.getOperationType() == OperationType.ori ||
                            inst.getOperationType() == OperationType.xori ||
                            inst.getOperationType() == OperationType.slti ||
                            inst.getOperationType() == OperationType.slli ||
                            inst.getOperationType() == OperationType.srli ||
                            inst.getOperationType() == OperationType.srai) {

                        containingProcessor.getRegisterFile().setValue(rd, aluResult);

                    } else if (inst.getOperationType() == OperationType.load) {
                        containingProcessor.getRegisterFile().setValue(rd, ldResult);

                    } else if (inst.getOperationType() == OperationType.store ||
                            inst.getOperationType() == OperationType.beq ||
                            inst.getOperationType() == OperationType.bne ||
                            inst.getOperationType() == OperationType.blt ||
                            inst.getOperationType() == OperationType.bgt ||
                            inst.getOperationType() == OperationType.jmp) {
                        // Do nothing for these cases

                    } else if (inst.getOperationType() == OperationType.end) {
                        Simulator.setSimulationComplete(true);

                    } else {
                        Misc.printErrorAndExit("Unknown Instruction!!");
                    }
                }
            }
        }
        MA_RW_Latch.setRW_enable(false);
        IF_EnableLatch.setIF_enable(true);
    }
}
