package mg.dynaquest.queryexecution.po.streaming.object;

public class DefaultTimeIntervalPredicate implements TimeIntervalPredicate {

	public boolean evaluate(TimeInterval i1, TimeInterval i2) {
		return true;
	}

}
