/**
 * @author Marco Grawunder
 */
package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.Subscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PhysicalSubscription<K> extends Subscription<K> {

	private static final long serialVersionUID = -6266008340674321020L;
	private boolean done;
	private int openCalls = 0;
	
	public PhysicalSubscription(K target, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		super(target, sinkInPort, sourceOutPort, schema);
		done = false;
	}
	
	public void setDone(boolean done) {
		this.done = done;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public synchronized void incOpenCalls(){
		openCalls++;
	}

	public synchronized void decOpenCalls(){
		openCalls--;
	}
	
	public synchronized int getOpenCalls(){
		return openCalls;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	
}