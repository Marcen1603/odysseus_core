package de.uniol.inf.is.odysseus.sparql;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.SPARQLParser;
import de.uniol.inf.is.odysseus.sparql.parser.visitor.SPARQLCreateLogicalPlanVisitor;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class SPARQL implements IQueryParser{

	public static SPARQL instance;
	private static SPARQLParser parser;

	public static final String language = "";
	
	/**
	 * This list contains all broker names that
	 * have been detected during the last call
	 * of parse(String).
	 */
	private ArrayList<String> brokerNames;

	private User user;
	private IDataDictionary dd;

	public static synchronized IQueryParser getInstance() {
		if (instance == null) {
			instance = new SPARQL();
		}
		return instance;
	}
	
	@Override
	public String getLanguage() {
		return "SPARQL";
	}

	@Override
	public synchronized List<IQuery> parse(String query, User user, IDataDictionary dd)
			throws QueryParseException {
		this.user= user;
		this.dd = dd;
		return parse(new StringReader(query), user, dd);
	}

	@Override
	public synchronized List<IQuery> parse(Reader reader, User user, IDataDictionary dd)
			throws QueryParseException {
		this.user = user;
		this.dd = dd;
		try {
			if (parser == null) {
				parser = new SPARQLParser(reader);
			} else {
				parser.ReInit(reader);
			}
			ASTQuery queryNode = parser.Query();
			
			SPARQLCreateLogicalPlanVisitor visitor = new SPARQLCreateLogicalPlanVisitor();
			
			visitor.setUser(user);
			visitor.setDataDictionary(dd);
			ILogicalOperator logicalOp = (ILogicalOperator)visitor.visit(queryNode, null);
			
			IQuery query = new Query();
			query.setParserId(getLanguage());
			query.setLogicalPlan(logicalOp, true);
			
			List<IQuery> listOfQueries = new ArrayList<IQuery>();
			listOfQueries.add(query);
			return listOfQueries;
			
		} catch (NoClassDefFoundError e) {
			throw new QueryParseException(
					"parse error: missing plugin for language feature",
					e.getCause());
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}


}
