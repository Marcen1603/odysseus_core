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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

public class PredicateParameter extends AbstractParameter<IPredicate<?>> {

	private static final long serialVersionUID = 271263573427804690L;

	public PredicateParameter(){}
	
	public PredicateParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}
	
	public PredicateParameter(String name, REQUIREMENT requirement) {
		super(name, requirement, USAGE.RECENT);
	}

	@Override
	protected void internalAssignment() {
		PredicateItem pItem = (PredicateItem) inputValue;
		IPredicateBuilder pBuilder = OperatorBuilderFactory.getPredicateBuilder(pItem
				.getPredicateType());

		if (pBuilder == null) {
			throw new IllegalArgumentException("unkown type of predicate: "
					+ pItem.getPredicateType());
		}
		setValue(pBuilder.createPredicate(getAttributeResolver(), pItem
				.getPredicate()));
	}
	
	@Override
	public String getPQLStringInternal() {
		StringBuilder sb = new StringBuilder();
		PredicateItem pItem = (PredicateItem) inputValue;
		sb.append(pItem.getPredicateType());
		sb.append("('").append(getValue().toString()).append("')");
		return sb.toString();
	}

}
