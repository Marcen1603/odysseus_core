package de.uniol.inf.is.odysseus.physicaloperator.base.event;


import java.util.EventObject;

import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */

public class POEvent extends EventObject {

	private static final long serialVersionUID = 6854987972249370174L;
	private POEventType type = null;
	
	public POEvent(ISource<?> source, POEventType type) {
		super(source);
		this.type = type;
	}
	
	public POEvent(ISink<?> source, POEventType type) {
		super(source);
		this.type = type;
	}
	
	public POEvent(IPipe<?,?> source, POEventType type){
		super(source);
		this.type = type;
	}

	public String toString() {
		return this.getClass().getName();
	}

	public POEventType getPOEventType(){
		return type;
	}

}