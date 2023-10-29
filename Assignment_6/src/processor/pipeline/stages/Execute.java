package processor.pipeline.stages;

import configuration.Configuration;
import generic.*;
import generic.Instruction.OperationType;
import processor.Clock;
import processor.Processor;
import processor.pipeline.latch.EX_IF_LatchType;
import processor.pipeline.latch.EX_MA_LatchType;
import processor.pipeline.latch.OF_EX_LatchType;


public class Execute implements Element {
    Processor containingProcessor;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    EX_IF_LatchType EX_IF_Latch;
    int aluRes, overFlow, opCode;
    boolean isBranchTaken;
    int branchPC;

    public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch) {
        this.containingProcessor = containingProcessor;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
        this.EX_IF_Latch = eX_IF_Latch;
    }

    public void performEX() {


        if (OF_EX_Latch.isEX_enable()) {
            if (!OF_EX_Latch.isEXBusy()) {
                if (!EX_MA_Latch.isMABusy()) {
                    OF_EX_Latch.setEXMABusy(false);
                    if (OF_EX_Latch.isValidInst()) {
                        Instruction inst = OF_EX_Latch.getInstruction();
                        if (inst != null) {
                            executeInstruction(inst);
                            eventScheduler(inst);
                            OF_EX_Latch.setValidInst(false);
                        } else {
                            EX_MA_Latch.setInstruction(null);
                            EX_MA_Latch.setValidInst(true);
                            EX_IF_Latch.setIsBranchTaken(false);
                            OF_EX_Latch.setValidInst(false);
                        }
                    }
                } else {
                    OF_EX_Latch.setEXMABusy(true);
                }
            }
            OF_EX_Latch.setEX_enable(false);
            EX_MA_Latch.setMA_enable(true);
        }
    }


    private void executeInstruction(Instruction inst) {
        long op1 = OF_EX_Latch.getOperand1();
        long op2 = OF_EX_Latch.getOperand2();
        long imm = OF_EX_Latch.getImmediate();
        long second = (OF_EX_Latch.getIsImmediate()) ? imm : op2;
        if (inst.getOperationType() == OperationType.add || inst.getOperationType() == OperationType.addi) {
            this.aluRes = getResult(op1 + second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.sub || inst.getOperationType() == OperationType.subi) {
            this.aluRes = getResult(op1 - second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.mul || inst.getOperationType() == OperationType.muli) {
            this.aluRes = getResult(op1 * second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.div || inst.getOperationType() == OperationType.divi) {
            this.aluRes = getResult(op1 / second);
            this.overFlow = (int) (op1 % second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.and || inst.getOperationType() == OperationType.andi) {
            this.aluRes = getResult(op1 & second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.or || inst.getOperationType() == OperationType.ori) {
            this.aluRes = getResult(op1 | second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.xor || inst.getOperationType() == OperationType.xori) {
            this.aluRes = getResult(op1 ^ second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.slt || inst.getOperationType() == OperationType.slti) {
            this.aluRes = (op1 < second) ? 1 : 0;
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.sll || inst.getOperationType() == OperationType.slli) {
            this.aluRes = getResult(op1 << second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.srl || inst.getOperationType() == OperationType.srli) {
            this.aluRes = getResult(op1 >>> second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.sra || inst.getOperationType() == OperationType.srai) {
            this.aluRes = getResult(op1 >> second);
            this.isBranchTaken = false;

        } else if (inst.getOperationType() == OperationType.load) {
            executeLoad(op1, imm);

        } else if (inst.getOperationType() == OperationType.store) {
            executeStore(op2, imm, (int) op1);

        } else if (inst.getOperationType() == OperationType.beq) {
            executeComp(op1 == op2);

        } else if (inst.getOperationType() == OperationType.bne) {
            executeComp(op1 != op2);

        } else if (inst.getOperationType() == OperationType.blt) {
            executeComp(op1 < op2);

        } else if (inst.getOperationType() == OperationType.bgt) {
            executeComp(op1 > op2);

        } else if (inst.getOperationType() == OperationType.jmp) {
            executeJmp();

        } else if (inst.getOperationType() == OperationType.end) {
            EX_IF_Latch.setIsBranchTaken(false);
            this.isBranchTaken = false;

        } else {
            Misc.printErrorAndExit("Unknown Instruction!!");
        }
    }

    private void executeLoad(long op1, long imm) {
        this.aluRes = getResult(op1 + imm);
        this.isBranchTaken = false;
    }

    private void executeStore(long op2, long imm, int op1) {
        this.aluRes = getResult(op2 + imm);
        this.opCode = op1;
        this.isBranchTaken = false;
    }

    private void executeComp(boolean op1) {
        if (op1) {
            this.isBranchTaken = true;
            this.branchPC = OF_EX_Latch.getBranchTarget();
        } else {
            this.isBranchTaken = false;
        }
    }

    private void executeJmp() {
        EX_IF_Latch.setIsBranchTaken(true);
        EX_IF_Latch.setBranchPC(OF_EX_Latch.getBranchTarget());
        this.isBranchTaken = true;
        this.branchPC = OF_EX_Latch.getBranchTarget();
    }


    private int getResult(long res) {
        String binaryString = Long.toBinaryString(res);
        if (binaryString.length() <= 32) {
            return (int) res;

        } else {


            this.overFlow = binaryToDecimal(binaryString.substring(0, binaryString.length() - 32), (res < 0));

            return binaryToDecimal(binaryString.substring(binaryString.length() - 32), (res < 0));
        }
    }


    private int binaryToDecimal(String binaryString, boolean isSigned) {
        int radixVal = 2;
        if (!isSigned) {
            return Integer.parseInt(binaryString, radixVal);
        } else {
            String copyString = '0' + binaryString.substring(1);
            int ans = Integer.parseInt(copyString, radixVal);
            if (binaryString.length() == 32) {
                int power = (1 << 30);
                if (binaryString.charAt(0) == '1') {
                    ans -= power;
                    ans -= power;
                }
            } else {
                int power = (1 << (binaryString.length() - 1));
                if (binaryString.charAt(0) == '1') {
                    ans -= power;
                }
            }
            return ans;
        }
    }


    private void eventScheduler(Instruction inst) {
        if (inst.getOperationType() == OperationType.mul || inst.getOperationType() == OperationType.muli) {
            ExecutionCompleteEvent newExecCompleteEvent = new ExecutionCompleteEvent(Clock.getCurrentTime() + Configuration.multiplier_latency, this, this, inst, this.aluRes, this.overFlow, this.opCode, this.isBranchTaken, this.branchPC);
            Simulator.getEventQueue().addEvent(newExecCompleteEvent);
            OF_EX_Latch.setEXBusy(true);
        } else if (inst.getOperationType() == OperationType.div || inst.getOperationType() == OperationType.divi) {
            ExecutionCompleteEvent newExecCompleteEvent = new ExecutionCompleteEvent(Clock.getCurrentTime() + Configuration.divider_latency, this, this, inst, this.aluRes, this.overFlow, this.opCode, this.isBranchTaken, this.branchPC);
            Simulator.getEventQueue().addEvent(newExecCompleteEvent);
            OF_EX_Latch.setEXBusy(true);
        } else if (inst.getOperationType() == OperationType.add || inst.getOperationType() == OperationType.sub || inst.getOperationType() == OperationType.and || inst.getOperationType() == OperationType.or || inst.getOperationType() == OperationType.xor || inst.getOperationType() == OperationType.slt || inst.getOperationType() == OperationType.sll || inst.getOperationType() == OperationType.srl || inst.getOperationType() == OperationType.sra || inst.getOperationType() == OperationType.addi || inst.getOperationType() == OperationType.subi || inst.getOperationType() == OperationType.andi || inst.getOperationType() == OperationType.ori || inst.getOperationType() == OperationType.xori || inst.getOperationType() == OperationType.slti || inst.getOperationType() == OperationType.slli || inst.getOperationType() == OperationType.srli || inst.getOperationType() == OperationType.srai || inst.getOperationType() == OperationType.load || inst.getOperationType() == OperationType.store || inst.getOperationType() == OperationType.beq || inst.getOperationType() == OperationType.bne || inst.getOperationType() == OperationType.blt || inst.getOperationType() == OperationType.bgt) {
            ExecutionCompleteEvent newExecCompleteEvent = new ExecutionCompleteEvent(Clock.getCurrentTime() + Configuration.ALU_latency, this, this, inst, this.aluRes, this.overFlow, this.opCode, this.isBranchTaken, this.branchPC);
            Simulator.getEventQueue().addEvent(newExecCompleteEvent);
            OF_EX_Latch.setEXBusy(true);
        } else if (inst.getOperationType() == OperationType.jmp || inst.getOperationType() == OperationType.end) {
            containingProcessor.getControlInterlockUnit().validate();
            mountLatch(inst);
        } else {
            Misc.printErrorAndExit("Unknown Instruction!!");
        }
    }

    private void mountLatch(Instruction inst) {
        OF_EX_Latch.setEXBusy(false);
        OF_EX_Latch.setValidInst(false);
        OF_EX_Latch.setEX_enable(false);

        EX_MA_Latch.setValidInst(true);
        EX_MA_Latch.setInstruction(inst);
        EX_MA_Latch.setMA_enable(true);
    }
    @Override
    public void handleEvent(Event e) {
        if (EX_MA_Latch.isMABusy()) {
            e.setEventTime(Clock.getCurrentTime() + 1);
            Simulator.getEventQueue().addEvent(e);
        } else {
            ExecutionCompleteEvent event = (ExecutionCompleteEvent) e;
            OF_EX_Latch.setEXBusy(false);
            EX_MA_Latch.setMA_enable(true);
            EX_MA_Latch.setValidInst(true);
            OF_EX_Latch.setValidInst(false);
            OF_EX_Latch.setEX_enable(false);
            EX_IF_Latch.setIsBranchTaken(event.isBranchTaken());
            EX_IF_Latch.setBranchPC(event.getBranchPC());
            EX_MA_Latch.setInstruction(event.getInst());
            EX_MA_Latch.setAluResult(event.getAluResult());
            EX_MA_Latch.setExcess(event.getExcess());
            EX_MA_Latch.setOperand(event.getOp());
            containingProcessor.getControlInterlockUnit().validate();
        }
    }

}
