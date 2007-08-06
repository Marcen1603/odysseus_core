package mg.dynaquest.queryexecution.po.streaming.object;

import java.util.Iterator;

// Analog zur Sweep Area von Pipes

public abstract class DynamicStreamBuffer {

	private StreamBufferQueryPredicate queryPred;
	private StreamBufferRemovePredicate removePred;
	
	public void setQueryPredicate(StreamBufferQueryPredicate queryPred){
		this.queryPred = queryPred;
	}

	public void setRemovePredicate(StreamBufferRemovePredicate removePred){
		this.removePred = removePred;
	}
	
	public void setDefaultRemovePredicate(){
		this.removePred = DefaultRemovePredicate.getInstance();
	}

	public StreamBufferQueryPredicate getQueryPredicate() {
		return queryPred;
	}

	public StreamBufferRemovePredicate getRemovePredicate() {
		return removePred;
	}		
	
	public abstract void insert(StreamExchangeElement e);
	public abstract void remove(StreamExchangeElement e);
	public abstract StreamExchangeElement top();	
	public abstract Iterator<StreamExchangeElement> query(StreamExchangeElement e);
	public abstract void reorganize(StreamExchangeElement e, DynamicStreamBuffer outputBuffer);

	public abstract int size();
	
}
