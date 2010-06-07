package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class UnionPO<R extends IMetaAttributeContainer<?>> extends AbstractPipe<R, R> {

	protected ITransferFunction<R> transferFunction;
	
	public UnionPO(ITransferFunction<R> transferFunction) {
		this.transferFunction = transferFunction;
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
	protected synchronized void process_next(R object, int port) {
		transferFunction.newElement(object, port);
	}
	
}
