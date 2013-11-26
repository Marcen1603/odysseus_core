/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.NonSymmetricMatrixException;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class MatrixDeterminantFunction extends AbstractFunction<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5178021143045609051L;
	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE,
			SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE };

	@Override
	public String getSymbol() {
		return "det";
	}

	@Override
	public Double getValue() {
		RealMatrix a = MatrixUtils.createRealMatrix((double[][]) this
				.getInputValue(0));
		return getValueInternal(a);
	}

	protected double getValueInternal(RealMatrix a) {
		try {
			return new CholeskyDecomposition(a).getDeterminant();
		} catch (NonSymmetricMatrixException
				| NonPositiveDefiniteMatrixException e) {
			return new LUDecomposition(a).getDeterminant();
		}
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

	@Override
	public int getArity() {
		return 1;
	}

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
		return accTypes;
	}

}
