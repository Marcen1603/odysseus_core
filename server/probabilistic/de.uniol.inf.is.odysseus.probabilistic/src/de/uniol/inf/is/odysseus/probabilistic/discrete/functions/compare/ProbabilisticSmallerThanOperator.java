/*
 * Copyright 2013 The Odysseus Team
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

package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.compare.SmallerThanOperator;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Smaller-Than operator for discrete probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticSmallerThanOperator extends SmallerThanOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9122605635777338549L;
	/**
	 * Accepted data types.
	 */
	private static final SDFDatatype[] ACC_TYPES1 = new SDFDatatype[] {
			SDFProbabilisticDatatype.PROBABILISTIC_BYTE,
			SDFProbabilisticDatatype.PROBABILISTIC_SHORT,
			SDFProbabilisticDatatype.PROBABILISTIC_INTEGER,
			SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE,
			SDFProbabilisticDatatype.PROBABILISTIC_LONG, SDFDatatype.BYTE,
			SDFDatatype.SHORT, SDFDatatype.INTEGER, SDFDatatype.LONG,
			SDFDatatype.DOUBLE, SDFDatatype.FLOAT };
	
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][]{
		ACC_TYPES1,ACC_TYPES1
	};
	
	public ProbabilisticSmallerThanOperator() {
		super(ACC_TYPES);
	}

}
