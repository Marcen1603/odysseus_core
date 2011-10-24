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

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDefaultPriority;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTElementPriorities;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTElementPriority;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.PriorityAO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public class CreatePriorityAOVisitor extends AbstractDefaultVisitor {

	private ILogicalOperator top;
	private AttributeResolver resolver;

	public CreatePriorityAOVisitor() {
	}

	public void setTopOperator(ILogicalOperator top) {
		this.top = top;
		initResolver();
	}

	private void initResolver() {
		if (this.top == null || this.resolver == null) {
			return;
		}
	}

	public void setAttributeResolver(AttributeResolver resolver) {
		this.resolver = resolver;
		initResolver();
	}

	@Override
	public Object visit(ASTElementPriorities node, Object data) throws QueryParseException {
		PriorityAO<RelationalTuple<? extends IPriority>> ao = new PriorityAO<RelationalTuple<? extends IPriority>>();
		top.subscribeSink(ao, 0, 0, top.getOutputSchema());
		node.childrenAccept(this, ao);
		top = ao;
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTElementPriority node, Object data) throws QueryParseException {
		PriorityAO<RelationalTuple<IPriority>> ao = (PriorityAO<RelationalTuple<IPriority>>) data;

		IPredicate<RelationalTuple<?>> predicate;
		IAttributeResolver tmpResolver = new DirectAttributeResolver(top
				.getOutputSchema());
		// TODO resolving mit dem anderen resolver auch machen und
		// dementsprechend position des aos setzen
		predicate = CreatePredicateVisitor.toPredicate(node.getPredicate(),
				tmpResolver);
		ao.setPriority(node.getPriority(), predicate);
		return data;
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	@Override
	public Object visit(ASTDefaultPriority node, Object data) throws QueryParseException {
		((PriorityAO<RelationalTuple>) data).setDefaultPriority(node
				.getPriority());
		return data;
	}

	public ILogicalOperator getTopOperator() {
		return top;
	}
}
