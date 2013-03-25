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
package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ChangeDetectPO;

public class RelationalChangeDetectPO extends ChangeDetectPO<Tuple<?>> {

	final protected int[] comparePositions;

	public RelationalChangeDetectPO(int[] comparePositions) {
		super();
		this.comparePositions = comparePositions;
		StringBuffer tmp = new StringBuffer(" ");
		for (int i:comparePositions){
			tmp.append(i).append(",");
		}
		setName(getName()+tmp);
	}

	public RelationalChangeDetectPO(RelationalChangeDetectPO pipe) {
		super(pipe);
		this.comparePositions = pipe.comparePositions;
	}

	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement){
		return !Tuple.equalsAt(object, lastElement, comparePositions);
	}		
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (! (ipo instanceof RelationalChangeDetectPO)){
			return false;
		}
		
		RelationalChangeDetectPO other = (RelationalChangeDetectPO) ipo;
			
		boolean result = super.isSemanticallyEqual(other) ;
		
		if (this.comparePositions.length != other.comparePositions.length){
			return false;
		}

		for (int i=0;i<this.comparePositions.length;i++){
			if (comparePositions[i] != other.comparePositions[i]){
				return false;
			}
		}
		
		return result;
		
	}
	
}
