package de.uniol.inf.is.odysseus.parallelization.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

// no annotation, because it is not useful in pql
public class ObserverCounterAO extends UnaryLogicalOp implements IStatefulAO{

	private static final long serialVersionUID = -880712993171839675L;
	private Long numberOfElements;

	public ObserverCounterAO(){
        super();
    }
     
    public ObserverCounterAO(ObserverCounterAO observerCounterAO){
        super(observerCounterAO);
        this.numberOfElements = observerCounterAO.numberOfElements;
    }
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ObserverCounterAO(this);
	}

	public void setNumberOfElements(Long numberOfElements){
		this.numberOfElements = numberOfElements;
	}

	
	public Long getNumberOfElements(){
		return numberOfElements;
	}
	
}
