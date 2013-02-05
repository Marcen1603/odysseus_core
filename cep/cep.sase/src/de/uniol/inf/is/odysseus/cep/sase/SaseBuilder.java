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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.cep.epa.symboltable.relational.RelationalSymbolTableOperationFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
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
	public List<ILogicalQuery> parse(Reader reader, ISession user,
			IDataDictionary dd) throws QueryParseException {
		this.user = user;
		this.dd = dd;
		SaseLexer lex = null;
		try {
			lex = new SaseLexer(new ANTLRReaderStream(reader));
		} catch (IOException e) {
			throw new QueryParseException(e);
		}
		return processParse(lex, true, false);
	}

	@Override
	public List<ILogicalQuery> parse(String text, ISession user,
			IDataDictionary dd) throws QueryParseException {
		return parse(text, user, dd, true, false);
	}

	public List<ILogicalQuery> parse(String text, ISession user,
			IDataDictionary dd, boolean attachSources, boolean createTmpQuery)
			throws QueryParseException {
		this.user = user;
		this.dd = dd;
		SaseLexer lex = new SaseLexer(new ANTLRStringStream(text));
		return processParse(lex, attachSources, createTmpQuery);
	}

	private List<ILogicalQuery> processParse(SaseLexer lexer,
			boolean attachSources, boolean createTmpQuery)
			throws QueryParseException {
		ArrayList<ILogicalQuery> retList = new ArrayList<ILogicalQuery>();
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
			ILogicalOperator ao = walker.start(attachSources);
			if (ao != null) {
				ILogicalQuery query;
				if (createTmpQuery) {
					query = new LogicalQuery(-1);
				} else {
					query = new LogicalQuery();
				}
				query.setParserId(getLanguage());
				query.setLogicalPlan(ao, true);
				retList.add(query);
			} else {
				throw new QueryParseException(
						"Could not create logical Operator");
			}
		} catch (RecognitionException e) {
			throw new QueryParseException(e);
		}
		return retList;
	}

}
