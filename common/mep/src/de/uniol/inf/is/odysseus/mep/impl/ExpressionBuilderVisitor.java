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

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.intern.Constant;
import de.uniol.inf.is.odysseus.mep.intern.Variable;

public class ExpressionBuilderVisitor implements MEPImplVisitor {

    private final Map<String, Variable> symbolTable = new HashMap<>();
    private final List<SDFSchema> schema;

    public ExpressionBuilderVisitor(final List<SDFSchema> schema) {
        this.schema = schema;
    }

    public ExpressionBuilderVisitor(final SDFSchema schema) {
        this.schema = new LinkedList<>();
        this.schema.add(schema);
    }

    @Override
    public Object visit(final SimpleNode node, final Object data) {
        return null;
    }

    @Override
    public Object visit(final ASTFunction node, final Object data) {
        final String symbol = node.getSymbol();
        if (!MEP.containsFunction(symbol)) {
            throw new IllegalArgumentException("Function  \"" + symbol
                    + "\" cannot be found.");
        }
        final List<IMepFunction<?>> functions = MEP.getFunctions(symbol);
        IMepFunction<?> selectedFunction = null;

        final int arity = node.jjtGetNumChildren();

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
            if (selectedFunction.getClass() == MatrixLine.class) {
                selectedFunction = new MatrixLine(new IMepExpression[arity]);
            }
            final IMepExpression<?>[] expressions = new IMepExpression[arity];
            for (int i = 0; i < arity; ++i) {
                // pass the accepted types of this function for the current
                // argument
                expressions[i] = (IMepExpression<?>) node.jjtGetChild(i)
                        .jjtAccept(this, selectedFunction.getAcceptedTypes(i));
            }
            selectedFunction.setArguments(expressions);
        } else {
            final IMepExpression<?>[] expressions = new IMepExpression[arity];

            for (final IMepFunction<?> function : functions) {
                if (arity == function.getArity()) {
                    final List<SDFDatatype> parameters = new ArrayList<>();
                    for (int i = 0; i < arity; ++i) {
                        expressions[i] = (IMepExpression<?>) node.jjtGetChild(i)
                                .jjtAccept(this, function.getAcceptedTypes(i));
                    }
                    for (int i = 0; i < arity; ++i) {
                        final SDFDatatype rt = expressions[i].getReturnType();
                        parameters.add(rt);
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
                // InfoService.error("No function symbol for current Parameters found",
                // null, "ExpressionParser");
                final StringBuffer params = new StringBuffer();
                final StringBuffer exprStr = new StringBuffer();
                for (int i = 0; i < node.children.length; i++) {
                    // params.append(((IExpression<?>)n).getReturnType());
                    final IMepExpression<?> expr = (IMepExpression<?>) node.jjtGetChild(i)
                            .jjtAccept(this, null);
                    exprStr.append(expr+"("+expr.getReturnType()+") ");
                }

                throw new IllegalArgumentException("no function " + symbol
                        + " for input parameter " + params + " in " + exprStr
                        + " found !");
                // for (int i = 0; i < arity; ++i) {
                // expressions[i] = (IExpression<?>) node.jjtGetChild(i)
                // .jjtAccept(this,
                // selectedFunction.getAcceptedTypes(i));
                // }
            }

            selectedFunction.setArguments(expressions);
        }
        // }
        if (selectedFunction != null) {
            if ((this.schema != null) && (this.schema.size() > 0)) {
                final SDFConstraint c = this.schema.get(0).getConstraint(
                        SDFConstraint.BASE_TIME_UNIT);
                if (c != null) {
                    selectedFunction.setBasetimeUnit((TimeUnit) c.getValue());
                }
            }
        }

        return selectedFunction;
    }

    @Override
    public Object visit(final ASTConstant node, final Object data) {
        SDFDatatype type = SDFDatatype.OBJECT;
        if (node.getValue() instanceof Double) {
            type = SDFDatatype.DOUBLE;
        } else if (node.getValue() instanceof Float) {
            type = SDFDatatype.FLOAT;
        } else if (node.getValue() instanceof Short) {
            type = SDFDatatype.SHORT;
        } else if (node.getValue() instanceof Byte) {
            type = SDFDatatype.BYTE;
        } else if (node.getValue() instanceof Integer) {
            type = SDFDatatype.INTEGER;
        } else if (node.getValue() instanceof Boolean) {
            type = SDFDatatype.BOOLEAN;
        } else if (node.getValue() instanceof Long) {
            type = SDFDatatype.LONG;
        } else if (node.getValue() instanceof String) {
            type = SDFDatatype.STRING;
        }
        return new Constant<>(node.getValue(), type);
    }

    @Override
    public Object visit(final ASTVariable node, final Object data) {
        final String identifier = node.getIdentifier();
        if (this.symbolTable.containsKey(identifier)) {
            final Variable var = this.symbolTable.get(identifier);
            var.restrictAcceptedTypes((SDFDatatype[]) data);
            return var;
        }
        Variable variable;
        if (this.schema != null) {
            SDFAttribute attribute = null;
            for (final SDFSchema s : this.schema) {
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
        this.symbolTable.put(identifier, variable);

        // Check for Array
        // if(node.jjtGetNumChildren() == 1) {
        // variable.setArrayIndex((Integer) node.jjtGetChild(0).jjtAccept(this,
        // data));
        // }
        return variable;
    }

    @Override
    public Object visit(final ASTExpression node, final Object data) {
        this.symbolTable.clear();
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    @Override
    public Object visit(final ASTMatrix node, final Object data) {
        final int childCount = node.jjtGetNumChildren();
        final IMepExpression<?>[] values = new IMepExpression<?>[childCount];
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
    public Object visit(final ASTMatrixLine node, final Object data) {
        final int childCount = node.jjtGetNumChildren();
        final IMepExpression<?>[] values = new IMepExpression<?>[childCount];
        for (int i = 0; i < childCount; ++i) {
            values[i] = (IMepExpression<?>) node.jjtGetChild(i).jjtAccept(this,
                    data);
        }
        return new MatrixLine(values);
    }

}
