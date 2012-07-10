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
package de.uniol.inf.is.odysseus.markov.operator.logical;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;

public class MarkovAO extends AggregateAO{

	
	private static final long serialVersionUID = 2707805704896762434L;
		
	private HiddenMarkovModel hmm;	

	public MarkovAO(HiddenMarkovModel hmm){
		this.hmm = hmm;		
	}
	
	public MarkovAO(MarkovAO markovAO){
		super(markovAO);		
		this.hmm = markovAO.hmm;
	}
	
	@Override
	public MarkovAO clone() {
		return new MarkovAO(this);
	}
	
	public HiddenMarkovModel getHiddenMarkovModel() {
		return hmm;
	}

	public void setHiddenMarkovModel(HiddenMarkovModel hmm) {
		this.hmm = hmm;
	}
	
	

}
