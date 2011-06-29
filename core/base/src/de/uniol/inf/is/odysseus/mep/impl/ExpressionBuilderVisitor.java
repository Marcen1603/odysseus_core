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
package de.uniol.inf.is.odysseus.mep.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.mep.Constant;
import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.Variable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class ExpressionBuilderVisitor implements MEPImplVisitor {

	private Map<String, Variable> symbolTable = new HashMap<String, Variable>();

	@Override
	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTFunction node, Object data) {
		String symbol = node.getSymbol();
		if (!MEP.containsFunction(symbol)) {
			throw new IllegalArgumentException("no such function: " + symbol);
		}
		IFunction<?> function = MEP.getFunction(symbol);
		
		int arity = node.jjtGetNumChildren();
		IExpression<?>[] expressions = new IExpression[arity];
		for (int i = 0; i < arity; ++i) {
			// pass the accepted types of this function for the current argument
			expressions[i] = (IExpression<?>) node.jjtGetChild(i).jjtAccept(
					this, function.getAcceptedTypes(i));
		}
		function.setArguments(expressions);

		return function;
	}

	@Override
	public Object visit(ASTConstant node, Object data) {
		return new Constant<Object>(node.getValue(), SDFDatatype.OBJECT);
	}

	@Override
	public Object visit(ASTVariable node, Object data) {
		String identifier = node.getIdentifier();
		if (symbolTable.containsKey(identifier)) {
			Variable var = symbolTable.get(identifier);
			var.restrictAcceptedTypes((Class<?>[])data);
			return var;
		} else {
			Variable variable = new Variable(identifier);
			variable.setAcceptedTypes((Class<?>[])data);
			symbolTable.put(identifier, variable);
			return variable;
		}	
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		this.symbolTable.clear();
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	public Object visit(ASTMatrix node, Object data) {
		int childCount = node.jjtGetNumChildren();
		IExpression<?>[] values = new IExpression<?>[childCount];
		int valuesPerLine = 0;
		for (int i = 0; i < childCount; ++i) {
			values[i] = (MatrixLine) node.jjtGetChild(i).jjtAccept(this, data);
			if (i == 0) {
				valuesPerLine = values[0].toFunction().getArity();
			} else {
				if (values[i].toFunction().getArity() != valuesPerLine) {
					throw new IllegalArgumentException(
							"matrix is not rectangular");
				}
			}
		}

		return new MatrixFunction(values);
	}

	public Object visit(ASTMatrixLine node, Object data) {
		int childCount = node.jjtGetNumChildren();
		IExpression<?>[] values = new IExpression<?>[childCount];
		for (int i = 0; i < childCount; ++i) {
			values[i] = (IExpression<?>) node.jjtGetChild(i).jjtAccept(this, data);
		}
		return new MatrixLine(values);
	}

}
