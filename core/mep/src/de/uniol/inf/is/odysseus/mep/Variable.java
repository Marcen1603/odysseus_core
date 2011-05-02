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
package de.uniol.inf.is.odysseus.mep;

import java.util.Collections;
import java.util.Set;

public class Variable implements IExpression<Object> {
	private Object value;
	private final String identifier;
	private final Class<?> type;

	public Variable(String id) {
		this.identifier = id;
		this.type = Object.class;
	}

	public Variable(String id, Class<?> type) {
		this.identifier = id;
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void bind(Object value) {
		if (value instanceof Number) {
			value = ((Number) value).doubleValue();
		}
		this.value = value;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		return identifier + "[ " + value + " ]";
	}

	@Override
	public boolean equals(Object obj) {
		return ((Variable) obj).identifier.equals(this.identifier);
	}

	@Override
	public int hashCode() {
		return this.identifier.hashCode();
	}

	@Override
	public Set<Variable> getVariables() {
		return Collections.singleton(this);
	}

	@Override
	public Variable getVariable(String varName) {
		return this.identifier.equals(varName) ? this : null;
	}

	@Override
	public Class<?> getReturnType() {
		return type;
	}

	@Override
	public boolean isVariable() {
		return true;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public Variable toVariable() {
		return this;
	}

	@Override
	public IFunction<Object> toFunction() {
		throw new RuntimeException("cannot convert Variable to IFunction");
	}

	@Override
	public Constant<Object> toConstant() {
		throw new RuntimeException("cannot convert Variable to Constant");
	}
}
