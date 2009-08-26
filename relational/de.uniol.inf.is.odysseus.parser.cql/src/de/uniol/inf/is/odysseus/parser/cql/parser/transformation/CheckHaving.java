package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.HashSet;

import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFromClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWhereClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public class CheckHaving extends AbstractDefaultVisitor {
	private class CollectAggregations extends AbstractDefaultVisitor {
		@Override
		public Object visit(ASTExpression node, Object data) {
			return node.childrenAccept(this, data);
		}

		@Override
		public Object visit(ASTSimpleToken node, Object data) {
			return node.childrenAccept(this, data);
		}
		
		@Override
		public Object visit(ASTFunctionExpression node, Object data) {
			return node.childrenAccept(this, data);
		}

		@Override
		public Object visit(ASTAggregateExpression node, Object data) {
			aggregations.add(getName(node, attributeResolver));
			return data;
		}
	}

	private HashSet<String> aggregations;

	private boolean isInHaving = false;

	IAttributeResolver attributeResolver;

	public CheckHaving() {
	}

	public void init(IAttributeResolver resolver) {
		this.attributeResolver = resolver;
	}

	@Override
	public Object visit(ASTFromClause node, Object data) {
		return data;
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) {
		return data;
	}

	@Override
	public Object visit(ASTSelectClause node, Object data) {
		// collect all aggregations defined in the SELECT clause
		this.aggregations = new HashSet<String>();
		CollectAggregations collect  = new CollectAggregations();
		return collect.visit(node, data);
	}

	@Override
	public Object visit(ASTHavingClause node, Object data) {
		isInHaving = true;
		node.childrenAccept(this, data);
		isInHaving = false;
		return data;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		// only check SimpleToken nodes when they are inside
		// a HAVING clause
		if (!isInHaving) {
			return data;
		}

		Node child = node.jjtGetChild(0);
		// Aggregation has to be present in the select clause, too
		if (child instanceof ASTAggregateExpression) {
			if (!this.aggregations.contains(getName(
					(ASTAggregateExpression) child, this.attributeResolver))) {
				throw new RuntimeException(
						"invalid identifier in HAVING clause: " + child);
			}
		}
		return data;
	}
}
