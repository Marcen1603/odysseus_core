package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateViewStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriorizedStatement;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class CreateViewVisitor extends AbstractDefaultVisitor {

	private User caller;
	private IDataDictionary dd;

	public CreateViewVisitor(User user, IDataDictionary dd) {
		this.caller = user;
		this.dd = dd;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTCreateViewStatement node, Object data) {
		ASTIdentifier nameNode = (ASTIdentifier) node.jjtGetChild(0);
		String viewName = nameNode.getName();
		
		CQLParser parser = new CQLParser();
		parser.setUser(caller);
		parser.setDataDictionary(dd);
		ILogicalOperator operator = ((List<IQuery>) parser.visit((ASTPriorizedStatement) node.jjtGetChild(1), null)).get(0).getLogicalPlan();
		
		if (dd.containsViewOrStream(viewName, caller)) {
			throw new RuntimeException("ambigious name of view: " + viewName);
		}
		dd.addSourceType(viewName, "RelationalStreaming");
		dd.setView(viewName, operator, caller);
		
		return null;
	}
}
