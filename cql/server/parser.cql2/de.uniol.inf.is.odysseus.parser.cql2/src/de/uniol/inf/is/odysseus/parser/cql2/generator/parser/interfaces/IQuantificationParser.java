package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;

public interface IQuantificationParser {

	String parse(ComplexPredicate predicate, SimpleSelect parent) throws PQLOperatorBuilderException;
	
}
