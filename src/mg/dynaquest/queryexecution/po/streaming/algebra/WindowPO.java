package mg.dynaquest.queryexecution.po.streaming.algebra;

import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.algebra.UnaryAlgebraPO;

public class WindowPO extends UnaryAlgebraPO {

	WindowType windowType = null;
	long windowSize = 1;


	public WindowPO(WindowType windowType, long windowSize) {
		super();
		this.windowType = windowType;
		this.windowSize = windowSize;
	}

	public WindowPO(WindowPO windowPO) {
		super(windowPO);
		this.windowSize = windowPO.windowSize;
	}

	public WindowPO() {
		super();
	}

	@Override
	public SupportsCloneMe cloneMe() {
		return new WindowPO(this);
	}
	
	public long getWindowSize(){
		return windowSize;
	}
	
	public void setWindowSize(long windowSize){
		this.windowSize = windowSize;
	}

	public WindowType getWindowType() {
		return windowType;
	}

	public void setWindowType(WindowType windowType) {
		this.windowType = windowType;
	}

	@Override
	public String getPOName() {
		return WindowPO.class.getSimpleName();
	}
}
