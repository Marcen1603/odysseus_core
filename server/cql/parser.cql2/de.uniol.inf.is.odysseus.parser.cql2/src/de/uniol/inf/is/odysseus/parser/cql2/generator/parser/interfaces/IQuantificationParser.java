package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

public interface IQuantificationParser {

	String parse(ComplexPredicate predicate, SimpleSelect parent);
	
}
