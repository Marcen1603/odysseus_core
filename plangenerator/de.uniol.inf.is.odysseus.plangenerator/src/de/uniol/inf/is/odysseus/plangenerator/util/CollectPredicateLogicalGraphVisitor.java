/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.plangenerator.util;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;

public class CollectPredicateLogicalGraphVisitor<T extends ILogicalOperator>
		implements IGraphNodeVisitor<T, Set<IRelationalPredicate>> {

	private AccessAO access;
	private Pair<AccessAO, AccessAO> pair;
	private Set<IRelationalPredicate> predicates;

	public CollectPredicateLogicalGraphVisitor(AccessAO access) {
		this.access = access;
		this.pair = null;
		this.predicates = new HashSet<IRelationalPredicate>();
	}

	public CollectPredicateLogicalGraphVisitor(Pair<AccessAO, AccessAO> pair) {
		this.access = null;
		this.pair = pair;
		this.predicates = new HashSet<IRelationalPredicate>();
	}

	@Override
	public void nodeAction(T node) {
		for (IPredicate<?> predicate : node.getPredicates()) {
			for (SDFAttribute attribute : predicate.getAttributes()) {
				// FIXME: das funktioniert solange keine komplexeren operatoren wie rename dazukommen :D
				// FIXME: vor jedem accessao-sourcename steht noch System. davor...
				if (this.access != null && attribute.getSourceName().equals(this.access.getSourcename().substring(7))) {
					predicates.add((IRelationalPredicate) predicate);
				} else if(this.pair != null && 
						(attribute.getSourceName().equals(this.pair.getE1().getSourcename().substring(7)) 
								|| attribute.getSourceName().equals(this.pair.getE2().getSourcename()))) {
					predicates.add((IRelationalPredicate) predicate);
				}
			}
		}
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<IRelationalPredicate> getResult() {
		return this.predicates;
	}

}
