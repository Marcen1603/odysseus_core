package mg.dynaquest.queryexecution.po.streaming.object;

public interface StreamBufferRemovePredicate {
	boolean evaluate(StreamExchangeElement s1, StreamExchangeElement s2);
}
