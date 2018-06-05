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
package de.uniol.inf.is.odysseus.parser.pql.priority;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractExpressionBuilder;

public class PriorityPredicateBuilder<T extends IStreamObject<M>, M extends IMetaAttribute> extends AbstractExpressionBuilder<T,M> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IPredicate<T> createPredicate(IAttributeResolver resolver,
			String predicate) {
		return new PriorityPredicate();
	}

	@Override
	public String getName() {
		return "PRIORITYPREDICATE";
	}

	@Override
	public String getAliasName() {
		return null;
	}

}
