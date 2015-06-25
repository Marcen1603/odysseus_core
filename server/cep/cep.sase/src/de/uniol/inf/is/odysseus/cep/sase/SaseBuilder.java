/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.cep.sase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.cep.epa.symboltable.relational.RelationalSymbolTableOperationFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class SaseBuilder implements IQueryParser, BundleActivator {

	private ISession user;
	private IDataDictionary dd;

	@Override
	public String getLanguage() {
		return "SASE_Relational";
	}

	@Override
	public void start(BundleContext arg0) throws Exception {

	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
	}

	public void printTree(CommonTree t, int indent) {
		if (t != null) {
			StringBuffer sb = new StringBuffer(indent);
			for (int i = 0; i < indent; i++)
				sb = sb.append("   ");
			for (int i = 0; i < t.getChildCount(); i++) {
				System.out.println(sb.toString() + t.getChild(i).toString());
				printTree((CommonTree) t.getChild(i), indent + 1);
			}
		}
	}

	@Override
	public List<IExecutorCommand> parse(String text, ISession user,
			IDataDictionary dd, Context context, IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException {
		return parse(text, user, dd, true, false);
	}

	public List<IExecutorCommand> parse(String text, ISession user,
			IDataDictionary dd, boolean attachSources, boolean createTmpQuery)
			throws QueryParseException {
		this.user = user;
		this.dd = dd;
		SaseLexer lex = new SaseLexer(new ANTLRStringStream(text));
		return processParse(lex, attachSources, createTmpQuery);
	}

	private List<IExecutorCommand> processParse(SaseLexer lexer,
			boolean attachSources, boolean createTmpQuery)
			throws QueryParseException {
		ArrayList<IExecutorCommand> retList = new ArrayList<>();
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SaseParser parser = new SaseParser(tokens);
		SaseParser.start_return ret;
		try {
			ret = parser.start();
		} catch (RecognitionException e) {
			throw new QueryParseException(e);
		}
		CommonTree tree = (CommonTree) ret.getTree();
		// printTree(tree, 2);
		CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
		SaseAST walker = new SaseAST(nodes);
		walker.setUser(user);
		walker.setDataDictionary(dd);

		// Relational ... ggf. auslagern ?
		walker.symTableOpFac = new RelationalSymbolTableOperationFactory();
		CepVariable.setSymbolTableOperationFactory(walker.symTableOpFac);

		try {
			ILogicalQuery query;
			if (createTmpQuery) {
				query = new LogicalQuery(-1);
			} else {
				query = new LogicalQuery();
			}
			query.setParserId(getLanguage());

			IExecutorCommand cmd = walker.start(attachSources, query);
			retList.add(cmd);
			
			//			if (ao != null) {
//				retList.add(query);
//			} else {
//				throw new QueryParseException(
//						"Could not create logical Operator");
//			}
		} catch (RecognitionException e) {
			throw new QueryParseException(e);
		}
		return retList;
	}
	
	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		Map<String, List<String>> tokens = new HashMap<>();		
		List<String> staticTokens = Arrays.asList(SaseParser.tokenNames);
		tokens.put("TOKEN", staticTokens);
		return tokens;
	}
	
	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		return new ArrayList<>();
	}

}
