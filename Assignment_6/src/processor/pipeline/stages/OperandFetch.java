package processor.pipeline.stages;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Misc;
import generic.Operand;
import generic.Operand.OperandType;
import processor.Processor;
import processor.pipeline.latch.IF_OF_LatchType;
import processor.pipeline.latch.OF_EX_LatchType;

public class OperandFetch {
    Processor containingProcessor;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;

    public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch) {
        this.containingProcessor = containingProcessor;
        this.IF_OF_Latch = iF_OF_Latch;
        this.OF_EX_Latch = oF_EX_Latch;
    }

    public void performOF() {


        if (IF_OF_Latch.isOF_enable()) {
            if (!OF_EX_Latch.isEXBusy() && !OF_EX_Latch.isEXMABusy()) {
                IF_OF_Latch.setOFBusy(false);
                if (IF_OF_Latch.getNop()) {
                    OF_EX_Latch.setInstruction(null);

                    OF_EX_Latch.setValidInst(true);
                    IF_OF_Latch.setValidInst(false);
                } else {
                    if (IF_OF_Latch.isValidInst()) {
                        containingProcessor.getDataInterlockUnit().checkConflict();
                        if (IF_OF_Latch.getStall()) {
                            OF_EX_Latch.setInstruction(null);
                            OF_EX_Latch.setValidInst(true);
                        } else {
                            getCode();
                            OF_EX_Latch.setValidInst(true);
                            IF_OF_Latch.setValidInst(false);
                        }
                    } else {

                    }
                }
            } else {


                IF_OF_Latch.setOFBusy(true);
            }

            IF_OF_Latch.setOF_enable(false);
            OF_EX_Latch.setEX_enable(true);
        } else {

        }
    }


    private void getCode() {
        String inst = padStart(Integer.toBinaryString(IF_OF_Latch.getInstruction()), 32);
        Instruction newIns = new Instruction();
        newIns.setProgramCounter(IF_OF_Latch.getCurrentPC());
        newIns.setOperationType(OperationType.values()[binaryToDecimal(inst.substring(0, 5), false)]);

        if (newIns.getOperationType() == OperationType.add || newIns.getOperationType() == OperationType.sub ||
                newIns.getOperationType() == OperationType.mul || newIns.getOperationType() == OperationType.div ||
                newIns.getOperationType() == OperationType.and || newIns.getOperationType() == OperationType.or ||
                newIns.getOperationType() == OperationType.xor || newIns.getOperationType() == OperationType.slt ||
                newIns.getOperationType() == OperationType.sll || newIns.getOperationType() == OperationType.srl ||
                newIns.getOperationType() == OperationType.sra) {

            newIns.setSourceOperand1(getRegisterOperand(inst.substring(5, 10)));
            newIns.setSourceOperand2(getRegisterOperand(inst.substring(10, 15)));
            newIns.setDestinationOperand(getRegisterOperand(inst.substring(15, 20)));

            OF_EX_Latch.setOperand1(containingProcessor.getRegisterFile().getValue(newIns.getSourceOperand1().getValue()));
            OF_EX_Latch.setOperand2(containingProcessor.getRegisterFile().getValue(newIns.getSourceOperand2().getValue()));
            OF_EX_Latch.setIsImmediate(false);

        } else if (newIns.getOperationType() == OperationType.addi || newIns.getOperationType() == OperationType.subi ||
                newIns.getOperationType() == OperationType.muli || newIns.getOperationType() == OperationType.divi ||
                newIns.getOperationType() == OperationType.andi || newIns.getOperationType() == OperationType.ori ||
                newIns.getOperationType() == OperationType.xori || newIns.getOperationType() == OperationType.slti ||
                newIns.getOperationType() == OperationType.slli || newIns.getOperationType() == OperationType.srai ||
                newIns.getOperationType() == OperationType.load || newIns.getOperationType() == OperationType.store) {

            newIns.setSourceOperand1(getRegisterOperand(inst.substring(5, 10)));
            newIns.setDestinationOperand(getRegisterOperand(inst.substring(10, 15)));
            newIns.setSourceOperand2(getImmediateOperand(inst.substring(15, 32)));

            OF_EX_Latch.setOperand1(containingProcessor.getRegisterFile().getValue(newIns.getSourceOperand1().getValue()));
            OF_EX_Latch.setOperand2(containingProcessor.getRegisterFile().getValue(newIns.getDestinationOperand().getValue()));
            OF_EX_Latch.setImmediate(newIns.getSourceOperand2().getValue());
            OF_EX_Latch.setIsImmediate(true);

        } else if (newIns.getOperationType() == OperationType.beq || newIns.getOperationType() == OperationType.bne ||
                newIns.getOperationType() == OperationType.blt || newIns.getOperationType() == OperationType.bgt) {

            newIns.setSourceOperand1(getRegisterOperand(inst.substring(5, 10)));
            newIns.setSourceOperand2(getRegisterOperand(inst.substring(10, 15)));
            newIns.setDestinationOperand(getImmediateOperand(inst.substring(15, 32)));

            OF_EX_Latch.setOperand1(containingProcessor.getRegisterFile().getValue(newIns.getSourceOperand1().getValue()));
            OF_EX_Latch.setOperand2(containingProcessor.getRegisterFile().getValue(newIns.getSourceOperand2().getValue()));
            OF_EX_Latch.setBranchTarget(IF_OF_Latch.getCurrentPC() + newIns.getDestinationOperand().getValue());

        } else if (newIns.getOperationType() == OperationType.jmp) {
            if (binaryToDecimal(inst.substring(5, 10), false) != 0) {
                newIns.setDestinationOperand(getRegisterOperand(inst.substring(5, 10)));
            } else {
                newIns.setDestinationOperand(getImmediateOperand(inst.substring(10, 32)));
            }

            OF_EX_Latch.setBranchTarget(IF_OF_Latch.getCurrentPC() + newIns.getDestinationOperand().getValue());

        } else if (newIns.getOperationType() == OperationType.end) {
            // Do nothing

        } else {
            Misc.printErrorAndExit("Unknown Instruction!!");
        }

        OF_EX_Latch.setInstruction(newIns);
    }


    private String padStart(String str, int totalLength) {
        if (str.length() >= totalLength) {
            return str;
        }
        int count = 0;
        String ans = "";
        while (count < totalLength - str.length()) {
            ans += '0';
            ++count;
        }
        ans += str;
        return ans;
    }


    private int binaryToDecimal(String binaryString, boolean isSigned) {
        if (!isSigned) {
            return Integer.parseInt(binaryString, 2);
        } else {
            String copyString = '0' + binaryString.substring(1);
            int ans = Integer.parseInt(copyString, 2);
            if (binaryString.length() == 32) {
                if (binaryString.charAt(0) == '1') {
                    int power = (1 << 30);
                    ans -= power;
                    ans -= power;
                }
            } else {
                if (binaryString.charAt(0) == '1') {
                    int power = (1 << (binaryString.length() - 1));
                    ans -= power;
                }
            }
            return ans;
        }
    }


    private Operand getRegisterOperand(String val) {
        Operand operand = new Operand();
        operand.setOperandType(OperandType.Register);
        operand.setValue(binaryToDecimal(val, false));
        return operand;
    }


    private Operand getImmediateOperand(String val) {
        Operand operand = new Operand();
        operand.setOperandType(OperandType.Immediate);
        operand.setValue(binaryToDecimal(val, true));
        return operand;
    }
}
