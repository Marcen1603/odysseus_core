package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.AggregateAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAS;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTGroupByClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class CreateAggregationVisitor extends AbstractDefaultVisitor {

	private AttributeResolver attributeResolver;

	private AggregateAO ao;

	private SelectAO select;

	private ILogicalOperator top;

	private boolean hasGrouping;

	public void init(ILogicalOperator top, AttributeResolver attributeResolver) {
		ao = new AggregateAO();
		this.top = top;
		ao.setInputAO(top);
		ao.setInputSchema(top.getOutputSchema());
		select = null;
		this.attributeResolver = attributeResolver;
		this.hasGrouping = false;
	}

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
	public Object visit(ASTAggregateExpression aggrNode, Object data) {
		AggregateFunction function = aggrNode.getFunction();
		String attributeName = aggrNode.getAttributeName();
		SDFAttribute attribute = this.attributeResolver
				.getAttribute(attributeName);

		CQLAttribute outAttribute = outAttribute(attribute.toString(),
				function, aggrNode);
		if (!ao.getOutputSchema().contains(outAttribute)) {
			ao.addAggregation(attribute, function, outAttribute);
		}
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		SDFAttribute attribute = this.attributeResolver.getAttribute(node
				.getName());
		if (attribute == null) {
			throw new IllegalArgumentException("no such attribute: "
					+ node.getName());
		}
		ao.addGroupingAttribute(attribute);
		return null;
	}

	@Override
	public Object visit(ASTAS node, Object data) {
		return data;
	}

	@Override
	public Object visit(ASTSelectClause node, Object data) {
		node.childrenAccept(this, data);
		return ao;
	}

	private CQLAttribute outAttribute(String attributeName,
			AggregateFunction function, ASTAggregateExpression node) {
		String funcName = function.toString() + "(" + attributeName + ")";
		CQLAttribute attribute = this.attributeResolver.getAttribute(funcName);
		if (attribute == null) {
			SDFDatatype datatype = this.attributeResolver.getAttribute(
					attributeName).getDatatype();
			if (!isNumerical(datatype) && function != AggregateFunction.COUNT) {
				throw new IllegalArgumentException("function '"
						+ function.toString()
						+ "' can't be used on non scalar types");
			}
			attribute = new CQLAttribute(null, funcName);
			switch (function) {
			case AVG:
				attribute.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			case COUNT:
				attribute
						.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				break;
			default:
				// datatype equals datatype of input attribute
				// for other functions
				attribute.setDatatype(datatype);
			}

			this.attributeResolver.addAttribute(attribute);
		}
		return attribute;
	}

	private boolean isNumerical(SDFDatatype datatype) {
		// TODO oder sollte der check ueber die dtconstraints laufen?
		return datatype == SDFDatatypeFactory.getDatatype("Double")
				|| datatype == SDFDatatypeFactory.getDatatype("Integer")
				|| datatype == SDFDatatypeFactory.getDatatype("Long");
	}

	@Override
	public Object visit(ASTGroupByClause node, Object data) {
		this.hasGrouping = true;
		return data;
	}

	@Override
	public Object visit(ASTSimpleSource node, Object data) {
		return data;
	}

	@Override
	public Object visit(ASTSubselect node, Object data) {
		return data;
	}

	@Override
	public Object visit(ASTHavingClause node, Object data) {
		select = new SelectAO();
		select.setInputAO(ao);
		IPredicate<RelationalTuple<?>> predicate;
		predicate = CreatePredicateVisitor.toPredicate((ASTPredicate) node
				.jjtGetChild(0), this.attributeResolver);
		select.setPredicate(predicate);
		select.setOutputSchema(ao.getOutputSchema());

		return select;
	}

	public ILogicalOperator getResult() {
		if (this.select != null) {
			return this.select;
		}
		return this.ao.hasAggregations() || hasGrouping ? this.ao : top;
	}
}
