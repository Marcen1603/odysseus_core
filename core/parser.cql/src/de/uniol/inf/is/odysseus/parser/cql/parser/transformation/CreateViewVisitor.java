package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.List;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateViewStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriorizedStatement;

public class CreateViewVisitor extends AbstractDefaultVisitor {

	private User user;

	public CreateViewVisitor(User user) {
		this.user = user;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTCreateViewStatement node, Object data) {
		ASTIdentifier nameNode = (ASTIdentifier) node.jjtGetChild(0);
		String viewName = nameNode.getName();
		
		CQLParser parser = new CQLParser();
		ILogicalOperator operator = ((List<IQuery>) parser.visit((ASTPriorizedStatement) node.jjtGetChild(1), null)).get(0).getLogicalPlan();
		
		if (DataDictionary.getInstance().containsView(viewName, user)) {
			throw new RuntimeException("ambigious name of view: " + viewName);
		}
		DataDictionary.getInstance().addSourceType(viewName, "RelationalStreaming");
		DataDictionary.getInstance().setLogicalView(viewName, operator, user);
		
		return null;
	}
}
