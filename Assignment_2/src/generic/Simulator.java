package generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import generic.Operand.OperandType;

import javax.swing.*;


public class Simulator {

    static FileInputStream inputcodeStream = null;


    public static void setupSimulation(String assemblyProgramFile) {
        int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
//      The firstCodeAddress tell us about the very first PC value ig
        ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
//        System.out.println(firstCodeAddress);
//        System.out.println(ParsedProgram.getInstructionAt(2));
//        System.out.println(ParsedProgram.getInstructionAt(3));
//        ParsedProgram.printState();
    }

    public static void assemble(String objectProgramFile) {
        HashMap<Instruction.OperationType, Integer> opCodeMap = new HashMap<>();
        HashMap<Instruction.OperationType, Integer> typeMap = new HashMap<>();
        /*
         * Value 0 -> RI Type
         * Value 1 -> R2I Type
         * Value 2 -> R3 Type
         */
//		Arithmetic Inst
        opCodeMap.put(Instruction.OperationType.and, 0);
        typeMap.put(Instruction.OperationType.and, 2);
        opCodeMap.put(Instruction.OperationType.addi, 1);
        typeMap.put(Instruction.OperationType.addi, 1);
        opCodeMap.put(Instruction.OperationType.sub, 2);
        typeMap.put(Instruction.OperationType.sub, 2);
        opCodeMap.put(Instruction.OperationType.subi, 3);
        typeMap.put(Instruction.OperationType.subi, 1);
        opCodeMap.put(Instruction.OperationType.mul, 4);
        typeMap.put(Instruction.OperationType.mul, 2);
        opCodeMap.put(Instruction.OperationType.muli, 5);
        typeMap.put(Instruction.OperationType.muli, 1);
        opCodeMap.put(Instruction.OperationType.div, 6);
        typeMap.put(Instruction.OperationType.div, 2);
        opCodeMap.put(Instruction.OperationType.divi, 7);
        typeMap.put(Instruction.OperationType.divi, 1);
        opCodeMap.put(Instruction.OperationType.and, 8);
        typeMap.put(Instruction.OperationType.and, 2);
        opCodeMap.put(Instruction.OperationType.andi, 9);
        typeMap.put(Instruction.OperationType.andi, 1);
        opCodeMap.put(Instruction.OperationType.or, 10);
        typeMap.put(Instruction.OperationType.or, 2);
        opCodeMap.put(Instruction.OperationType.ori, 11);
        typeMap.put(Instruction.OperationType.ori, 1);
        opCodeMap.put(Instruction.OperationType.xor, 12);
        typeMap.put(Instruction.OperationType.xor, 2);
        opCodeMap.put(Instruction.OperationType.xori, 13);
        typeMap.put(Instruction.OperationType.xori, 1);
        opCodeMap.put(Instruction.OperationType.slt, 14);
        typeMap.put(Instruction.OperationType.slt, 2);
        opCodeMap.put(Instruction.OperationType.slti, 15);
        typeMap.put(Instruction.OperationType.slti, 1);
        opCodeMap.put(Instruction.OperationType.sll, 16);
        typeMap.put(Instruction.OperationType.sll, 2);
        opCodeMap.put(Instruction.OperationType.slli, 17);
        typeMap.put(Instruction.OperationType.slli, 1);
        opCodeMap.put(Instruction.OperationType.srl, 18);
        typeMap.put(Instruction.OperationType.srl, 2);
        opCodeMap.put(Instruction.OperationType.srli, 19);
        typeMap.put(Instruction.OperationType.srli, 1);
        opCodeMap.put(Instruction.OperationType.sra, 20);
        typeMap.put(Instruction.OperationType.sra, 2);
        opCodeMap.put(Instruction.OperationType.srai, 21);
        typeMap.put(Instruction.OperationType.srai, 1);
// 		Memory Inst
        opCodeMap.put(Instruction.OperationType.load, 22);
        typeMap.put(Instruction.OperationType.load, 1);
        opCodeMap.put(Instruction.OperationType.store, 23);
        typeMap.put(Instruction.OperationType.store, 1);
//		Control Flow Inst
        opCodeMap.put(Instruction.OperationType.jmp, 24);
        typeMap.put(Instruction.OperationType.jmp, 0);
        opCodeMap.put(Instruction.OperationType.beq, 25);
        typeMap.put(Instruction.OperationType.beq, 1);
        opCodeMap.put(Instruction.OperationType.bne, 26);
        typeMap.put(Instruction.OperationType.bne, 1);
        opCodeMap.put(Instruction.OperationType.blt, 27);
        typeMap.put(Instruction.OperationType.blt, 1);
        opCodeMap.put(Instruction.OperationType.bgt, 28);
        typeMap.put(Instruction.OperationType.bgt, 1);
//		End Inst
        opCodeMap.put(Instruction.OperationType.end, 29);
        typeMap.put(Instruction.OperationType.end, 0);

        Instruction inst = ParsedProgram.getInstructionAt(2);
//        System.out.println( inst.getOperationType());
//        System.out.println(inst.getSourceOperand1());
//        System.out.println(inst.getDestinationOperand());
//        System.out.println(inst.getSourceOperand2());
        System.out.println(typeMap.get(inst.getOperationType()));
        System.out.println(opCodeMap.get(inst.getOperationType()));

        

        //TODO your assembler code
        //1. open the objectProgramFile in binary mode
        //2. write the firstCodeAddress to the file
        //3. write the data to the file
        //4. assemble one instruction at a time, and write to the file
        //5. close the file
    }

}
