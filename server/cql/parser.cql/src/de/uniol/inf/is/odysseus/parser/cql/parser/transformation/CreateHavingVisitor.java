package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPredicate;

public class CreateHavingVisitor extends AbstractDefaultVisitor {

	private IAttributeResolver attributeResolver;
	private ILogicalOperator top;

	public CreateHavingVisitor(ILogicalOperator top, IAttributeResolver havingAR) {
		this.top = top;
		this.attributeResolver = havingAR;
	}
	
	@Override
	public Object visit(ASTHavingClause node, Object data) throws QueryParseException {
		SelectAO select = new SelectAO();
		select.subscribeToSource(top, 0, 0, top.getOutputSchema());
		IPredicate<Tuple<?>> predicate;
		predicate = CreatePredicateVisitor.toPredicate((ASTPredicate) node.jjtGetChild(0), this.attributeResolver);
		select.setPredicate(predicate);
		top = select;
		return select;		
	}

	public ILogicalOperator getTop() {
		return top;
	}

}
