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

public class Constant<T> implements IExpression<T> {

	private final T value;

	public Constant(T value) {
		this.value = value;
	}
	
	@Override
	public T getValue() {
		return value;
	}

	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Variable> getVariables() {
		return Collections.EMPTY_SET;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends T> getReturnType() {
		return (Class<? extends T>) value.getClass();
	}

	@Override
	public Variable getVariable(String name) {
		return null;
	}
	
	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public Variable toVariable() {
		throw new RuntimeException("cannot convert Constant to Variable");
	}

	@Override
	public IFunction<T> toFunction() {
		throw new RuntimeException("cannot convert Constant to IFunction");
	}

	@Override
	public Constant<T> toConstant() {
		return this;
	}
}
