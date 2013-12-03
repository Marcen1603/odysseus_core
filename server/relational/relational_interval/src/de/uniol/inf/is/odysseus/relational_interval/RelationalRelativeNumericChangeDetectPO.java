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

public class RelationalRelativeNumericChangeDetectPO extends
		AbstractRelationalNumericChangeDetectPO {

	public RelationalRelativeNumericChangeDetectPO(int[] comparePositions,
			double tolerance) {
		super(comparePositions, tolerance);
	}

	public RelationalRelativeNumericChangeDetectPO(
			RelationalRelativeNumericChangeDetectPO pipe) {
		super(pipe);
	}

	@Override
	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement) {
		for (int i : comparePositions) {
			Number a = object.getAttribute(i);
			Number b = lastElement.getAttribute(i);
			if (a.doubleValue() != 0) {
				if (Math.abs(1 - b.doubleValue() / a.doubleValue()) > tolerance) {
					return true;
				}
			}else{
				// Need special handling if last value is 0 (0.1 * 0 is 0)
				if (b.doubleValue() != 0){
					return true;
				}
			}
			return false;
		}

		return false;
	}

}
