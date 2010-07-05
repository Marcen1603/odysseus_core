package de.uniol.inf.is.odysseus.parser.pql;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.parser.pql.impl.PredicateItem;

public class PredicateParameter extends AbstractParameter<IPredicate<?>> {

	public PredicateParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	public void setValueOf(Object object) {
		PredicateItem pItem = (PredicateItem) object;
		IPredicateBuilder pBuilder = PQLParser.getPredicateBuilder(pItem
				.getPredicateType());

		if (pBuilder == null) {
			throw new IllegalArgumentException("unkown type of predicate: "
					+ pItem.getPredicateType());
		}
		setValue(pBuilder.createPredicate(getAttributeResolver(), pItem.getPredicate()));
	}

}
