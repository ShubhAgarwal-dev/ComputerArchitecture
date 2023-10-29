package processor.pipeline.stages;

import configuration.Configuration;
import generic.*;
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


    int aluResult, excess, op;
    boolean isBranchTaken;
    int branchPC;

    public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch,
                   EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch) {
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


                            compute(inst);
                            scheduleEvent(inst);
                            OF_EX_Latch.setValidInst(false);

                        } else {


                            EX_MA_Latch.setInstruction(inst);

                            EX_MA_Latch.setValidInst(true);

                            EX_IF_Latch.setIsBranchTaken(false);

                            OF_EX_Latch.setValidInst(false);
                        }
                    } else {

                    }
                } else {


                    OF_EX_Latch.setEXMABusy(true);
                }
            } else {

            }

            OF_EX_Latch.setEX_enable(false);
            EX_MA_Latch.setMA_enable(true);
        } else {

        }
    }



    private void compute(Instruction inst) {



        long op1 = OF_EX_Latch.getOperand1();
        long op2 = OF_EX_Latch.getOperand2();
        long imm = OF_EX_Latch.getImmediate();
        long second = (OF_EX_Latch.getIsImmediate()) ? imm : op2;



        switch (inst.getOperationType()) {
            case add:
            case addi: {


                this.aluResult = getResult(op1 + second);
                this.isBranchTaken = false;
                break;
            }

            case sub:
            case subi: {


                this.aluResult = getResult(op1 - second);
                this.isBranchTaken = false;
                break;
            }

            case mul:
            case muli: {


                this.aluResult = getResult(op1 * second);
                this.isBranchTaken = false;
                break;
            }

            case div:
            case divi: {



                this.aluResult = getResult(op1 / second);
                this.excess = (int) (op1 % second);
                this.isBranchTaken = false;
                break;
            }

            case and:
            case andi: {


                this.aluResult = getResult(op1 & second);
                this.isBranchTaken = false;
                break;
            }

            case or:
            case ori: {


                this.aluResult = getResult(op1 | second);
                this.isBranchTaken = false;
                break;
            }

            case xor:
            case xori: {


                this.aluResult = getResult(op1 ^ second);
                this.isBranchTaken = false;
                break;
            }

            case slt:
            case slti: {


                this.aluResult = (op1 < second) ? 1 : 0;
                this.isBranchTaken = false;
                break;
            }

            case sll:
            case slli: {


                this.aluResult = getResult(op1 << second);
                this.isBranchTaken = false;
                break;
            }

            case srl:
            case srli: {


                this.aluResult = getResult(op1 >>> second);
                this.isBranchTaken = false;
                break;
            }

            case sra:
            case srai: {


                this.aluResult = getResult(op1 >> second);
                this.isBranchTaken = false;
                break;
            }

            case load: {


                this.aluResult = getResult(op1 + imm);
                this.isBranchTaken = false;
                break;
            }

            case store: {



                this.aluResult = getResult(op2 + imm);
                this.op = (int) op1;
                this.isBranchTaken = false;
                break;
            }

            case beq: {
                if (op1 == op2) {



                    this.isBranchTaken = true;
                    this.branchPC = OF_EX_Latch.getBranchTarget();
                } else {

                    this.isBranchTaken = false;
                }
                break;
            }

            case bne: {
                if (op1 != op2) {



                    this.isBranchTaken = true;
                    this.branchPC = OF_EX_Latch.getBranchTarget();
                } else {

                    this.isBranchTaken = false;
                }
                break;
            }

            case blt: {
                if (op1 < op2) {



                    this.isBranchTaken = true;
                    this.branchPC = OF_EX_Latch.getBranchTarget();
                } else {

                    this.isBranchTaken = false;
                }
                break;
            }

            case bgt: {
                if (op1 > op2) {



                    this.isBranchTaken = true;
                    this.branchPC = OF_EX_Latch.getBranchTarget();
                } else {

                    this.isBranchTaken = false;
                }
                break;
            }

            case jmp: {
                EX_IF_Latch.setIsBranchTaken(true);
                EX_IF_Latch.setBranchPC(OF_EX_Latch.getBranchTarget());
                this.isBranchTaken = true;
                this.branchPC = OF_EX_Latch.getBranchTarget();
                break;
            }


            case end: {
                EX_IF_Latch.setIsBranchTaken(false);
                this.isBranchTaken = false;
                break;
            }

            default:
                Misc.printErrorAndExit("Unknown Instruction!!");
        }


    }


    private int getResult(long res) {
        String binaryString = Long.toBinaryString(res);
        if (binaryString.length() <= 32) {
            return (int) res;

        } else {


            this.excess = binaryToDecimal(
                    binaryString.substring(0, binaryString.length() - 32), (res < 0));

            return binaryToDecimal(binaryString.substring(binaryString.length() - 32), (res < 0));
        }
    }


    private int binaryToDecimal(String binaryString, boolean isSigned) {
        if (!isSigned) {
            return Integer.parseInt(binaryString, 2);

        } else {
            String copyString = '0' + binaryString.substring(1);
            int ans = Integer.parseInt(copyString, 2);


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


    private void scheduleEvent(Instruction inst) {

        switch (inst.getOperationType()) {
            case mul:
            case muli: {


                Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(
                        Clock.getCurrentTime() + Configuration.multiplier_latency, this, this, inst,
                        this.aluResult, this.excess, this.op, this.isBranchTaken, this.branchPC));
                OF_EX_Latch.setEXBusy(true);
                break;
            }
            case div:
            case divi: {


                Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(
                        Clock.getCurrentTime() + Configuration.divider_latency, this, this, inst,
                        this.aluResult, this.excess, this.op, this.isBranchTaken, this.branchPC));
                OF_EX_Latch.setEXBusy(true);
                break;
            }

            case add:
            case sub:
            case and:
            case or:
            case xor:
            case slt:
            case sll:
            case srl:
            case sra:
            case addi:
            case subi:
            case andi:
            case ori:
            case xori:
            case slti:
            case slli:
            case srli:
            case srai:
            case load:
            case store:
            case beq:
            case bne:
            case blt:
            case bgt: {


                Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(
                        Clock.getCurrentTime() + Configuration.ALU_latency, this, this, inst,
                        this.aluResult, this.excess, this.op, this.isBranchTaken, this.branchPC));
                OF_EX_Latch.setEXBusy(true);
                break;
            }

            case jmp:
            case end: {

                containingProcessor.getControlInterlockUnit().validate();

                OF_EX_Latch.setEXBusy(false);
                OF_EX_Latch.setValidInst(false);
                OF_EX_Latch.setEX_enable(false);

                EX_MA_Latch.setValidInst(true);
                EX_MA_Latch.setInstruction(inst);
                EX_MA_Latch.setMA_enable(true);
                break;
            }

            default:
                Misc.printErrorAndExit("Unknown Instruction!!");
        }
    }


    @Override
    public void handleEvent(Event e) {


        if (EX_MA_Latch.isMABusy()) {


            e.setEventTime(Clock.getCurrentTime() + 1);
            Simulator.getEventQueue().addEvent(e);
        } else {
            ExecutionCompleteEvent event = (ExecutionCompleteEvent) e;



            OF_EX_Latch.setEXBusy(false);
            OF_EX_Latch.setValidInst(false);
            OF_EX_Latch.setEX_enable(false);

            EX_IF_Latch.setIsBranchTaken(event.isBranchTaken());
            EX_IF_Latch.setBranchPC(event.getBranchPC());

            EX_MA_Latch.setMA_enable(true);
            EX_MA_Latch.setValidInst(true);
            EX_MA_Latch.setInstruction(event.getInst());
            EX_MA_Latch.setAluResult(event.getAluResult());
            EX_MA_Latch.setExcess(event.getExcess());
            EX_MA_Latch.setOperand(event.getOp());


            containingProcessor.getControlInterlockUnit().validate();
        }
    }

}
