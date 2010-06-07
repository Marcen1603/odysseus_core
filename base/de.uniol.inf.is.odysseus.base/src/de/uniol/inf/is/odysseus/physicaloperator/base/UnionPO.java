package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class UnionPO<R extends IMetaAttributeContainer<?>> extends AbstractPipe<R, R> {

	protected ITransferFunction<R> transferFunction;
	
	public UnionPO(ITransferFunction<R> transferFunction) {
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
		transferFunction.newElement(object, port);
	}
	
}
