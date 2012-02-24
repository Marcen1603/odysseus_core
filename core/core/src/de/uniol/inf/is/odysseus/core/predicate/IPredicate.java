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
package de.uniol.inf.is.odysseus.core.predicate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author Jonas Jacobi
 */
public interface IPredicate<T> extends IClone, Serializable {
	boolean evaluate(T input);
	boolean evaluate(T left, T right);
	@Override
	public IPredicate<T> clone();
	public void init();
	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> updated);
	public boolean equals(IPredicate<T> pred);
	boolean isContainedIn(Object o);
	public List<SDFAttribute> getAttributes();
}
