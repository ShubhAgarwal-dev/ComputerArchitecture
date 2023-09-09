package generic;

import generic.Instruction.OperationType;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;


public class Simulator {

    static FileInputStream inputcodeStream = null;
    /*
     * Value 0 -> RI Type
     * Value 1 -> R2I Type
     * Value 2 -> R3 Type
     */
    static HashMap<Instruction.OperationType, Integer> typeMap = new HashMap<Instruction.OperationType, Integer>() {{
        put(Instruction.OperationType.add, 2);
        put(Instruction.OperationType.addi, 1);
        put(Instruction.OperationType.sub, 2);
        put(Instruction.OperationType.subi, 1);
        put(Instruction.OperationType.mul, 2);
        put(Instruction.OperationType.muli, 1);
        put(Instruction.OperationType.div, 2);
        put(Instruction.OperationType.divi, 1);
        put(Instruction.OperationType.and, 2);
        put(Instruction.OperationType.andi, 1);
        put(Instruction.OperationType.or, 2);
        put(Instruction.OperationType.ori, 1);
        put(Instruction.OperationType.xor, 2);
        put(Instruction.OperationType.xori, 1);
        put(Instruction.OperationType.slt, 2);
        put(Instruction.OperationType.slti, 1);
        put(Instruction.OperationType.sll, 2);
        put(Instruction.OperationType.slli, 1);
        put(Instruction.OperationType.srl, 2);
        put(Instruction.OperationType.srli, 1);
        put(Instruction.OperationType.sra, 2);
        put(Instruction.OperationType.srai, 1);
        put(Instruction.OperationType.load, 1);
        put(Instruction.OperationType.store, 1);
        put(Instruction.OperationType.jmp, 0);
        put(Instruction.OperationType.beq, 1);
        put(Instruction.OperationType.bne, 1);
        put(Instruction.OperationType.blt, 1);
        put(Instruction.OperationType.bgt, 1);
        put(Instruction.OperationType.end, 0);
    }};

    public static void setupSimulation(String assemblyProgramFile) {
        int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
        ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
    }

    private static String instToMachineCode(Instruction inst) {

        int opCode = inst.getOperationType().ordinal();

        if (typeMap.get(inst.getOperationType()) == 2) {
            int dest_val = inst.getDestinationOperand().getValue();
            int source1_val = inst.getSourceOperand1().getValue();
            int source2_val = inst.getSourceOperand2().getValue();
            opCode = (opCode << 27) | (source1_val << 22) | (source2_val << 17) | (dest_val << 12);
        }

        if (typeMap.get(inst.getOperationType()) == 1) {
            int source_val = inst.getSourceOperand1().getValue();
            if (inst.getSourceOperand2().operandType == Operand.OperandType.Immediate) {
                int dest_val = inst.getDestinationOperand().getValue();
                int imm = inst.getSourceOperand2().getValue();
                opCode = (opCode << 27) | (source_val << 22) | (dest_val << 17) | imm;
            } else {
                if (inst.getSourceOperand2().operandType == Operand.OperandType.Register) {
                    int source2_val = inst.getSourceOperand2().getValue();
                    if (inst.getDestinationOperand().operandType == Operand.OperandType.Label) {
                        int dest_val = ParsedProgram.symtab.get(inst.getDestinationOperand().labelValue) - inst.programCounter;
                        String val = Integer.toBinaryString(dest_val);
                        if (val.length() > 17) {
                            val = val.substring(val.length() - 17);
                        }
                        dest_val = Integer.parseInt(val, 2);
                        opCode = (opCode << 27) | (source_val << 22) | (source2_val << 17) | dest_val;
                    } else {
                        int dest_val = inst.getDestinationOperand().getValue();
                        opCode = (opCode << 27) | (source_val << 22) | (source2_val << 17) | dest_val;
                    }
                } else {
                    int dest_val = inst.getDestinationOperand().getValue();
                    int val = ParsedProgram.symtab.get(inst.getSourceOperand2().labelValue);
                    opCode = (opCode << 27) | (source_val << 22) | (dest_val << 17) | val;
                }
            }
        }

        if (inst.getOperationType() == OperationType.jmp) {
            int val = 0;
            if (inst.getDestinationOperand().operandType == Operand.OperandType.Label) {
                val = ParsedProgram.symtab.get(inst.destinationOperand.labelValue);
                System.out.println(val);
                val -= inst.getProgramCounter();
                System.out.println(val);
            } else {
                val = inst.getDestinationOperand().getValue();
            }
            String val_adj = Integer.toBinaryString(val);
            if (val_adj.length() > 17) {
                val_adj = val_adj.substring(val_adj.length() - 22);
            }
            val = Integer.parseInt(val_adj, 2);
            opCode = (opCode << 27) | val;
        }

        if (inst.getOperationType() == OperationType.end) {
            opCode = opCode << 27;
        }

        return Integer.toBinaryString(opCode);
    }

    public static String adjustMachineCode(String machineCode) {
        int len = machineCode.length();
        return "0".repeat(32 - len) + machineCode;
    }

    public static void assemble(String objectProgramFile) {
        try (FileOutputStream asm = new FileOutputStream(objectProgramFile)) {
            BufferedOutputStream bfile = new BufferedOutputStream(asm);

            byte[] addressCode = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
            bfile.write(addressCode);

            for (Integer value : ParsedProgram.data) {
                byte[] dataValue = ByteBuffer.allocate(4).putInt(value).array();
                bfile.write(dataValue);
            }

            for (Instruction i : ParsedProgram.code) {
                int instInteger = (int) Long.parseLong(adjustMachineCode(instToMachineCode(i)), 2);
                byte[] instBinary = ByteBuffer.allocate(4).putInt(instInteger).array();
                bfile.write(instBinary);
            }
            bfile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
