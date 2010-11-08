package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateViewStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriorizedStatement;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class CreateViewVisitor extends AbstractDefaultVisitor {

	private User caller;

	public CreateViewVisitor(User user) {
		this.caller = user;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTCreateViewStatement node, Object data) {
		ASTIdentifier nameNode = (ASTIdentifier) node.jjtGetChild(0);
		String viewName = nameNode.getName();
		
		CQLParser parser = new CQLParser();
		parser.setUser(caller);
		ILogicalOperator operator = ((List<IQuery>) parser.visit((ASTPriorizedStatement) node.jjtGetChild(1), null)).get(0).getLogicalPlan();
		
		if (DataDictionary.getInstance().containsView(viewName, caller)) {
			throw new RuntimeException("ambigious name of view: " + viewName);
		}
		DataDictionary.getInstance().addSourceType(viewName, "RelationalStreaming", caller);
		DataDictionary.getInstance().setLogicalView(viewName, operator, caller);
		
		return null;
	}
}
