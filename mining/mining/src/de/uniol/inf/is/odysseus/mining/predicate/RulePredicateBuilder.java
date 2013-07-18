package de.uniol.inf.is.odysseus.mining.predicate;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IPredicateBuilder;
import de.uniol.inf.is.odysseus.mining.MiningDatatypes;
import de.uniol.inf.is.odysseus.relational.base.predicate.ForPredicate.Type;

public class RulePredicateBuilder implements IPredicateBuilder {
	
	private Type type;

	public RulePredicateBuilder(Type type){
		this.type = type;
	}
	

	@Override
	public IPredicate<?> createPredicate(IAttributeResolver resolver, String predicate) {
		if (predicate.contains(",")) {
			String[] parts = predicate.split(",");
			String attributeName = parts[0].trim();
			predicate = parts[1].trim();
			SDFAttribute attribute = resolver.getAttribute(attributeName);
			if(!attribute.getDatatype().equals(MiningDatatypes.ASSOCIATION_RULE)){
				throw new IllegalArgumentException("Attribute "+attributeName+" for RulePredicate must be an association rule!");
			}
			
			RulePredicate pred = new RulePredicate(type, attribute, predicate);
			return pred;
		}
		
		throw new IllegalArgumentException("ForListPredicate needs an attribute!");
	}

}
