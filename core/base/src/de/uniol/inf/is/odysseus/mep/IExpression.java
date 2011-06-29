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

import java.util.Set;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public interface IExpression<T> {
	public T getValue();
	public Object acceptVisitor(IExpressionVisitor visitor, Object data);
	public Set<Variable> getVariables();
	public Variable getVariable(String name);
	public SDFDatatype getReturnType();
	public boolean isVariable();
	public boolean isFunction();
	public boolean isConstant();
	public Variable toVariable();
	public IFunction<T> toFunction();
	public Constant<T> toConstant();
}
