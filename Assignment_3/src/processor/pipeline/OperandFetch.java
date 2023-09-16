package processor.pipeline;

import generic.Misc;
import generic.Simulator;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			String binaryInstruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			while(binaryInstruction.length() < 32)
			{
				binaryInstruction = "0" + binaryInstruction;
			}

			// OF stage
			// opcode
			int opcode = Integer.parseInt(binaryInstruction.substring(0, 5), 2);

			// imm
			int imm = Misc.getIntFromBinaryString(binaryInstruction.substring(15, 32));

			// branchPC
			int branchPC;
			if(opcode != 24)
			{
				// R2I
				int Imm = Misc.getIntFromBinaryString(binaryInstruction.substring(15, 32));
				branchPC = IF_OF_Latch.getPc()+Imm;
			}
			else
			{
				// RI
				Integer rd = Integer.parseInt(binaryInstruction.substring(5, 10), 2);
				Integer Imm = Misc.getIntFromBinaryString(binaryInstruction.substring(10, 32));
				branchPC = IF_OF_Latch.getPc()+rd+Imm;
			}

			// op1
			int rs1 = Integer.parseInt(binaryInstruction.substring(5, 10), 2);
			int op1 = containingProcessor.getRegisterFile().getValue(rs1);

			// op2
			int rs2 = Integer.parseInt(binaryInstruction.substring(10, 15), 2);
			int op2 = containingProcessor.getRegisterFile().getValue(rs2);

			// rd
			int rd;
			if(opcode <= 21 && opcode%2 == 0)
			{
				// R3 Type
				rd = Integer.parseInt(binaryInstruction.substring(15, 20), 2);
			}
			else if(opcode <= 21 && opcode%2 == 1)
			{
				// R2I Type
				rd = Integer.parseInt(binaryInstruction.substring(10, 15), 2);
			}
			else if(opcode >= 22 && opcode <= 23)
			{
				// R2I Type
				rd = Integer.parseInt(binaryInstruction.substring(10, 15), 2);
			}
			else if(opcode == 24)
			{
				// RI
				rd = Integer.parseInt(binaryInstruction.substring(5, 10), 2);
			}
			else
			{
				// R2I
				rd = Integer.parseInt(binaryInstruction.substring(10, 15), 2);
			}

			// r31
			int r31 = containingProcessor.getRegisterFile().getValue(31);
			
			// Set in latch
			OF_EX_Latch.setOpcode(opcode);
			OF_EX_Latch.setImm(imm);
			OF_EX_Latch.setOp1(op1);
			OF_EX_Latch.setOp2(op2);
			OF_EX_Latch.setPc(IF_OF_Latch.getPc());
			OF_EX_Latch.setRd(rd);

			// Set branchPC
			containingProcessor.setBranchPC(branchPC);

			// Set EX_enable
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);

			if(Simulator.isDebugMode())
			{	
				System.out.println("[Debug] (OF) PC: " + IF_OF_Latch.getPc());
				System.out.println("[Debug] (OF) Opcode: " + opcode);
				System.out.println("[Debug] (OF) Rs1: " + rs1);
				System.out.println("[Debug] (OF) Rs2: " + rs2);
				System.out.println("[Debug] (OF) Rd: " + rd);
				System.out.println("[Debug] (OF) Op1: " + op1);
				System.out.println("[Debug] (OF) Op2: " + op2);
				System.out.println("[Debug] (OF) Imm: " + imm);
				System.out.println("[Debug] (OF) BranchPC: " + branchPC);
			}
		}
	}

}