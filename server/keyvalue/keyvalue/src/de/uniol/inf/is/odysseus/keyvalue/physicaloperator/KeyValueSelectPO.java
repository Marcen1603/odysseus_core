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
package de.uniol.inf.is.odysseus.keyvalue.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;

/**
 * @author Jan Sören Schwarz
 */
public class KeyValueSelectPO<T extends KeyValueObject<?>> extends AbstractPipe<T, T> implements IHasPredicate {

	private IPredicate<? super T> predicate;
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();
	
	@Override
	public IPredicate<? super T> getPredicate() {
		return predicate;
	}

	public KeyValueSelectPO(IPredicate<? super T> predicate){
		this.predicate = predicate.clone();	
	}
	
	public KeyValueSelectPO(KeyValueSelectPO<T> po){
		super(po);
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
			//Removed sending negated elements to port 1 --> use Route instead (Selectivity measurement will always be one in this case)
			//transfer(object,1);
			heartbeatGenerationStrategy.generateHeartbeat(object, this);
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	public void process_open() throws OpenFailedException{
		this.predicate.init();
	}
	
	@Override
	public String toString(){
		return super.toString() + " predicate: " + this.getPredicate().toString(); 
	}

	public IHeartbeatGenerationStrategy<T> getHeartbeatGenerationStrategy() {
		return heartbeatGenerationStrategy;
	}

	public void setHeartbeatGenerationStrategy(IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}
}
