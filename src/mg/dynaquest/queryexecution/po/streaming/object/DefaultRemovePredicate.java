package mg.dynaquest.queryexecution.po.streaming.object;

public class DefaultRemovePredicate implements StreamBufferRemovePredicate {

	static private DefaultRemovePredicate instance = new DefaultRemovePredicate();
	
	private DefaultRemovePredicate(){}
	
	public static DefaultRemovePredicate getInstance(){
		return instance;
	}
	
	public boolean evaluate(StreamExchangeElement s1, StreamExchangeElement s2) {
		return s1.getValidity().getStart().afterOrEquals(s2.getValidity().getEnd());
	}

}
