package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.List;
import java.util.Vector;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractIterableSource<T> extends AbstractSource<T>
		implements IIterableSource<T> {

	protected List<IOperatorOwner> deactivateRequestControls = new Vector<IOperatorOwner>();

	final private POEvent activatedEvent = new POEvent(this,
			POEventType.Activated);
	final private POEvent deActivatedEvent = new POEvent(this,
			POEventType.Deactivated);
	boolean activated = true;

	public AbstractIterableSource() {

	}

	public AbstractIterableSource(AbstractIterableSource<T> source) {
		super(source);
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		super.removeOwner(owner);
		this.deactivateRequestControls.remove(owner);
		fireEvents();
	}

	private void fireEvents() {
		if (!isActive() && activated == true) {
			activated = false;
			fire(deActivatedEvent);
		} else if (isActive() && activated == false) {
			activated = true;
			fire(activatedEvent);
		}
	}

	@Override
	public void activateRequest(IOperatorOwner operatorControl) {
		this.deactivateRequestControls.remove(operatorControl);
		fireEvents();
	}

	@Override
	public void deactivateRequest(IOperatorOwner operatorControl) {
		this.deactivateRequestControls.add(operatorControl);
		fireEvents();
	}

	@Override
	public boolean deactivateRequestedBy(IOperatorOwner operatorControl) {
		boolean ret = this.deactivateRequestControls.contains(operatorControl);
		fireEvents();
		return ret;
	}

	@Override
	public synchronized boolean isActive() {
		int ownerCount = owners.size();
		int deactivateRequestCount = this.deactivateRequestControls.size();

		return ownerCount > deactivateRequestCount;
	}

}
