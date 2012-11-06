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
package de.uniol.inf.is.odysseus.core.server.mep.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;

public class ExpressionBuilderVisitor implements MEPImplVisitor {

    private Map<String, Variable> symbolTable = new HashMap<String, Variable>();
    private IAttributeResolver    attributeResolver;

    public ExpressionBuilderVisitor(IAttributeResolver attributeResolver) {
        // TODO Auto-generated constructor stub
        this.attributeResolver = attributeResolver;
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
        IExpression<?>[] expressions = new IExpression[arity];

        for (IFunction<?> function : functions) {
            List<SDFDatatype> parameter = new ArrayList<SDFDatatype>();
            for (int i = 0; i < arity; ++i) {
                // pass the accepted types of this function for the current
                // argument
                expressions[i] = (IExpression<?>) node.jjtGetChild(i).jjtAccept(this, function.getAcceptedTypes(i));
                SDFDatatype returnType = null;
                if (expressions[i] == null) {
                    throw new IllegalArgumentException("invalid parameter for function: " + node.getSymbol());
                }
                if (expressions[i].isFunction()) {
                    returnType = expressions[i].toFunction().getReturnType();
                }
                else if (expressions[i].isConstant()) {
                    returnType = expressions[i].toConstant().getReturnType();
                }
                else {
                    Variable variable = expressions[i].toVariable();
                    if (attributeResolver != null) {
                        SDFAttribute attribute = attributeResolver.getAttribute(variable.getIdentifier());
                        returnType = attribute.getDatatype();
                    }
                    else {
                        returnType = variable.getReturnType();
                    }
                }
                parameter.add(returnType);
                System.out.println("Expression: " + i + " " + expressions[i] + " => " + returnType);
            }
            selectedFunction = MEP.getFunction(function.getSymbol(), parameter);
            function.setArguments(expressions);
        }
        if (selectedFunction != null) {
            selectedFunction.setArguments(expressions);
        }
        return selectedFunction;
    }

    @Override
    public Object visit(ASTConstant node, Object data) {
        SDFDatatype type = SDFDatatype.OBJECT;
        if (node.getValue() instanceof Double) {
            type = SDFDatatype.DOUBLE;
        }
        if (node.getValue() instanceof Integer) {
            type = SDFDatatype.INTEGER;
        }
        if (node.getValue() instanceof Boolean) {
            type = SDFDatatype.BOOLEAN;
        }
        if (node.getValue() instanceof Long) {
            type = SDFDatatype.LONG;
        }
        if (node.getValue() instanceof String) {
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
        Variable variable = new Variable(identifier);
        variable.setAcceptedTypes((SDFDatatype[]) data);
        symbolTable.put(identifier, variable);
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
            }
            else {
                if (values[i].toFunction().getArity() != valuesPerLine) {
                    throw new IllegalArgumentException("matrix is not rectangular");
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
            values[i] = (IExpression<?>) node.jjtGetChild(i).jjtAccept(this, data);
        }
        return new MatrixLine(values);
    }

}
