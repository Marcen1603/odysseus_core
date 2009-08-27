package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDefaultPriority;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTElementPriorities;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTElementPriority;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.PriorityAO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class CreatePriorityAOVisitor extends AbstractDefaultVisitor {

	private ILogicalOperator top;

	public CreatePriorityAOVisitor(ILogicalOperator top) {
		this.top = top;
	}

	@Override
	public Object visit(ASTElementPriorities node, Object data) {
		PriorityAO<RelationalTuple<? extends IPriority>> ao = new PriorityAO<RelationalTuple<? extends IPriority>>();
		ao.setInputAO(0, top);
		ao.setOutputSchema(top.getOutputSchema());
		ao.setInputSchema(0, top.getOutputSchema());
		node.childrenAccept(this, ao);
		top = ao;
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTElementPriority node, Object data) {
		PriorityAO<RelationalTuple<IPriority>> ao = (PriorityAO<RelationalTuple<IPriority>>) data;
		AttributeResolver tmpResolver = new AttributeResolver();
		for (SDFAttribute attribute : this.top.getOutputSchema()) {
			tmpResolver.addAttribute((CQLAttribute) attribute);
		}

		IPredicate<RelationalTuple<?>> predicate;
		predicate = CreatePredicateVisitor.toPredicate(node.getPredicate(),
				tmpResolver);
		ao.setPriority(node.getPriority(), predicate);
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTDefaultPriority node, Object data) {
		((PriorityAO<RelationalTuple>) data).setDefaultPriority(node
				.getPriority());
		return data;
	}
	
	public ILogicalOperator getTopOperator() {
		return top;
	}
}
