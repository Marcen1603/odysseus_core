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

public class AbstractRelationalNumericChangeDetectPO extends RelationalChangeDetectPO {

	final protected double tolerance;

	public AbstractRelationalNumericChangeDetectPO(int[] comparePositions, double tolerance) {
		super(comparePositions);
		this.tolerance = tolerance;
	}

	public AbstractRelationalNumericChangeDetectPO(AbstractRelationalNumericChangeDetectPO pipe) {
		super(pipe);
		this.tolerance = pipe.tolerance; 
	}

	@Override
	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement) {
		for (int i:comparePositions){
			Number a = object.getAttribute(i);
			Number b = lastElement.getAttribute(i);
			if (Math.abs(a.doubleValue()-b.doubleValue()) <= tolerance){
				return true;
			}
			return false;
		}
		
		return false;
	}
	
	
}
