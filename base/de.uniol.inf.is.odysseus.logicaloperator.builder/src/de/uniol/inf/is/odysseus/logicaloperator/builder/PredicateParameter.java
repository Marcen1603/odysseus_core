package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;

public class PredicateParameter extends AbstractParameter<IPredicate<?>> {

	public PredicateParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
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

}
