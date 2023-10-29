package processor.interlocks;

import generic.*;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import processor.Processor;
import processor.pipeline.latch.EX_MA_LatchType;
import processor.pipeline.latch.IF_EnableLatchType;
import processor.pipeline.latch.IF_OF_LatchType;
import processor.pipeline.latch.MA_RW_LatchType;


public class DataLock {


    Processor containingProcessor;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;


    public DataLock(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
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


        if ((EX_MA_Latch.isValidInst() && eXInst != null && hasConflict(currInst, eXInst)) || (MA_RW_Latch.isValidInst() && mAInst != null && hasConflict(currInst, mAInst))) {

            IF_EnableLatch.setStall(true);
            IF_OF_Latch.setStall(true);

            Simulator.updateNumberOfDataHazard();
            Statistics.setDynamicInstCount(Statistics.getDynamicInstCount() - 1);
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
        boolean isSecondImm = (A.getSourceOperand2().getOperandType() == OperandType.valueOf("Immediate"));
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

        if (A.getOperationType() == OperationType.valueOf("load") && B.getOperationType() == OperationType.valueOf("store")) {
            int addr1 = containingProcessor.getRegisterFile().getValue(rs1A) + rs2A;
            int addr2 = containingProcessor.getRegisterFile().getValue(rdB) + rs2B;
            if (addr1 == addr2) {
                return true;
            }
        }

        if (B.getOperationType() == OperationType.valueOf("store")) {
            return false;
        }

        return rs1A == rdB || (!isSecondImm && second == rdB);
    }

    private Instruction getInstruction() {
        String inst = padStart(Integer.toBinaryString(IF_OF_Latch.getInstruction()), 32);
        Instruction newIns = new Instruction();

        newIns.setOperationType(OperationType.values()[binaryToDecimal(inst.substring(0, 5), false)]);

        if (newIns.getOperationType() == OperationType.add || newIns.getOperationType() == OperationType.sub || newIns.getOperationType() == OperationType.mul || newIns.getOperationType() == OperationType.div || newIns.getOperationType() == OperationType.and || newIns.getOperationType() == OperationType.or || newIns.getOperationType() == OperationType.xor || newIns.getOperationType() == OperationType.slt || newIns.getOperationType() == OperationType.sll || newIns.getOperationType() == OperationType.srl || newIns.getOperationType() == OperationType.sra) {

            newIns.setSourceOperand1(getRegisterOperand(inst.substring(5, 10)));
            newIns.setSourceOperand2(getRegisterOperand(inst.substring(10, 15)));
            newIns.setDestinationOperand(getRegisterOperand(inst.substring(15, 20)));

        } else if (newIns.getOperationType() == OperationType.addi || newIns.getOperationType() == OperationType.subi || newIns.getOperationType() == OperationType.muli || newIns.getOperationType() == OperationType.divi || newIns.getOperationType() == OperationType.andi || newIns.getOperationType() == OperationType.ori || newIns.getOperationType() == OperationType.xori || newIns.getOperationType() == OperationType.slti || newIns.getOperationType() == OperationType.slli || newIns.getOperationType() == OperationType.srli || newIns.getOperationType() == OperationType.srai || newIns.getOperationType() == OperationType.load || newIns.getOperationType() == OperationType.store) {

            newIns.setSourceOperand1(getRegisterOperand(inst.substring(5, 10)));
            newIns.setDestinationOperand(getRegisterOperand(inst.substring(10, 15)));
            newIns.setSourceOperand2(getImmediateOperand(inst.substring(15, 32)));

        } else if (newIns.getOperationType() == OperationType.beq || newIns.getOperationType() == OperationType.bne || newIns.getOperationType() == OperationType.blt || newIns.getOperationType() == OperationType.bgt) {

            newIns.setSourceOperand1(getRegisterOperand(inst.substring(5, 10)));
            newIns.setSourceOperand2(getRegisterOperand(inst.substring(10, 15)));
            newIns.setDestinationOperand(getImmediateOperand(inst.substring(15, 32)));

        } else if (newIns.getOperationType() == OperationType.jmp) {
            if (binaryToDecimal(inst.substring(5, 10), false) != 0) {
                newIns.setDestinationOperand(getRegisterOperand(inst.substring(5, 10)));
            } else {
                newIns.setDestinationOperand(getImmediateOperand(inst.substring(10, 32)));
            }
        } else if (newIns.getOperationType() == OperationType.end) {
            // Do nothing for end operation
        } else {
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
