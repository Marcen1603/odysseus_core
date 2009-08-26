package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTGroupByClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;

/**
 * @author Jonas Jacobi
 */
public class CheckAttributes extends AbstractDefaultVisitor {
	// TODO distinct expressions

	private AttributeResolver attributeResolver;

	public CheckAttributes(AttributeResolver attributeResolver) {
		this.attributeResolver = attributeResolver;
	}

	@Override
	public Object visit(ASTGroupByClause node, Object data) {
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			Node curChild = node.jjtGetChild(i);
			if (!isAttributeValid(curChild)) {
				throw new RuntimeException("invalid Attribute: " + curChild
						+ " in GROUP BY clause");
			}
		}
		return data;
	}

	@Override
	/*
	 * ignore source nodes, because they can contain subqueries and we don't
	 * want to check their attributes
	 */
	public Object visit(ASTSubselect node, Object data) {
		return data;
	}

	private boolean isAttributeValid(Node node) {
		ASTIdentifier identifier = (ASTIdentifier) node;
		return this.attributeResolver.isAttributeValid(identifier.getName());
	}

	@Override
	public Object visit(ASTHavingClause node, Object data) {
		return data;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		Node childNode = node.jjtGetChild(0);
		if (childNode instanceof ASTIdentifier && !isAttributeValid(childNode)) {
			throw new RuntimeException("invalid Attribute: " + childNode);
		}
		if (childNode instanceof ASTAggregateExpression
				&& !isAttributeValid(childNode.jjtGetChild(1))) {
			throw new RuntimeException("invalid Attribute: " + childNode);
		}
		return data;
	}

}
