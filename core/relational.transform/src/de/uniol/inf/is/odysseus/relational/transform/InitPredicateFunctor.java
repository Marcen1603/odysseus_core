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
package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.IUnaryFunctor;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Initializes IRelationalal predicates with the input schemas of a given
 * ILogicalOperator.
 * @author Jonas Jacobi
 */
public class InitPredicateFunctor implements IUnaryFunctor<IPredicate<?>> {

	private final SDFAttributeList leftSchema;
	private final SDFAttributeList rightSchema;
	
	public InitPredicateFunctor(ILogicalOperator op) {
		this.leftSchema = op.getInputSchema(0);
		this.rightSchema = op.getNumberOfInputs() > 1 ? op.getInputSchema(1) : null;
	}
	@Override
	public void call(IPredicate<?> parameter) {
		if(parameter instanceof IRelationalPredicate) {
			((IRelationalPredicate)parameter).init(leftSchema, rightSchema);				
		}
	}

}
