package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;

public interface IAttributeNameParser {
	
	String parse(String attributename, String sourcename);
	String parse(String attributename);
	String parse(Attribute attribute);

}
