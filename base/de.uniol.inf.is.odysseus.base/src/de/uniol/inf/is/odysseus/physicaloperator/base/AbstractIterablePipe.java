package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.List;
import java.util.Vector;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;

public abstract class AbstractIterablePipe<R, W> extends AbstractPipe<R, W>
		implements IIterableSource<W> {
	protected List<IOperatorOwner> deactivateRequestControls = new Vector<IOperatorOwner>();

	public AbstractIterablePipe(){};
	
	public AbstractIterablePipe(AbstractIterablePipe<R,W> pipe){
		super(pipe);
	}
	
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
	
	@Override
	public boolean isDone() {
		return super.isDone();
	}
}
