package de.uniol.inf.is.odysseus.parser.pql;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImpl;

public class PQLParser implements IQueryParser {

	private PQLParserImpl parser;

	@Override
	public String getLanguage() {
		return "PQL";
	}

	@Override
	public synchronized List<ILogicalOperator> parse(String query)
			throws QueryParseException {
		return parse(new StringReader(query));
	}

	@Override
	public synchronized List<ILogicalOperator> parse(Reader reader)
			throws QueryParseException {
		try {
			if (parser == null) {
				parser = new PQLParserImpl(reader);
			} else {
				PQLParserImpl.ReInit(reader);
			}

			return PQLParserImpl.query();
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}
	
	 public static void addOperatorBuilder(String identifier, IOperatorBuilder builder) {
		 PQLParserImpl.addOperatorBuilder(identifier, builder);
	 }
	 
	 public static void removeOperatorBuilder(String identifier) {
		 PQLParserImpl.removeOperatorBuilder(identifier);
	 }

}
