/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class SelectPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements IHasPredicate{

	private IPredicate<? super T> predicate;
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();
	
	@Override
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
	protected void process_next(T object, int port) {
		if (predicate.evaluate(object)) {
			transfer(object);
		}else{
			// Send filtered data to output port 1
			transfer(object,1);
			heartbeatGenerationStrategy.generateHeartbeat(object, this);
		}
	}
	
	@Override
	public void process_open() throws OpenFailedException{
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

	@Override
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
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof SelectPO<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		SelectPO<T> spo = (SelectPO<T>) ipo;
		// Different sources
		if(!this.hasSameSources(spo)) return false;
		// Predicates match
		if(this.predicate.equals(spo.getPredicate())
				|| (this.predicate.isContainedIn(spo.getPredicate()) && spo.getPredicate().isContainedIn(this.predicate))) {
			return true;
		}

		return false;
	}
	
	@Override
	@SuppressWarnings({"rawtypes"})
	public boolean isContainedIn(IPipe<T,T> ip) {
		if(!(ip instanceof SelectPO) || !this.hasSameSources(ip)) {
			return false;
		}
		// Sonderfall, dass das Prädikat des anderen SelectPOs ein OrPredicate ist und das Prädikat von diesem SelectPO nicht.
		if((ComplexPredicateHelper.isOrPredicate(((SelectPO)ip).getPredicate()) && !ComplexPredicateHelper.isOrPredicate(this.predicate))) {
			return ComplexPredicateHelper.contains(((SelectPO)ip).getPredicate(), this.predicate);
		}
		if(this.predicate.isContainedIn(((SelectPO<T>)ip).predicate)) {
			return true;
		}
		return false;
	}
}

