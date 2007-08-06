package mg.dynaquest.queryexecution.po.streaming.object;

public interface TimeIntervalPredicate {
	public boolean evaluate(TimeInterval i1, TimeInterval i2);
}
