package de.uniol.inf.is.odysseus.parser.pql.relational;

import java.io.StringReader;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.parser.pql.IPredicateBuilder;
import de.uniol.inf.is.odysseus.parser.pql.relational.predicateParser.ParseException;
import de.uniol.inf.is.odysseus.parser.pql.relational.predicateParser.RelationalPredicateParser;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public class RelationalPredicateBuilder implements IPredicateBuilder {

	@Override
	public IPredicate<?> createPredicate(IAttributeResolver resolver,
			String predicate) {
		RelationalPredicateParser parser = new RelationalPredicateParser(
				new StringReader(predicate));

		parser.setAttributeResolver(resolver);
		try {
			return parser.Predicate();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
