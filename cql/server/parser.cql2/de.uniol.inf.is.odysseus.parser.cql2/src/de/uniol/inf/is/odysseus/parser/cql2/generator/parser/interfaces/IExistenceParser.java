package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import java.util.List;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;

public interface IExistenceParser {

	List<String> parse(ComplexPredicate predicate, SimpleSelect parent, List<String> predicateString,boolean negated) throws PQLOperatorBuilderException;
	
}
