package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTGroupByClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWhereClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class CheckGroupBy extends AbstractDefaultVisitor {
	private boolean hasAggregates;

	private Set<SDFAttribute> checkGroupingAttributes;

	private AttributeResolver attributeResolver;

	private boolean hasGrouping;

	@Override
	public Object visit(ASTSelectClause node, Object data) {
		hasAggregates = false;
		hasGrouping = false;
		this.checkGroupingAttributes = new HashSet<SDFAttribute>();
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			node.jjtGetChild(i).jjtAccept(this, data);
		}

		return data;
	}

	@Override
	public Object visit(ASTAggregateExpression node, Object data) {
		hasAggregates = true;
		return data;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		Node child = node.jjtGetChild(0);
		if (child instanceof ASTIdentifier) {
			checkGroupingAttributes.add(this.attributeResolver
					.getAttribute(child.toString()));
		} else {
			child.jjtAccept(this, data);
		}
		return data;
	}

	@Override
	public Object visit(ASTGroupByClause node, Object data) {
		this.hasGrouping = true;
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			String curIdentifier = ((ASTIdentifier) node.jjtGetChild(i))
					.getName();
			SDFAttribute attribute = this.attributeResolver
					.getAttribute(curIdentifier);

			checkGroupingAttributes.remove(attribute);
		}
		return data;
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) {
		return data;
	}

	@Override
	public Object visit(ASTSubselect node, Object data) {
		return data;
	}

	public boolean checkOkay() {
		return checkGroupingAttributes.isEmpty()
				|| (!hasAggregates && !hasGrouping);
	}

	public void init(AttributeResolver attributeResolver) {
		this.attributeResolver = attributeResolver;
	}
}
