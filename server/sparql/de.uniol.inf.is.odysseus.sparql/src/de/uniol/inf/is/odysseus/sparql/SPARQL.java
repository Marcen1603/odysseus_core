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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.SimplePlanPrinter;
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
	public synchronized List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context, IMetaAttribute metaAttribute, IServerExecutor executor)
			throws QueryParseException {
//		this.user= user;
//		this.dd = dd;
		try{
			return parse(new StringReader(query), user, dd, context, metaAttribute);
		}catch(QueryParseException e){
			System.out.println("Query: " + query);
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	private synchronized List<IExecutorCommand> parse(Reader reader, ISession user, IDataDictionary dd, Context context, IMetaAttribute metaAttribute)
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
	
			List<IExecutorCommand> listOfQueries = new ArrayList<>();
			// an access ao must not be returned
			if(!visitor.isCreateStatement()){
				ILogicalQuery query = new LogicalQuery();
				query.setParserId(getLanguage());
				query.setLogicalPlan(new LogicalPlan(logicalOp), true);
				CreateQueryCommand cmd = new CreateQueryCommand(query, user);
				listOfQueries.add(cmd);
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
	
	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		Map<String, List<String>> tokens = new HashMap<>();		
		return tokens;
	}
	
	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		return new ArrayList<>();
	}
}
