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
package de.uniol.inf.is.odysseus.mep.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.MEP;

public class ExpressionBuilderVisitor implements MEPImplVisitor {

	private Map<String, Variable> symbolTable = new HashMap<String, Variable>();
	private final List<SDFSchema> schema;

	public ExpressionBuilderVisitor(List<SDFSchema> schema) {
		this.schema = schema;
	}

	public ExpressionBuilderVisitor(SDFSchema schema) {
		this.schema = new LinkedList<>();
		this.schema.add(schema);
	}

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
		List<IFunction<?>> functions = MEP.getFunctions(symbol);
		IFunction<?> selectedFunction = null;

		int arity = node.jjtGetNumChildren();

		// if (schema != null) {
		// for (IFunction<?> function : functions) {
		// List<SDFDatatype> parameter = new ArrayList<SDFDatatype>();
		// for (int i = 0; i < arity; ++i) {
		// // pass the accepted types of this function for the current
		// // argument
		// expressions[i] = (IExpression<?>) node.jjtGetChild(i).jjtAccept(this,
		// function.getAcceptedTypes(i));
		// SDFDatatype returnType = null;
		// if (expressions[i] == null) {
		// throw new IllegalArgumentException("invalid parameter for function: "
		// + node.getSymbol());
		// }
		// if (expressions[i].isFunction()) {
		// returnType = expressions[i].toFunction().getReturnType();
		// }
		// else if (expressions[i].isConstant()) {
		// returnType = expressions[i].toConstant().getReturnType();
		// }
		// else {
		// Variable variable = expressions[i].toVariable();
		// SDFAttribute attribute =
		// schema.findAttribute(variable.getIdentifier());
		// if (attribute != null) {
		// returnType = attribute.getDatatype();
		// }
		// else {
		// System.out.println("Attribute is null " + variable);
		// returnType = variable.getReturnType();
		// }
		// }
		// parameter.add(returnType);
		// }
		// selectedFunction = MEP.getFunction(function.getSymbol(), parameter);
		// function.setArguments(expressions);
		// }
		// if (selectedFunction != null) {
		// selectedFunction.setArguments(expressions);
		// }
		// }
		// else {

		if (functions.size() == 0) {
			throw new IllegalArgumentException("no such function: " + symbol);
		} else if (functions.size() == 1) {
			selectedFunction = functions.get(0);
			IExpression<?>[] expressions = new IExpression[arity];
			for (int i = 0; i < arity; ++i) {
				// pass the accepted types of this function for the current
				// argument
				expressions[i] = (IExpression<?>) node.jjtGetChild(i)
						.jjtAccept(this, selectedFunction.getAcceptedTypes(i));
			}
			selectedFunction.setArguments(expressions);
		} else {
			IExpression<?>[] expressions = new IExpression[arity];

			for (IFunction<?> function : functions) {
				if (arity == function.getArity()) {
					List<SDFDatatype> parameters = new ArrayList<SDFDatatype>();
					for (int i = 0; i < arity; ++i) {
						expressions[i] = (IExpression<?>) node.jjtGetChild(i)
								.jjtAccept(this, function.getAcceptedTypes(i));
					}
					for (int i = 0; i < arity; ++i) {
						parameters.add(expressions[i].getReturnType());
					}
					selectedFunction = MEP.getFunction(symbol, parameters);
					if (selectedFunction != null) {
						break;
					}
				}
			}
			// If no function match the parameter use the first available
			// function
			if (selectedFunction == null) {
				selectedFunction = functions.get(0);
				for (int i = 0; i < arity; ++i) {
					expressions[i] = (IExpression<?>) node.jjtGetChild(i)
							.jjtAccept(this,
									selectedFunction.getAcceptedTypes(i));
				}
			}

			selectedFunction.setArguments(expressions);
		}
		// }
		if (selectedFunction != null) {
			if (schema != null && schema.size() > 0) {
				SDFConstraint c = schema.get(0).getConstraint(
						SDFConstraint.BASE_TIME_UNIT);
				if (c != null)
					selectedFunction.setBasetimeUnit((TimeUnit) c.getValue());
			}
		}
		return selectedFunction;
	}

	@Override
	public Object visit(ASTConstant node, Object data) {
		SDFDatatype type = SDFDatatype.OBJECT;
		if (node.getValue() instanceof Double) {
			type = SDFDatatype.DOUBLE;
		}else if (node.getValue() instanceof Float) {
			type = SDFDatatype.FLOAT;
		}else if (node.getValue() instanceof Short) {
			type = SDFDatatype.SHORT;
		}else if (node.getValue() instanceof Byte) {
			type = SDFDatatype.BYTE;
		}else if (node.getValue() instanceof Integer) {
			type = SDFDatatype.INTEGER;
		}else if (node.getValue() instanceof Boolean) {
			type = SDFDatatype.BOOLEAN;
		}else if (node.getValue() instanceof Long) {
			type = SDFDatatype.LONG;
		}else if (node.getValue() instanceof String) {
			type = SDFDatatype.STRING;
		}
		return new Constant<Object>(node.getValue(), type);
	}

	@Override
	public Object visit(ASTVariable node, Object data) {
		String identifier = node.getIdentifier();
		if (symbolTable.containsKey(identifier)) {
			Variable var = symbolTable.get(identifier);
			var.restrictAcceptedTypes((SDFDatatype[]) data);
			return var;
		}
		Variable variable;
		if (this.schema != null) {
			SDFAttribute attribute = null;
			for (SDFSchema s : schema) {
				attribute = s.findAttribute(identifier);
				if (attribute != null) {
					break;
				}
			}
			if (attribute != null) {
				variable = new Variable(identifier, attribute.getDatatype());
			} else {
				variable = new Variable(identifier);
			}
		} else {
			variable = new Variable(identifier);
		}
		variable.setAcceptedTypes((SDFDatatype[]) data);
		symbolTable.put(identifier, variable);
		
		//Check for Array
//		if(node.jjtGetNumChildren() == 1) {
//			variable.setArrayIndex((Integer) node.jjtGetChild(0).jjtAccept(this, data));
//		}
		return variable;
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		this.symbolTable.clear();
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	@Override
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

	@Override
	public Object visit(ASTMatrixLine node, Object data) {
		int childCount = node.jjtGetNumChildren();
		IExpression<?>[] values = new IExpression<?>[childCount];
		for (int i = 0; i < childCount; ++i) {
			values[i] = (IExpression<?>) node.jjtGetChild(i).jjtAccept(this,
					data);
		}
		return new MatrixLine(values);
	}

}
