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

package de.uniol.inf.is.odysseus.probabilistic.function;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.bool.OrOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticAndOperator extends AbstractBinaryOperator<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9218264809141056703L;

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Double> operator) {
		return operator.getClass() == OrOperator.class;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Double> operator) {
		return operator.getClass() == OrOperator.class;
	}

	@Override
	public int getPrecedence() {
		return 13;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public String getSymbol() {
		return "&&";
	}

	@Override
	public Double getValue() {
		return FastMath.min(getNumericalInputValue(0),
				getNumericalInputValue(1));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

	public static final SDFDatatype[] ACC_TYPES = new SDFDatatype[] {
		SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
		SDFDatatype.FLOAT, SDFDatatype.LONG, SDFDatatype.DOUBLE };

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity() - 1) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s).");
		}
		return ACC_TYPES;
	}

}
