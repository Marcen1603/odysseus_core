/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

@SuppressWarnings("unchecked")
public class ObjectTrackingSelectPO<T extends IMetaAttributeContainer<M>, M extends IPredictionFunctionKey & IApplicationTime> extends AbstractPipe<T, T> {

	private Map<IPredicate<? super T>, IRangePredicate> rangePredicates;

	private long duration;
	private long counter;
	private long transfered;

	public ObjectTrackingSelectPO(Map<IPredicate<? super T>, IRangePredicate> rangePredicates){
		this.rangePredicates = rangePredicates;
	}
	
	public ObjectTrackingSelectPO(ObjectTrackingSelectPO<T, M> original){
		this.rangePredicates = new HashMap<IPredicate<? super T>, IRangePredicate>();
		for(Entry<IPredicate<? super T>, IRangePredicate> entry: original.rangePredicates.entrySet()){
			this.rangePredicates.put(entry.getKey(), entry.getValue());
		}
	}
	

	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		// first get the range predicate
		this.counter++;
		long start = System.nanoTime();
		IRangePredicate rangePredicate = this.rangePredicates.get(object.getMetadata().getPredictionFunctionKey());
		
		List<ITimeInterval> appIntervals = rangePredicate.evaluate(object);
		long end = System.nanoTime();
		this.duration += (end - start);
		
				
		if(!appIntervals.isEmpty()){
	
			// set the time intervals and return the tuple
			// TODO intervals setzen.
			object.getMetadata().setApplicationIntervals(appIntervals);
			
			transfer(object);
			this.transfered++;
		}
	}
	
	@Override
	public ObjectTrackingSelectPO<T, M> clone() {
		return new ObjectTrackingSelectPO<T, M>(this);
	}
	
	@Override
	protected void process_done() {
		System.out.println("elements processed: " + this.counter);
		System.out.println("Brutto duration: " + this.duration);
		
		long additionalEvaluationDuration = 0;
		for(Entry<IPredicate<? super T>, IRangePredicate> entry : this.rangePredicates.entrySet()){
			additionalEvaluationDuration += entry.getValue().getAdditionalEvaluationDuration();
		}
		System.out.println("additional duration: " + additionalEvaluationDuration);
		System.out.println("Netto duration: " + (this.duration - additionalEvaluationDuration));
		
		System.out.println("elements transfered: " + this.transfered);
		System.out.println("elements per second: " + 1e9 * this.counter / (this.duration - additionalEvaluationDuration));
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

}

