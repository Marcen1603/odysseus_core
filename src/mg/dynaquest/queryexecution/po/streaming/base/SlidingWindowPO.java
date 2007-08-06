package mg.dynaquest.queryexecution.po.streaming.base;

import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.streaming.algebra.WindowPO;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.queryexecution.po.streaming.object.TimeInterval;

public class SlidingWindowPO extends AbstractWindowPO {

	public SlidingWindowPO(AbstractWindowPO operator) {
		super(operator);
	}
	
	public SlidingWindowPO(WindowPO algebraPO){
		super(algebraPO);
	}

	public SlidingWindowPO() {
		super();
	}

	@Override
	protected void setWindow(StreamExchangeElement<?> next) {
		TimeInterval interval = next.getValidity();
		interval.getEnd().setPoint(
				interval.getStart().getMainPoint() + getWindowSize(),
				interval.getStart().getSubpoint());
	}

	@Override
	public String getInternalPOName() {
		return "SlidingWindowPO";
	}

	public SupportsCloneMe cloneMe() {
		return new SlidingWindowPO(this);
	}

}
