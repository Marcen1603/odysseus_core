package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class UnionPO<R extends IMetaAttributeContainer<?>> extends AbstractPipe<R, R> {

	protected ITransferArea<R,R> transferFunction;
	
	public UnionPO(ITransferArea<R,R> transferFunction) {
		this.transferFunction = transferFunction;
		transferFunction.init(this);
	}

	public UnionPO(UnionPO<R> unionPO){
		this.transferFunction = unionPO.transferFunction.clone();
		transferFunction.setSourcePo(this);
	}

	@Override
	public UnionPO<R> clone(){
		return new UnionPO<R>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		transferFunction.init(this);
	}
	
	@Override
	protected synchronized void process_next(R object, int port) {
		transferFunction.transfer(object);
		transferFunction.newElement(object, port);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		transferFunction.newHeartbeat(timestamp, port);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof UnionPO)) {
			return false;
		}
		UnionPO upo = (UnionPO) ipo;
		if(this.getSubscribedToSource().equals(upo.getSubscribedToSource())) {
			// Sicherheitshalber erst einmal false
			//return true;
			return false;
		}
		return false;
	}
}
