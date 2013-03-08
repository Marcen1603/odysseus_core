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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

public class SQIMVN extends AbstractFunction<Double> {

	private static final long serialVersionUID = 1L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE },
			{ SDFProbabilisticDatatype.MULTIVARIATE_COVARIANCE_MATRIX },
			{ SDFDatatype.VECTOR_DOUBLE }, { SDFDatatype.VECTOR_DOUBLE } };

	@Override
	public String getSymbol() {
		return "SQIMVN";
	}

	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDFDatatype getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

}
