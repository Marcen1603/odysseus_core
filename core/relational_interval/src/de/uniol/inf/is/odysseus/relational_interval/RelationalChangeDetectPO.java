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

import de.uniol.inf.is.odysseus.core.server.physicaloperator.ChangeDetectPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

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

	@Override
	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement) {
		for (int i:comparePositions){
			Object a = object.getAttribute(i);
			Object b = lastElement.getAttribute(i);
			if (!a.equals(b)){
				return true;
			}
		}
		
		return false;
	}
	
	
}
