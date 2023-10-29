package processor.pipeline.stages;

import generic.*;
import generic.Instruction.OperationType;
import generic.event.*;
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
                        if (inst.getOperationType() == OperationType.load) {
                            if (!containingProcessor.getL1dCache().isCacheBusy()) {
                                CacheReadEvent newCacheReadEvent = new CacheReadEvent(Clock.getCurrentTime(),
                                        this, containingProcessor.getL1dCache(),
                                        aluResult);
                                Simulator.getEventQueue()
                                        .addEvent(newCacheReadEvent);
                            }
                            EX_MA_Latch.setMABusy(true);

                        } else if (inst.getOperationType() == OperationType.store) {
                            if (!containingProcessor.getL1dCache().isCacheBusy()) {
                                CacheWriteEvent newCacheWriteEvent = new CacheWriteEvent(Clock.getCurrentTime(),
                                        this, containingProcessor.getL1dCache(),
                                        aluResult, operand);
                                Simulator.getEventQueue()
                                        .addEvent(newCacheWriteEvent);
                            }
                            EX_MA_Latch.setMABusy(true);

                        } else if (inst.getOperationType() == OperationType.add ||
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
                                inst.getOperationType() == OperationType.srai ||
                                inst.getOperationType() == OperationType.beq ||
                                inst.getOperationType() == OperationType.bne ||
                                inst.getOperationType() == OperationType.blt ||
                                inst.getOperationType() == OperationType.bgt ||
                                inst.getOperationType() == OperationType.jmp ||
                                inst.getOperationType() == OperationType.end) {
                            EX_MA_Latch.setMABusy(false);
                            EX_MA_Latch.setMA_enable(false);
                            EX_MA_Latch.setValidInst(false);

                            MA_RW_Latch.setInstruction(this.instruction);
                            MA_RW_Latch.setExcess(this.excess);
                            MA_RW_Latch.setAluResult(aluResult);
                            MA_RW_Latch.setRW_enable(true);
                            MA_RW_Latch.setValidInst(true);

                        } else {
                            Misc.printErrorAndExit("Unknown Instruction!!");
                        }
                    } else {
                        MA_RW_Latch.setInstruction(inst);
                        MA_RW_Latch.setValidInst(true);
                        EX_MA_Latch.setValidInst(false);
                    }
                }
            }
            EX_MA_Latch.setMA_enable(false);
            MA_RW_Latch.setRW_enable(true);
        }
    }


    @Override
    public void handleEvent(Event e) {

        CacheResponseEvent event = (CacheResponseEvent) e;

        containingProcessor.getL1dCache().setCacheBusy(false);

        EX_MA_Latch.setMABusy(false);
        EX_MA_Latch.setValidInst(false);

        MA_RW_Latch.setInstruction(this.instruction);
        MA_RW_Latch.setExcess(this.excess);
        MA_RW_Latch.setLdResult(event.getVal());
        MA_RW_Latch.setValidInst(true);
    }

}
