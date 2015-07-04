package de.uniol.inf.is.odysseus.parallelization.logicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

// no annotation, because it is not useful in pql
public class ObserverCounterAO extends UnaryLogicalOp implements IStatefulAO{

	private static final long serialVersionUID = -880712993171839675L;
	private Integer numberOfElements;
	private List<Observer> observers;

	public ObserverCounterAO(){
        super();
        this.observers = new ArrayList<Observer>();
    }
     
    public ObserverCounterAO(ObserverCounterAO observerCounterAO){
        super(observerCounterAO);
        this.numberOfElements = observerCounterAO.numberOfElements;
        this.observers = new ArrayList<Observer>(observerCounterAO.observers);
    }
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ObserverCounterAO(this);
	}

	public void setNumberOfElements(Integer numberOfElements){
		this.numberOfElements = numberOfElements;
	}
	
	public void addObserver(Observer observer){
		this.observers.add(observer);
	}
	
}
