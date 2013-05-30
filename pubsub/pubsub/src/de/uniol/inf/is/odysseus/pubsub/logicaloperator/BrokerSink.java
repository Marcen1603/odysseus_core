package de.uniol.inf.is.odysseus.pubsub.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;

public class BrokerSink extends UnaryLogicalOp{
	
	public BrokerSink(){
		super();
	}
	
	public BrokerSink(BrokerSink brokerSink){
		super(brokerSink);
	}
	
	@Override
    @Parameter(type=PredicateParameter.class, isList=true)
    public void setPredicates(List<IPredicate<?>> predicates) {
        super.setPredicates(predicates);
    }
	
	@Override
	public AbstractLogicalOperator clone() {
		return new BrokerSink(this);
	}

}
