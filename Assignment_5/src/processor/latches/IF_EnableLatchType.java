package processor.latches;

public class IF_EnableLatchType {
	
	boolean IF_enable;

	boolean IF_Buzy;

	public boolean isIF_Buzy() {
		return IF_Buzy;
	}

	public void setIF_Buzy(boolean IF_Buzy) {
		this.IF_Buzy = IF_Buzy;
	}

	public IF_EnableLatchType()
	{
		IF_enable = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

}
