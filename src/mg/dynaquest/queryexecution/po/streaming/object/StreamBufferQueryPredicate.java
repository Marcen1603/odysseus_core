package mg.dynaquest.queryexecution.po.streaming.object;

public abstract class StreamBufferQueryPredicate {
	TemporalJoinPredicate tJoinPredicate;
	
	public StreamBufferQueryPredicate(TemporalJoinPredicate tJoinPredicate){
		this.tJoinPredicate = tJoinPredicate;
	}

	public TemporalJoinPredicate getTJoinPredicate() {
		return tJoinPredicate;
	}

	public void setTJoinPredicate(TemporalJoinPredicate joinPredicate) {
		tJoinPredicate = joinPredicate;
	}
	
	public boolean evaluate(StreamExchangeElement e1, StreamExchangeElement e2){
		return TimeInterval.overlaps(e1.getValidity(), e2.getValidity()) &&
			tJoinPredicate.evaluate(e1, e2);
	}
	
}
