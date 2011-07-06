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
package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.parser.cql.parser.AbstractQuantificationPredicate;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * This is a dummy class for the conversion of the AST to real predicates. In a
 * later stage the QuantificationPredicates get converted to Existenceoperators,
 * so there is no need for evaluation methods.
 * 
 * @author Jonas Jacobi
 */
public class QuantificationPredicate extends
		AbstractPredicate<RelationalTuple<?>> {

	private static final long serialVersionUID = -125391059150938434L;
	private AbstractQuantificationPredicate astPredicate;

	public QuantificationPredicate(AbstractQuantificationPredicate astPredicate) {
		super();
		this.astPredicate = astPredicate;
	}

	public QuantificationPredicate(QuantificationPredicate pred) {
		this.astPredicate = pred.astPredicate;
	}

	@Override
	public boolean evaluate(RelationalTuple<?> input) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean evaluate(RelationalTuple<?> left, RelationalTuple<?> right) {
		throw new RuntimeException("Not implemented");
	}

	public AbstractQuantificationPredicate getAstPredicate() {
		return astPredicate;
	}

	public void setAstPredicate(AbstractQuantificationPredicate astPredicate) {
		this.astPredicate = astPredicate;
	}

	@Override
	public QuantificationPredicate clone() {
		return new QuantificationPredicate(this);
	}
	
	@Override
	public boolean equals(IPredicate pred) {
		if(!(pred instanceof QuantificationPredicate)) {
			return false;
		}
		if(astPredicate.equals(((QuantificationPredicate)pred).astPredicate)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof QuantificationPredicate)) {
			return false;
		}
		if(astPredicate.equals(((QuantificationPredicate)o).astPredicate)) {
			return true;
		}
		return false;
	}
}
