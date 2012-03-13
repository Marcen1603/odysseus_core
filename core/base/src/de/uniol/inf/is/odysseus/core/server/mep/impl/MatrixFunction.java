/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.core.server.mep.impl;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * Internal function for building arrays.
 * Expects Double[] as arguments and builds a matrix out of them.
 * No check for rectangularity is applied. This is checked in the ExpressionBuilderVisitor!
 */
public class MatrixFunction extends AbstractFunction<double[][]> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -742218734048620320L;
	private int arity;

	public MatrixFunction(IExpression<?>[] lines) {
		this.arity = lines.length;
		setArguments(lines);
	}
	
	@Override
	public int getArity() {
		return arity;
	}

	@Override
	public String getSymbol() {
		return "__matrix";
	}

	@Override
	public double[][] getValue() {
		int arity = getArity();
		double[][] value = new double[arity][];
		for(int i = 0; i < arity; ++i){
			value[i] = (double[]) getArgument(i).getValue();
		}
		
		return value;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.MATRIX_DOUBLE;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[]{SDFDatatype.VECTOR_DOUBLE};
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > arity){
			throw new IllegalArgumentException("AbsValue has only 1 argument.");
		}
        return accTypes;
	}
}
