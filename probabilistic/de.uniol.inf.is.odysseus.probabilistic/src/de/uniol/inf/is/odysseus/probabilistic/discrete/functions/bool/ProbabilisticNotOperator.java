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

package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.bool;

import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticResult;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticUnaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticNotOperator extends AbstractProbabilisticUnaryOperator<ProbabilisticResult> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4123516281481943058L;

	@Override
	public int getPrecedence() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "!";
	}

	@Override
	public ProbabilisticResult getValue() {
		ProbabilisticResult a = (ProbabilisticResult) getInputValue(0);

		AbstractProbabilisticValue<?> value = ((AbstractProbabilisticValue<?>) a.getValue()).clone();
		for (Entry<?, Double> entry : value.getValues().entrySet()) {
			entry.setValue(1.0 - entry.getValue());
		}
		return new ProbabilisticResult(1.0 - a.getProbability(), value);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.RIGHT_TO_LEFT;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_RESULT };

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > 0) {
			throw new IllegalArgumentException("! has only 1 argument.");
		}
		return accTypes;
	}

}
