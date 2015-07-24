package de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator;


import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLPO;


public abstract class AbstractODLPO<R extends IStreamObject<?>, W extends IStreamObject<?>> extends AbstractPipe<R, W> implements IODLPO<R, W> {

	public AbstractODLPO() {
		super();
	}
	
	public AbstractODLPO(AbstractODLPO<R,W> po) {
		super(po);
	}
	
	public AbstractODLPO(AbstractODLAO ao) {

	}


	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	protected void process_open() {
		onProcessOpen();
	}
	
	@Override
	protected void process_done() {
		onProcessDone();
	}
	
	@Override
	protected void process_close() {
		onProcessClose();
	}
	
	
	protected void onProcessOpen() {

	}
	
	protected void onProcessClose() {

	}
	
	protected void onProcessDone() {

	}
	
	
	@Override
	protected void process_next(R object, int port) {
		onProcessNext(object, port);
	}
	
	@SuppressWarnings("unchecked")
	protected void onProcessNext(R object, int port) {
		transfer((W) object);
	}
	
	
	
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		onProcessPuncutation(punctuation, port);
	}
	
	protected void onProcessPuncutation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}



}
