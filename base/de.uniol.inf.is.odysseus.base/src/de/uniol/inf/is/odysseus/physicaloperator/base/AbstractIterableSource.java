package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.List;
import java.util.Vector;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractIterableSource<T> extends AbstractSource<T>
		implements IIterableSource<T> {

	protected List<IOperatorOwner> deactivateRequestControls = new Vector<IOperatorOwner>();
	
	@Override
	public void removeOwner(IOperatorOwner owner) {
		super.removeOwner(owner);
		this.deactivateRequestControls.remove(owner);
	}
	
	@Override
	public void activateRequest(IOperatorOwner operatorControl) {
			this.deactivateRequestControls.remove(operatorControl);
	}

	@Override
	public void deactivateRequest(IOperatorOwner operatorControl) {
			this.deactivateRequestControls.add(operatorControl);
	}

	@Override
	public boolean deactivateRequestedBy(IOperatorOwner operatorControl) {
			return this.deactivateRequestControls.contains(operatorControl);
	}

	@Override
	public synchronized boolean isActive() {
		int ownerCount = owners.size();
		int deactivateRequestCount = this.deactivateRequestControls.size();

		return ownerCount > deactivateRequestCount;
	}

}
