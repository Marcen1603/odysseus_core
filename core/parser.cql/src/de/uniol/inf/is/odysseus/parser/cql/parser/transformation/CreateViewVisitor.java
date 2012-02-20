/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateViewStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriorizedStatement;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

public class CreateViewVisitor extends AbstractDefaultVisitor {

	private ISession caller;
	private IDataDictionary dd;

	public CreateViewVisitor(ISession user, IDataDictionary dd) {
		this.caller = user;
		this.dd = dd;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTCreateViewStatement node, Object data) throws QueryParseException {
		ASTIdentifier nameNode = (ASTIdentifier) node.jjtGetChild(0);
		String viewName = nameNode.getName();
		
		CQLParser parser = new CQLParser();
		parser.setUser(caller);
		parser.setDataDictionary(dd);
		ILogicalOperator operator = ((List<ILogicalQuery>) parser.visit((ASTPriorizedStatement) node.jjtGetChild(1), null)).get(0).getLogicalPlan();
		
		if (dd.containsViewOrStream(viewName, caller)) {
			throw new RuntimeException("ambigious name of view: " + viewName);
		}
		dd.addSourceType(viewName, "RelationalStreaming");
		try {
			dd.setView(viewName, operator, caller);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}
		
		return null;
	}
}
