package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Create;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StreamTo;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;

public interface ICreateParser {
	
	void parseCreate(Create query) throws PQLOperatorBuilderException;
	void parseStreamTo(StreamTo query) throws PQLOperatorBuilderException;

}
