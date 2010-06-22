package de.uniol.inf.is.odysseus.parser.pql;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImpl;

public class PQLParser implements IQueryParser {

	private PQLParserImpl parser;

	@Override
	public String getLanguage() {
		return "PQL";
	}

	@Override
	public synchronized List<IQuery> parse(String query)
			throws QueryParseException {
		return parse(new StringReader(query));
	}

	@Override
	public synchronized List<IQuery> parse(Reader reader)
			throws QueryParseException {
		try {
			if (this.parser == null) {
				try {
					this.parser = new PQLParserImpl(reader);
				} catch (Error e) {
					PQLParserImpl.ReInit(reader);
				}
			} else {
				PQLParserImpl.ReInit(reader);
			}

			List<ILogicalOperator> logicalOps = PQLParserImpl.query();
			List<IQuery> queries = new ArrayList<IQuery>();
			
			for(ILogicalOperator op : logicalOps) {
				Query query = new Query();
				query.setParserId(getLanguage());
				query.setLogicalPlan(op);
				queries.add(query);
			}
			return queries;
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}

	public static void addOperatorBuilder(String identifier,
			IOperatorBuilder builder) {
		PQLParserImpl.addOperatorBuilder(identifier, builder);
	}

	public static void removeOperatorBuilder(String identifier) {
		PQLParserImpl.removeOperatorBuilder(identifier);
	}

}
