/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryNumberInputOperator;

/**
 * @author Cornelius Ludmann
 *
 */
public class MaxOperator extends AbstractBinaryNumberInputOperator<Double> {

	private static final long serialVersionUID = 1L;

	public MaxOperator() {
		super("MAX", SDFDatatype.DOUBLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IOperator#getPrecedence()
	 */
	@Override
	public int getPrecedence() {
		return 7;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public Double getValue() {
		return Math.max(getNumericalInputValue(0), getNumericalInputValue(1));
	}

}
