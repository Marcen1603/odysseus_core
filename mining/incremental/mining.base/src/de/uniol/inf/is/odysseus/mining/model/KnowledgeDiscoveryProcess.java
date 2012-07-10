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
package de.uniol.inf.is.odysseus.mining.model;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class KnowledgeDiscoveryProcess {

	private List<IPhase> phases = new ArrayList<IPhase>();
	private String name;
	
	public KnowledgeDiscoveryProcess(String name) {
		this.name = name;
	}


	public List<ILogicalOperator> buildLogicalPlan(){
		List<ILogicalOperator> topOps = new ArrayList<ILogicalOperator>();
		for(IPhase phase : this.phases){
			topOps.addAll(phase.buildLogicalPlan());
		}		
		return topOps;
	}


	public void setPhases(List<IPhase> phases) {
		this.phases = phases;		
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Knowledge Discovery Process with name \""+this.name+"\"\n");
		for(IPhase p : this.phases){
			sb.append("  "+p.toString()+"\n");
		}
		return sb.toString();
	}
		
}
