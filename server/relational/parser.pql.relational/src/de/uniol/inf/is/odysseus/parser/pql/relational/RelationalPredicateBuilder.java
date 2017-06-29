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
package de.uniol.inf.is.odysseus.parser.pql.relational;

import de.uniol.inf.is.odysseus.core.expression.IExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractExpressionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;

public class RelationalPredicateBuilder<M extends IMetaAttribute> extends AbstractExpressionBuilder {

	@Override
	public IPredicate<?> createPredicate(IAttributeResolver resolver,
			String predicate) {
		return (IPredicate<?>) createExpression(resolver, predicate);
	}

	@Override
	public IExpression createExpression(IAttributeResolver resolver, String expression) {
		SDFExpression expression1 = new SDFExpression("", expression, resolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		RelationalExpression<?> pred = new RelationalExpression<>(expression1);
		pred.initVars(resolver.getSchema());
		return pred;
	}

}
