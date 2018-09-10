package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;

public interface IWindowParser {

	String parse(SimpleSource source) throws PQLOperatorBuilderException;
	
}
