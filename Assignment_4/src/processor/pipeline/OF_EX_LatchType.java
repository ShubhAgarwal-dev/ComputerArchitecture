package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;

	private int opCode, rd, op1, op2, immediate, r31, rs1, rs2;

	public int getRs1() {
		return rs1;
	}

	public void setRs1(int rs1) {
		this.rs1 = rs1;
	}

	public int getRs2() {
		return rs2;
	}

	public void setRs2(int rs2) {
		this.rs2 = rs2;
	}

	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public int getOpCode() {
		return opCode;
	}

	public void setOpCode(int opCode) {
		this.opCode = opCode;
	}

	public int getRd() {
		return rd;
	}

	public void setRd(int rd) {
		this.rd = rd;
	}

	public int getOp1() {
		return op1;
	}

	public void setOp1(int op1) {
		this.op1 = op1;
	}

	public int getOp2() {
		return op2;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public int getImmediate() {
		return immediate;
	}

	public void setImmediate(int immediate) {
		this.immediate = immediate;
	}

	public int getR31() {
		return r31;
	}

	public void setR31(int r31) {
		this.r31 = r31;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}


}
