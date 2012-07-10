/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.sparql;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.SPARQLParser;
import de.uniol.inf.is.odysseus.sparql.parser.visitor.SPARQLCreateLogicalPlanVisitor;

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
	public synchronized List<ILogicalQuery> parse(String query, ISession user, IDataDictionary dd)
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
	public synchronized List<ILogicalQuery> parse(Reader reader, ISession user, IDataDictionary dd)
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
	
			List<ILogicalQuery> listOfQueries = new ArrayList<ILogicalQuery>();
			// an access ao must not be returned
			if(!visitor.isCreateStatement()){
				ILogicalQuery query = new LogicalQuery();
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
