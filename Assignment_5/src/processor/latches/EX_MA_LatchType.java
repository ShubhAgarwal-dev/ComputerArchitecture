package processor.latches;

public class EX_MA_LatchType {
	
	boolean MA_enable;

	private int  opRes, op1, op2, opCode, rd, r31;

	boolean MA_Buzy;

	public boolean isMA_Buzy() {
		return MA_Buzy;
	}

	public void setMA_Buzy(boolean MA_Buzy) {
		this.MA_Buzy = MA_Buzy;
	}


	public int getOpRes() {
		return opRes;
	}

	public void setOpRes(int opRes) {
		this.opRes = opRes;
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

	public int getR31() {
		return r31;
	}

	public void setR31(int r31) {
		this.r31 = r31;
	}

	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

}
