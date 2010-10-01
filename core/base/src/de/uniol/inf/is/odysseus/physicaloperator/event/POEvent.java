package de.uniol.inf.is.odysseus.physicaloperator.event;


import de.uniol.inf.is.odysseus.event.AbstractEvent;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */

public class POEvent extends AbstractEvent<IPhysicalOperator, String>{

	private static final long serialVersionUID = 6854987972249370174L;
	
	public POEvent(IPhysicalOperator source, POEventType type) {
		super(source,type,"");
	}
	
	public IPhysicalOperator getSource(){
		return getSender();
	}

	public POEventType getPOEventType(){
		return (POEventType) getEventType();
	}
}