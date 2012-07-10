/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.markov.model.algorithm;

import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;

public abstract class AbstractMarkovAlgorithm implements IMarkovAlgorithm{
	
	protected HiddenMarkovModel hmm;
	private boolean initiated = false;
	
	@Override
	public void create(HiddenMarkovModel hmm) {
		this.hmm = hmm;		
	}

	public abstract void init(Observation o);
	
	public abstract void step(Observation o);

	@Override
	public synchronized void next(Observation o) {
		if(!this.initiated ){
			this.initiated = true;
			init(o);
		}else{
			step(o);
		}
		
	}
	

}
