package processor.pipeline.latches;

public class IF_EnableLatchType {
	
	boolean IF_enable;

	public boolean is_IF_busy;
	
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
