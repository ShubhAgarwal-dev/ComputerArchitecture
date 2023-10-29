package processor.interlocks;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Misc;
import generic.Operand;
import generic.Operand.OperandType;
import generic.Simulator;
import processor.Processor;
import processor.pipeline.latch.EX_MA_LatchType;
import processor.pipeline.latch.IF_EnableLatchType;
import processor.pipeline.latch.IF_OF_LatchType;
import processor.pipeline.latch.MA_RW_LatchType;


public class DataInterlock {


    Processor containingProcessor;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;


    public DataInterlock(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
                         IF_OF_LatchType iF_OF_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
        this.containingProcessor = containingProcessor;
        this.IF_EnableLatch = iF_EnableLatch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
    }




    public void checkConflict() {

        Instruction currInst = getInstruction();
        Instruction eXInst = EX_MA_Latch.getInstruction();
        Instruction mAInst = MA_RW_Latch.getInstruction();


        if ((EX_MA_Latch.isValidInst() && eXInst != null && hasConflict(currInst, eXInst))
                || (MA_RW_Latch.isValidInst() && mAInst != null && hasConflict(currInst, mAInst))) {








            IF_EnableLatch.setStall(true);
            IF_OF_Latch.setStall(true);

            Simulator.incNumDataHazards();

        } else {




            IF_EnableLatch.setStall(false);
            IF_OF_Latch.setStall(false);
        }
    }




    private boolean hasConflict(Instruction A, Instruction B) {

        if (A == null || B == null) {
            return false;
        }


        switch (A.getOperationType()) {
            case jmp:
            case end:
                return false;
            default:
        }

        int rs1A = A.getSourceOperand1().getValue();
        int rs2A = A.getSourceOperand2().getValue();
        int rdA = A.getDestinationOperand().getValue();
        boolean isSecondImm =
                (A.getSourceOperand2().getOperandType() == OperandType.valueOf("Immediate"));
        int second = rs2A;

        if (A.getOperationType() == OperationType.valueOf("store")) {
            second = rdA;
            isSecondImm = false;
        }




        if (rs1A == 31 || second == 31) {
            return true;
        }


        switch (B.getOperationType()) {
            case jmp:
            case end:
                return false;
            default:
        }

        int rs2B = B.getSourceOperand2().getValue();
        int rdB = B.getDestinationOperand().getValue();


        switch (B.getOperationType()) {
            case beq:
            case bne:
            case blt:
            case bgt:
                return false;
            default:
        }



        if (A.getOperationType() == OperationType.valueOf("load")
                && B.getOperationType() == OperationType.valueOf("store")) {
            int addr1 = containingProcessor.getRegisterFile().getValue(rs1A) + rs2A;
            int addr2 = containingProcessor.getRegisterFile().getValue(rdB) + rs2B;
            if (addr1 == addr2) {
                return true;
            }
        }


        if (B.getOperationType() == OperationType.valueOf("store")) {
            return false;
        }


        if (rs1A == rdB || (!isSecondImm && second == rdB)) {
            return true;
        }

        return false;
    }





    private Instruction getInstruction() {

        String inst = padStart(Integer.toBinaryString(IF_OF_Latch.getInstruction()), 32);

        Instruction newIns = new Instruction();


        newIns.setOperationType(
                OperationType.values()[binaryToDecimal(inst.substring(0, 5), false)]);


        switch (newIns.getOperationType()) {

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
            case sra: {

                newIns.setSourceOperand1(getRegisterOperand(inst.substring(5, 10)));

                newIns.setSourceOperand2(getRegisterOperand(inst.substring(10, 15)));

                newIns.setDestinationOperand(getRegisterOperand(inst.substring(15, 20)));
                break;
            }


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
            case load:
            case store: {

                newIns.setSourceOperand1(getRegisterOperand(inst.substring(5, 10)));

                newIns.setDestinationOperand(getRegisterOperand(inst.substring(10, 15)));

                newIns.setSourceOperand2(getImmediateOperand(inst.substring(15, 32)));
                break;
            }

            case beq:
            case bne:
            case blt:
            case bgt: {

                newIns.setSourceOperand1(getRegisterOperand(inst.substring(5, 10)));

                newIns.setSourceOperand2(getRegisterOperand(inst.substring(10, 15)));

                newIns.setDestinationOperand(getImmediateOperand(inst.substring(15, 32)));
                break;
            }


            case jmp: {
                if (binaryToDecimal(inst.substring(5, 10), false) != 0) {

                    newIns.setDestinationOperand(getRegisterOperand(inst.substring(5, 10)));
                } else {

                    newIns.setDestinationOperand(getImmediateOperand(inst.substring(10, 32)));
                }
                break;
            }

            case end:
                break;

            default:
                Misc.printErrorAndExit("Unknown Instruction!!");
        }

        return newIns;
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
