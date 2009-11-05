/**
 * @author Marco Grawunder
 */
package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.Subscription;

public class PhysicalSubscription<K> extends Subscription<K> {

	private static final long serialVersionUID = -6266008340674321020L;
	private boolean done;
	
	public PhysicalSubscription(K target, int sinkPort, int sourcePort) {
		super(target, sinkPort, sourcePort);
		done = false;
	}
	
	public void setDone(boolean done) {
		this.done = done;
	}
	
	public boolean isDone() {
		return done;
	}


	
}