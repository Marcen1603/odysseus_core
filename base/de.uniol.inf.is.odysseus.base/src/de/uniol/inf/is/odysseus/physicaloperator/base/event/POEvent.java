package de.uniol.inf.is.odysseus.physicaloperator.base.event;


import de.uniol.inf.is.odysseus.base.IPhysicalOperator;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */

public class POEvent {

	private static final long serialVersionUID = 6854987972249370174L;
	final private POEventType type;
	private IPhysicalOperator source;
	
	public POEvent(IPhysicalOperator source, POEventType type) {
		this.source = source;
		this.type = type;
	}
	
	public IPhysicalOperator getSource(){
		return source;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}

	public POEventType getPOEventType(){
		return type;
	}

}