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
package de.uniol.inf.is.odysseus.markov.model.statemachine;

public class Transition {

	private double probability;
	private State nextState;
	private State sourceState;
	
	public Transition(State nextState, double probabilty){
		this.probability = probabilty;
		this.nextState = nextState;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public State getNextState() {
		return nextState;
	}

	public void setNextState(State nextState) {
		this.nextState = nextState;
	}	
	
	@Override
	public String toString() {
		return " --> "+this.nextState+ " : "+this.probability;		
	}

	public void setSourceState(State s){
		this.sourceState = s;
	}
	
	public State getSourceState() {	
		return this.sourceState;
	}
	
}
