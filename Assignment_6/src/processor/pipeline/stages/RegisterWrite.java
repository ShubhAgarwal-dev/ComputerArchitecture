package processor.pipeline.stages;

import generic.Instruction;
import generic.Misc;
import generic.Simulator;
import processor.Processor;
import processor.pipeline.latch.IF_EnableLatchType;
import processor.pipeline.latch.MA_RW_LatchType;

public class RegisterWrite {
    Processor containingProcessor;
    MA_RW_LatchType MA_RW_Latch;
    IF_EnableLatchType IF_EnableLatch;

    public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch,
                         IF_EnableLatchType iF_EnableLatch) {
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

                    switch (inst.getOperationType()) {

                        case add:
                        case sub:
                        case mul:
                        case div:
                        case and:
                        case or:
                        case xor:
                        case slt:
                        case sll:
                        case srl:
                        case sra:

                        case addi:
                        case subi:
                        case muli:
                        case divi:
                        case andi:
                        case ori:
                        case xori:
                        case slti:
                        case slli:
                        case srli:
                        case srai: {

                            containingProcessor.getRegisterFile().setValue(rd, aluResult);
                            break;
                        }

                        case load: {

                            containingProcessor.getRegisterFile().setValue(rd, ldResult);
                            break;
                        }

                        case store:
                        case beq:
                        case bne:
                        case blt:
                        case bgt:

                        case jmp:
                            break;

                        case end: {

                            Simulator.setSimulationComplete(true);
                            break;
                        }

                        default:
                            Misc.printErrorAndExit("Unknown Instruction!!");
                    }
                } else {

                }

            } else {

            }

            MA_RW_Latch.setRW_enable(false);
            IF_EnableLatch.setIF_enable(true);
        } else {

        }
    }

}
