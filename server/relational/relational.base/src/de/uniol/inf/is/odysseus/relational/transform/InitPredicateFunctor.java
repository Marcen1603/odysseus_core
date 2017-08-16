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
package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.core.expression.IRelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.predicate.IUnaryFunctor;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;

/**
 * Initializes IRelationalal predicates with the input schemas of a given
 * ILogicalOperator.
 * @author Jonas Jacobi
 */
public class InitPredicateFunctor implements IUnaryFunctor<IPredicate<?>> {

	private final SDFSchema leftSchema;
	private final SDFSchema rightSchema;
	
	public InitPredicateFunctor(ILogicalOperator op) {
		this.leftSchema = op.getInputSchema(0);
		this.rightSchema = op.getNumberOfInputs() > 1 ? op.getInputSchema(1) : null;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void call(IPredicate<?> parameter) {
		if(parameter instanceof IRelationalPredicate) {
			((IRelationalPredicate<?>)parameter).init(leftSchema, rightSchema);				
		}else if (parameter instanceof IRelationalExpression){
			((RelationalExpression<?>)parameter).initVars(leftSchema, rightSchema);	
		}
	}

}
