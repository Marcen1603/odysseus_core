package de.uniol.inf.is.odysseus.p2p_new.lb.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;

// TODO javaDoc M.B.
public class DistributionMergePO<T extends IStreamObject<? extends ITimeInterval>> 
		extends AbstractPipe<T, T> {
	
	private ITimeIntervalSweepArea<T> sweepArea;
	
	public ITimeIntervalSweepArea<T> getSweepArea() {
		
		return this.sweepArea;
		
	}

	// TODO sweepArea has to be initialized. M.B.
	public DistributionMergePO(ITimeIntervalSweepArea<T> sweepArea) {
		
		super();
		this.sweepArea = sweepArea;
		
	}

	public DistributionMergePO(DistributionMergePO<T> mergePO) {
		
		super(mergePO);
		this.sweepArea = mergePO.sweepArea.clone();
		
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		
		return new DistributionMergePO<T>(this);
		
	}

	@Override
	public OutputMode getOutputMode() {
		
		return OutputMode.INPUT;
		
	}

	@Override
	protected void process_next(T object, int port) {
		// TODO Auto-generated method stub

	}

}