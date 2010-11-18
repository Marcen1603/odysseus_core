package de.uniol.inf.is.odysseus.physicaloperator;

import java.util.List;
import java.util.Vector;

import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;

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
	public boolean isDone() {
		return super.isDone();
	}
}
