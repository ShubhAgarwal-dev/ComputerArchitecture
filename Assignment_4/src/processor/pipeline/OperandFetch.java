package processor.pipeline;

import generic.Misc;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;

	private int getOpCodeFromBinaryInstruction(String binaryCodeForInstruction) {
		return Integer.parseInt(binaryCodeForInstruction.substring(0, 5), 2);
	}
	private int getImmediateFromBinaryInstruction(String binaryCodeForInstruction){
		String immediateString = binaryCodeForInstruction.substring(15,32);
		boolean isNegative = immediateString.charAt(0)=='1';
		if(isNegative){
			StringBuilder complement = new StringBuilder();
			for(int i=0;i<immediateString.length();i++){
				if(immediateString.charAt(i)=='0'){
					complement.append('1');
				}else{
					complement.append('0');
				}
			}
			immediateString = complement.toString();
		}
		if(isNegative){
			return Integer.parseInt(immediateString,2)*-1 -1;
		}
		return  Integer.parseInt(immediateString,2);
	}

	/**
	 *
	 * @param binaryCodeForInstruction String
	 * @return substring(5,10)
	 */
	private int getOp1FromBinaryInstruction(String binaryCodeForInstruction){
		return Integer.parseInt(binaryCodeForInstruction.substring(5,10),2);
	}

	/**
	 *
	 * @param binaryCodeForInstruction String
	 * @return substring(10,15)
	 */
	private int getOp2FromBinaryInstruction(String binaryCodeForInstruction){
		return Integer.parseInt(binaryCodeForInstruction.substring(10,15),2);
	}

	/**
	 *
	 * @param binaryCodeForInstruction String
	 * @return substring(15,20)
	 */
	private int getOp3FromBinaryInstruction(String binaryCodeForInstruction){
		return Integer.parseInt(binaryCodeForInstruction.substring(15,20),2);
	}
	private int getBranchPC(String binaryCodeForInstruction,int opCode){
		int value;
		if(opCode!=24){
			// the format is R2I
			int immediate = getImmediateFromBinaryInstruction(binaryCodeForInstruction);
			value= Misc.getPC(containingProcessor)+immediate;
		}else{
			// the format is RI
			int rd = getOp1FromBinaryInstruction(binaryCodeForInstruction);
			int immediate = getImmediateFromBinaryInstruction(binaryCodeForInstruction);
			value = Misc.getPC(containingProcessor)+rd+immediate;
		}
		return  value;
	}

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

			// Getting the instruction and adjusting its size
			String binaryCodeForInstruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			binaryCodeForInstruction = "0".repeat(32-binaryCodeForInstruction.length())+binaryCodeForInstruction;
			// Getting the OP Code
			int opCode = getOpCodeFromBinaryInstruction(binaryCodeForInstruction);

			// Getting the Immediate
			int immediate = getImmediateFromBinaryInstruction(binaryCodeForInstruction);

			// branchPC
			int branchPC = getBranchPC(binaryCodeForInstruction,opCode);

			// op1
			int op1 = containingProcessor.getRegisterFile().getValue(getOp1FromBinaryInstruction(binaryCodeForInstruction));
			int rs1 = getOp1FromBinaryInstruction(binaryCodeForInstruction);
			// op2
			int op2 = containingProcessor.getRegisterFile().getValue(getOp2FromBinaryInstruction(binaryCodeForInstruction));
			int rs2 = getOp2FromBinaryInstruction(binaryCodeForInstruction);
			// rd
			int rd;
			if(opCode<=21 && opCode%2!=0){
				// R3 type
				rd = getOp2FromBinaryInstruction(binaryCodeForInstruction);
			}else if(opCode<=21){
				// R2I type
				rd = getOp3FromBinaryInstruction(binaryCodeForInstruction);
			}else if(opCode==22 || opCode==23){
				// R2I
				rd = getOp2FromBinaryInstruction(binaryCodeForInstruction);
			}else if(opCode==24){
				// RI
				rd = getOp1FromBinaryInstruction(binaryCodeForInstruction);
			}else{
				// R2I
				rd = getOp2FromBinaryInstruction(binaryCodeForInstruction);
			}

			// r31
			int r31 = containingProcessor.getRegisterFile().getValue(31);


			// set branch pc
			containingProcessor.setBranchPC(branchPC);

			// passing necessary values to the next latch
			OF_EX_Latch.setEX_enable(true);
			OF_EX_Latch.setOpCode(opCode);
			OF_EX_Latch.setOp1(op1);
			OF_EX_Latch.setOp2(op2);
			OF_EX_Latch.setRd(rd);
			OF_EX_Latch.setImmediate(immediate);
			OF_EX_Latch.setR31(r31);
			OF_EX_Latch.setRs1(rs1);
			OF_EX_Latch.setRs2(rs2);


			// disabling and enabling latches
//			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);

//			this.containingProcessor.DataLockUnit().DLU();

			System.out.println("[Debug] (OF) binCode: " + binaryCodeForInstruction);
			System.out.println("[Debug] (OF) PC: " + Misc.getPC(containingProcessor));
			System.out.println("[Debug] (OF) Opcode: " + opCode);
			System.out.println("[Debug] (OF) Rd: " + rd);
			System.out.println("[Debug] (OF) Op1: " + op1);
			System.out.println("[Debug] (OF) Op2: " + op2);
			System.out.println("[Debug] (OF) Imm: " + immediate);
			System.out.println("[Debug] (OF) BranchPC: " + branchPC);

		}
	}

}
