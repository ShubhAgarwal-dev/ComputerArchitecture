package processor.pipeline.stages;

import generic.*;
import processor.Clock;
import processor.Processor;
import processor.pipeline.latch.EX_MA_LatchType;
import processor.pipeline.latch.MA_RW_LatchType;


public class MemoryAccess implements Element {
    Processor containingProcessor;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;


    int excess;
    Instruction instruction;

    public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch,
                        MA_RW_LatchType mA_RW_Latch) {
        this.containingProcessor = containingProcessor;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
    }

    public void performMA() {


        if (EX_MA_Latch.isMA_enable()) {

            if (!EX_MA_Latch.isMABusy()) {

                if (EX_MA_Latch.isValidInst()) {
                    Instruction inst = EX_MA_Latch.getInstruction();



                    this.instruction = inst;

                    if (inst != null) {
                        int aluResult = EX_MA_Latch.getAluResult();
                        int operand = EX_MA_Latch.getOperand();

                        this.excess = EX_MA_Latch.getExcess();


                        IfMemoryOPRequired(inst, aluResult, operand);
                    } else {


                        MA_RW_Latch.setInstruction(inst);
                        MA_RW_Latch.setValidInst(true);
                        EX_MA_Latch.setValidInst(false);
                    }
                } else {

                }
            } else {

            }

            EX_MA_Latch.setMA_enable(false);
            MA_RW_Latch.setRW_enable(true);
        } else {

        }
    }

    private void IfMemoryOPRequired(Instruction inst, int aluResult, int operand) {
        switch (inst.getOperationType()) {
            case load: {




                if (!containingProcessor.getL1dCache().isCacheBusy()) {
                    Simulator.getEventQueue()
                            .addEvent(new CacheReadEvent(Clock.getCurrentTime(),
                                    this, containingProcessor.getL1dCache(),
                                    aluResult));
                } else {

                }
                EX_MA_Latch.setMABusy(true);
                break;
            }

            case store: {




                if (!containingProcessor.getL1dCache().isCacheBusy()) {
                    Simulator.getEventQueue()
                            .addEvent(new CacheWriteEvent(Clock.getCurrentTime(),
                                    this, containingProcessor.getL1dCache(),
                                    aluResult, operand));
                } else {

                }
                EX_MA_Latch.setMABusy(true);
                break;
            }

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
            case srai:
            case beq:
            case bne:
            case blt:
            case bgt:
            case jmp:
            case end: {
                MountLatch(aluResult);
                break;
            }

            default:
                Misc.printErrorAndExit("Unknown Instruction!!");
        }
    }

    private void MountLatch(int aluResult) {
        EX_MA_Latch.setMABusy(false);
        EX_MA_Latch.setMA_enable(false);
        EX_MA_Latch.setValidInst(false);



        MA_RW_Latch.setInstruction(this.instruction);


        MA_RW_Latch.setExcess(this.excess);

        MA_RW_Latch.setAluResult(aluResult);
        MA_RW_Latch.setRW_enable(true);
        MA_RW_Latch.setValidInst(true);
    }


    @Override
    public void handleEvent(Event e) {

        CacheResponseEvent event = (CacheResponseEvent) e;

        containingProcessor.getL1dCache().setCacheBusy(false);

        EX_MA_Latch.setMABusy(false);
        EX_MA_Latch.setValidInst(false);

        MA_RW_Latch.setInstruction(this.instruction);
        MA_RW_Latch.setExcess(this.excess);
        MA_RW_Latch.setLdResult(event.getValue());
        MA_RW_Latch.setValidInst(true);
    }

}
