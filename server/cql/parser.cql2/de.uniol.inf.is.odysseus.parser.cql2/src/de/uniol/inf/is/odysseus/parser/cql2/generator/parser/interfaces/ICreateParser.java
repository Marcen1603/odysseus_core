package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Create;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StreamTo;

public interface ICreateParser {
	
	void parseCreate(Create query);
	void parseStreamTo(StreamTo query);

}
