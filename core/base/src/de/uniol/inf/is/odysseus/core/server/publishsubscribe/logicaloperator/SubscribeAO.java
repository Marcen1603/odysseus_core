package de.uniol.inf.is.odysseus.core.server.publishsubscribe.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;

@LogicalOperator(name="SUBSCRIBE", minInputPorts=1, maxInputPorts=1, doc="Subscribe Operator")
public class SubscribeAO extends UnaryLogicalOp{

	public SubscribeAO(){
		super();
	}
	
	public SubscribeAO(SubscribeAO subscribeAO){
		super(subscribeAO);
	}
	
	@Override
    @Parameter(type=PredicateParameter.class, isList=true)
    public void setPredicates(List<IPredicate<?>> predicates) {
        super.setPredicates(predicates);
    }
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SubscribeAO(this);
	}

}
