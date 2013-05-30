package de.uniol.inf.is.odysseus.core.server.sentimentdetection.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;


@LogicalOperator(name="SENTIMENTDETECTION", minInputPorts=1, maxInputPorts=1)
public class SentimentDetectionAO extends UnaryLogicalOp{

	
	public SentimentDetectionAO(){
		super();
	}
	
	public SentimentDetectionAO(SentimentDetectionAO sentimentDetectionAO){
        super(sentimentDetectionAO);
    }
	
	@Override
    @Parameter(type=PredicateParameter.class, isList=true)
    public void setPredicates(List<IPredicate<?>> predicates) {
        super.setPredicates(predicates);
    }
	
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
