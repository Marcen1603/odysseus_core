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

	public PredicateParameter() {
	}

	public PredicateParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public PredicateParameter(String name, REQUIREMENT requirement) {
		super(name, requirement, USAGE.RECENT);
	}

	@Override
	protected void internalAssignment() {
		String predicateType = "";
		String predicate = "";
		if (inputValue instanceof PredicateItem) {
			PredicateItem pItem = (PredicateItem) inputValue;
			predicateType = pItem.getPredicateType();
			predicate = pItem.getPredicate();
		} else if (inputValue instanceof String) {
			if (getAttributeResolver().getSchema().size() > 0) {
				predicateType = getAttributeResolver().getSchema().get(0)
						.getType().getName();
			}
			predicate = (String) inputValue;
		} else if( inputValue instanceof IPredicate ) {
			setValue((IPredicate<?>)inputValue);
			return;
		}
		@SuppressWarnings("rawtypes")
		IExpressionBuilder pBuilder = OperatorBuilderFactory
				.getExpressionBuilder(predicateType);

		if (pBuilder == null) {
			throw new IllegalArgumentException("unkown type of predicate: "
					+ predicateType);
		}
		setValue(pBuilder.createPredicate(getAttributeResolver(), predicate));
	}

	@Override
	public String getPQLStringInternal() {
		StringBuilder sb = new StringBuilder();
		if (inputValue instanceof PredicateItem) {
			PredicateItem pItem = (PredicateItem) inputValue;
			sb.append(pItem.getPredicateType());
			sb.append("('").append(getValue().toString()).append("')");
		} else if (inputValue instanceof String) {
			sb.append("'").append(inputValue).append("'");
		} else if( inputValue instanceof IPredicate ) {
			sb.append("'").append( ((IPredicate<?>)inputValue).toString()).append("'");
		}
		return sb.toString();
	}

}
