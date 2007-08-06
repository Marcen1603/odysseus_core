package mg.dynaquest.queryexecution.po.streaming.base;

import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.streaming.algebra.WindowPO;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.queryexecution.po.streaming.object.TimeInterval;

public class TumblingWindowPO extends AbstractWindowPO {

	public TumblingWindowPO(TumblingWindowPO operator) {
		super(operator);
	}

	public TumblingWindowPO() {
	}

	public TumblingWindowPO(WindowPO algebraPO) {
		super(algebraPO);
	}

	@Override
	protected void setWindow(StreamExchangeElement<?> next) {
		TimeInterval interval = next.getValidity();
		
		interval.getEnd().setPoint(
				//integer division is used, to determine the closest point
				//in time x*windowSize with start < end
				(interval.getStart().getMainPoint() / getWindowSize() + 1)
						* getWindowSize(), 0);
	}

	@Override
	public String getInternalPOName() {
		return "TumblingWindowPO";
	}

	public SupportsCloneMe cloneMe() {
		return new TumblingWindowPO(this);
	}

}
