package de.uniol.inf.is.odysseus.parser.pql;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.parser.pql.impl.PredicateItem;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

@SuppressWarnings("unchecked")
public class PredicateParameter extends AbstractParameter<IPredicate> {

	private IAttributeResolver resolver;

	public PredicateParameter(String name, REQUIREMENT requirement) {
		super(name, IPredicate.class, requirement);
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
		setValue(pBuilder.createPredicate(resolver, pItem.getPredicate()));
	}

	public void setAttributeResolver(IAttributeResolver resolver) {
		this.resolver = resolver;
	}

}
