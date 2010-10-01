package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class SelectPO<T> extends AbstractPipe<T, T> {

	private IPredicate<? super T> predicate;
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();
	
	public IPredicate<? super T> getPredicate() {
		return predicate;
	}

	public SelectPO(IPredicate<? super T> predicate){
		this.predicate = predicate.clone();	
	}
	
	public SelectPO(SelectPO<T> po){
		this.predicate = po.predicate.clone();
		this.heartbeatGenerationStrategy = po.heartbeatGenerationStrategy.clone();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	protected synchronized void process_next(T object, int port) {
	//	System.out.println(this+" "+object+" "+port);
		if (predicate.evaluate(object)) {
			transfer(object);
		}else{
			heartbeatGenerationStrategy.generateHeartbeat(object, this);
		}
	}
	
	@Override
	public void process_open() throws OpenFailedException{
		super.process_open();
		this.predicate.init();
	}
	
	@Override
	public SelectPO<T> clone() {
		return new SelectPO<T>(this);
	}

//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((predicate == null) ? 0 : predicate.hashCode());
//		return result;
//	}
//
//	/* (non-Javadoc)
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 */
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		SelectPO<?> other = (SelectPO<?>) obj;
//		if (predicate == null) {
//			if (other.predicate != null)
//				return false;
//		} else if (!predicate.equals(other.predicate))
//			return false;
//		return true;
//	}

	public String toString(){
		return super.toString() + " predicate: " + this.getPredicate().toString(); 
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}

	public IHeartbeatGenerationStrategy<T> getHeartbeatGenerationStrategy() {
		return heartbeatGenerationStrategy;
	}

	public void setHeartbeatGenerationStrategy(
			IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}
}

