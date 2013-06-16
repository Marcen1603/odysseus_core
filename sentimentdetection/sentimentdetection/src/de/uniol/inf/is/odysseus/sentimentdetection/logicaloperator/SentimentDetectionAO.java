package de.uniol.inf.is.odysseus.sentimentdetection.logicaloperator;


import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;



@LogicalOperator(name="SENTIMENTDETECTION", minInputPorts=1, maxInputPorts=1)
public class SentimentDetectionAO extends UnaryLogicalOp{

	
	public SentimentDetectionAO(){
		super();
	}
	
	public SentimentDetectionAO(SentimentDetectionAO sentimentDetectionAO){
        super(sentimentDetectionAO);
    }
	

	@Override
	public AbstractLogicalOperator clone() {
	     return new SentimentDetectionAO(this);
	}

}
