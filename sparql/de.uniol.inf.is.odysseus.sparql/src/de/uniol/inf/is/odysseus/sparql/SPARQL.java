package de.uniol.inf.is.odysseus.sparql;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
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
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.util.SimplePlanPrinter;

public class SPARQL implements IQueryParser{

	public static SPARQL instance;
	private static SPARQLParser parser;

	public static final String language = "";
	
	/**
	 * This list contains all broker names that
	 * have been detected during the last call
	 * of parse(String).
	 */
//	private ArrayList<String> brokerNames;
//
//	private User user;
//	private IDataDictionary dd;

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
	public synchronized List<IQuery> parse(String query, ISession user, IDataDictionary dd)
			throws QueryParseException {
//		this.user= user;
//		this.dd = dd;
		try{
			return parse(new StringReader(query), user, dd);
		}catch(QueryParseException e){
			System.out.println("Query: " + query);
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public synchronized List<IQuery> parse(Reader reader, ISession user, IDataDictionary dd)
			throws QueryParseException {
		
//		this.user = user;
//		this.dd = dd;
		try {
			if (parser == null) {
				parser = new SPARQLParser(reader);
			} else {
				parser.ReInit(reader);
			}
			ASTQuery queryNode = parser.CompilationUnit();
			
			SPARQLCreateLogicalPlanVisitor visitor = new SPARQLCreateLogicalPlanVisitor();
			
			visitor.setUser(user);
			visitor.setDataDictionary(dd);
			
			ILogicalOperator logicalOp = (ILogicalOperator)((LinkedList)visitor.visit(queryNode, new LinkedList())).getFirst();
				
			
			// Dump the plan
			SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
			System.out.println("Logical plan:\n" + printer.createString(logicalOp));
	
			List<IQuery> listOfQueries = new ArrayList<IQuery>();
			// an access ao must not be returned
			if(!visitor.isCreateStatement()){
				IQuery query = new Query();
				query.setParserId(getLanguage());
				query.setLogicalPlan(logicalOp, true);
				listOfQueries.add(query);
			}
			return listOfQueries;
			
		} catch (NoClassDefFoundError e) {
			throw new QueryParseException(
					"parse error: missing plugin for language feature",
					e.getCause());
		} catch (Exception e) {
			throw new QueryParseException(e);
		} catch (Throwable t){
			t.printStackTrace();
			throw new QueryParseException("error.");
		}
	}
}
