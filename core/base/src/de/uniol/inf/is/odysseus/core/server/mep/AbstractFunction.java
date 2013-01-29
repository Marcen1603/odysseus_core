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
package de.uniol.inf.is.odysseus.core.server.mep;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionVisitor;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.Variable;

public abstract class AbstractFunction<T> implements IFunction<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3805396798229438499L;
	private IExpression<?>[] arguments;
	private Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();

	@Override
	final public void setArguments(IExpression<?>... arguments) {
		if (arguments.length != getArity()) {
			throw new IllegalArgumentException(
					"illegal number of arguments for function " + getSymbol());
		}

		this.arguments = new IExpression<?>[getArity()];
		for (int i = 0; i < arguments.length; i++) {
			setArgument(i, arguments[i]);
		}
	}

	@Override
	public void setArgument(int argumentPosition, IExpression<?> argument) {
		this.arguments[argumentPosition] = argument;
	}

	@Override
	public IExpression<?>[] getArguments() {
		return arguments;
	}

	@Override
	public Map<String, Serializable> getAdditionalContents() {
		return additionalContent;
	}

	@Override
	public Serializable getAdditionalContent(String name) {
		return additionalContent.get(name);
	}

	@SuppressWarnings("unchecked")
	final protected <S> S getInputValue(int argumentPos) {
		return (S) arguments[argumentPos].getValue();
	}

	final protected Double getNumericalInputValue(int argumentPos) {
		return ((Number) arguments[argumentPos].getValue()).doubleValue();
	}

	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public Set<Variable> getVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		for (int i = 0; i < getArity(); ++i) {
			variables.addAll(getArguments()[i].getVariables());
		}
		return variables;
	}

	@Override
	public Variable getVariable(String name) {
		Set<Variable> variables = getVariables();
		for (Variable curVar : variables) {
			if (curVar.getIdentifier().equals(name)) {
				return curVar;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getSymbol());
		builder.append('(');
		if (getArity() > 0) {
			if ((getArguments() != null) && (getArguments().length > 0)) {
				builder.append(getArguments()[0].toString());
				for (int i = 1; i < getArity(); ++i) {
					builder.append(", ");
					builder.append(getArguments()[i].toString());
				}
			}
		}
		builder.append(')');
		return builder.toString();
	}

	@Override
	public boolean optimizeConstantParameter() {
		return true;
	}

	@Override
	public IExpression<?> getArgument(int argumentPosition) {
		return this.arguments[argumentPosition];
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public Variable toVariable() {
		throw new RuntimeException("cannot convert IFunction to Variable");
	}

	@Override
	public IFunction<T> toFunction() {
		return this;
	}

	@Override
	public Constant<T> toConstant() {
		throw new RuntimeException("cannot convert IFunction to Constant");
	}
}
