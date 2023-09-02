package generic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.ByteBuffer;


import generic.Instruction.OperationType;


public class Simulator {

    static FileInputStream inputcodeStream = null;
    static HashMap<Instruction.OperationType, Integer> typeMap = new HashMap<>();

    public static void setupSimulation(String assemblyProgramFile) {
        /*
         * Value 0 -> RI Type
         * Value 1 -> R2I Type
         * Value 2 -> R3 Type
         */
//		Arithmetic Inst
        typeMap.put(Instruction.OperationType.add, 2);
        typeMap.put(Instruction.OperationType.addi, 1);
        typeMap.put(Instruction.OperationType.sub, 2);
        typeMap.put(Instruction.OperationType.subi, 1);
        typeMap.put(Instruction.OperationType.mul, 2);
        typeMap.put(Instruction.OperationType.muli, 1);
        typeMap.put(Instruction.OperationType.div, 2);
        typeMap.put(Instruction.OperationType.divi, 1);
        typeMap.put(Instruction.OperationType.and, 2);
        typeMap.put(Instruction.OperationType.andi, 1);
        typeMap.put(Instruction.OperationType.or, 2);
        typeMap.put(Instruction.OperationType.ori, 1);
        typeMap.put(Instruction.OperationType.xor, 2);
        typeMap.put(Instruction.OperationType.xori, 1);
        typeMap.put(Instruction.OperationType.slt, 2);
        typeMap.put(Instruction.OperationType.slti, 1);
        typeMap.put(Instruction.OperationType.sll, 2);
        typeMap.put(Instruction.OperationType.slli, 1);
        typeMap.put(Instruction.OperationType.srl, 2);
        typeMap.put(Instruction.OperationType.srli, 1);
        typeMap.put(Instruction.OperationType.sra, 2);
        typeMap.put(Instruction.OperationType.srai, 1);
// 		Memory Inst
        typeMap.put(Instruction.OperationType.load, 1);
        typeMap.put(Instruction.OperationType.store, 1);
//		Control Flow Inst
        typeMap.put(Instruction.OperationType.jmp, 0);
        typeMap.put(Instruction.OperationType.beq, 1);
        typeMap.put(Instruction.OperationType.bne, 1);
        typeMap.put(Instruction.OperationType.blt, 1);
        typeMap.put(Instruction.OperationType.bgt, 1);
//		End Inst
        typeMap.put(Instruction.OperationType.end, 0);

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
            int dest_val = inst.getDestinationOperand().getValue();
            int source_val = inst.getSourceOperand1().getValue();

            if (inst.getSourceOperand2().operandType == Operand.OperandType.Immediate) {
                int imm = inst.getSourceOperand2().getValue();
                opCode = (opCode << 27) | (source_val << 22) | (dest_val << 17) | imm;
            } else {
                int val = ParsedProgram.symtab.get(inst.getSourceOperand2().labelValue);
                opCode = (opCode << 27) | (source_val << 22) | (dest_val << 17) | val;
            }
        }

        if (inst.getOperationType() == OperationType.jmp) {
            int val = ParsedProgram.symtab.get(inst.destinationOperand.labelValue);
            opCode = (opCode << 27) | (val << 22);
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

        try(FileOutputStream asm = new FileOutputStream(objectProgramFile)) {
            BufferedOutputStream bfile = new BufferedOutputStream(asm);

            byte[] addressCode = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
            bfile.write(addressCode);

            for (var value: ParsedProgram.data) {
                byte[] dataValue = ByteBuffer.allocate(4).putInt(value).array();
                bfile.write(dataValue);
            }

//            StringBuilder opString = new StringBuilder();
            for (Instruction i : ParsedProgram.code) {
//                opString.append(adjustMachineCode(instToMachineCode(i)));
                int instInteger = (int) Long.parseLong(adjustMachineCode(instToMachineCode(i)), 2);
                byte[] instBinary = ByteBuffer.allocate(4).putInt(instInteger).array();
                bfile.write(instBinary);
            }
//            int len = opString.length();
//            System.out.println(opString.length());
//            ArrayList<Byte> arrayList = new ArrayList<>(len);
//            for (int i = 0; i < opString.length(); i++) {
//                arrayList.add(i, (opString.charAt(i) == '0') ? Byte.valueOf((byte) 0) : Byte.valueOf((byte) 1));
//            }
////            System.out.println(arrayList.size());
//            for (int i = 0; i < arrayList.size(); i++) {
//                asm.write(arrayList.get(i));
//            }
            bfile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //TODO your assembler code
        //1. open the objectProgramFile in binary mode
        //2. write the firstCodeAddress to the file
        //3. write the data to the file
        //4. assemble one instruction at a time, and write to the file
        //5. close the file
    }

}
