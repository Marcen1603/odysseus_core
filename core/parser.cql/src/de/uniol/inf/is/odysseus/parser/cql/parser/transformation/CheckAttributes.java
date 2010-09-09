package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDBSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTGroupByClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRenamedExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectAll;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

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
				throw new RuntimeException("invalid Attribute: " + curChild + " in GROUP BY clause");
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
		if (childNode instanceof ASTAggregateExpression && !isAttributeValid(childNode.jjtGetChild(1))) {
			throw new RuntimeException("invalid Attribute: " + childNode);
		}
		return data;
	}

	@Override
	public Object visit(ASTDBSelectStatement node, Object data) {

		// wie Subselect ignorieren
		return data;
	}

	@Override
	public Object visit(ASTSelectClause node, Object data) {
		if (!(node.jjtGetChild(0) instanceof ASTSelectAll)) {
			for (int i = 0; i < node.jjtGetNumChildren(); i++) {
				// selectClause -> renamedExpression ?
				if(!(node.jjtGetChild(i) instanceof ASTRenamedExpression)){
					continue;
				}
				// selectClause -> renamedExpression -> expression -> simpleToken ?
				Node simpleToken = node.jjtGetChild(i).jjtGetChild(0).jjtGetChild(0);
				if(!(simpleToken instanceof ASTSimpleToken)){
					continue;
				}
				Node childNode = simpleToken.jjtGetChild(0);
				if (childNode instanceof ASTIdentifier) {
					String identifier = ((ASTIdentifier) childNode).getName();
					String[] part = identifier.split("\\.");
					if (part.length == 2 && part[1].equals("*")) {
						ILogicalOperator source = this.attributeResolver.getSource(part[0]);
						if (source == null) {
							throw new RuntimeException("invalid Attribute: " + childNode);
						} else {

							for (int id = 0; id < source.getOutputSchema().size(); id++) {
								SDFAttribute attribute = source.getOutputSchema().getAttribute(id);
								ASTIdentifier ident = new ASTIdentifier(0);
								ident.setName(attribute.getSourceName() + "." + attribute.getAttributeName());
								ASTSimpleToken token = new ASTSimpleToken(0);
								ident.jjtSetParent(token);
								token.jjtAddChild(ident, 0);

								ASTExpression expression = new ASTExpression(0);
								expression.jjtAddChild(token, 0);
								token.jjtSetParent(expression);

								ASTRenamedExpression renExp = new ASTRenamedExpression(id);
								renExp.jjtAddChild(expression, 0);
								expression.jjtSetParent(renExp);

								node.jjtAddChild(renExp, id);
								renExp.jjtSetParent(node);
							}
							return data;
						}
					}

				}
			}
		}
		return super.visit(node, data);
	}

}
