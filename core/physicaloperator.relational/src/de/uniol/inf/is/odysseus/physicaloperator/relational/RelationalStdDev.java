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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AbstractListAggregation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ListPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class RelationalStdDev
		extends
		AbstractListAggregation<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8289285906323304991L;
	final int attribPos;

	public RelationalStdDev(int[] pos) {
		super("STDDEV");
		this.attribPos = pos[0];
	}

	@Override
	public Tuple<? extends IMetaAttribute> evaluate(
			IPartialAggregate<Tuple<? extends IMetaAttribute>> p) {
		ListPartialAggregate<Tuple<? extends IMetaAttribute>> list = (ListPartialAggregate<Tuple<? extends IMetaAttribute>>) p;
		int n = list.size();
		if (n > 0) {
			// Calc Average
			double sum = 0;
			for (Tuple<? extends IMetaAttribute> tuple : list) {
				sum = sum
						+ ((Number) (tuple.getAttribute(attribPos)))
								.doubleValue();
			}
			double avg = sum / n;
			// Calc Sum
			double stddev = 0.0;
			for (Tuple<? extends IMetaAttribute> tuple : list) {
				stddev += Math.pow((((Number) (tuple.getAttribute(attribPos)))
						.doubleValue() - avg), 2);
			}
			stddev = (1.0 / (n-1.0)) * stddev;
			stddev = Math.sqrt(stddev);
			Tuple<IMetaAttribute> returnVal = new Tuple<IMetaAttribute>(
					1, false);
			returnVal.setAttribute(0, stddev);
			return returnVal;
		}
        return null;
	}

}
