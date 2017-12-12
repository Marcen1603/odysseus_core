package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;

public interface IAggregationParser {
	
	Object[] parse(Collection<SelectExpression> list, List<Attribute> list2, List<Source> srcs);

}
