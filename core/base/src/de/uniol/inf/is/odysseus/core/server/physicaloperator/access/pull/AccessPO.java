package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IToObjectInputStreamTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IToStringArrayTransformer;

/**
 * This class represents all sources that need to be scheduled to deliver 
 * input (pull). For all sources that push their data use ReceivePO
 * 
 * @author Marco Grawunder
 *
 * @param <R> The immediate values that are send to 
 * @param <W> The Output that is written by this operator.
 */
public class AccessPO<R, W> extends AbstractIterableSource<W> {

	private static final Logger LOG = LoggerFactory
			.getLogger(AccessPO.class);

	private boolean isDone = false;
	
	private IInputHandler<R> input;

	final private IDataHandler<W> dataHandler;
	
	// Use different kinds of transformer to transform to
	// the format the IDataHandler can read
	final private IToStringArrayTransformer<R> stringTransformer;
	final private IToObjectInputStreamTransformer<R> oisTransformer;

	/**
	 * 
	 * @param input
	 * @param transformer
	 * @param dataHandler
	 */
	public AccessPO(IInputHandler<R> input, IToStringArrayTransformer<R> transformer,
			IDataHandler<W> dataHandler) {
		this.input = input;
		this.stringTransformer = transformer;
		this.dataHandler = dataHandler;
		this.oisTransformer = null;
	}

	public AccessPO(IInputHandler<R> input,IToObjectInputStreamTransformer<R> transformer, IDataHandler<W> dataHandler) {
		this.input = input;
		this.stringTransformer = null;
		this.dataHandler = dataHandler;
		this.oisTransformer = transformer;
	}

	@Override
	public synchronized boolean hasNext() {
		if (isDone || !isOpen()) {
			return false;
		}
		try {
			return input.hasNext();
		} catch (Exception e) {
			LOG.error("Exception during input", e);
		}

		tryPropagateDone();
		return false;
	}

	private void tryPropagateDone() {
		try {
			propagateDone();
		} catch (Throwable throwable) {
			LOG.error("Exception during propagating done", throwable);
		}
	}

	@Override
	public synchronized void transferNext() {
		if (isOpen() && !isDone()) {
			R object = null;
			W toTransfer = null;
			try {
				object = input.getNext();				
				if (object != null) {
					if (stringTransformer != null){		
						String[] data = stringTransformer.transform(object);
						toTransfer = dataHandler.readData(data);
						
					}else if (oisTransformer != null){
						toTransfer =  dataHandler.readData(oisTransformer.transform(object));
					}
				} else {
					isDone = true;
					propagateDone();
				}
			} catch (Exception e) {
				LOG.error("Cannot not transform object " +object, e);
			}
			if (toTransfer != null){
				transfer(toTransfer);
			}else{
				LOG.warn("Got empty object to transfer");
			}
		}
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		if (!isOpen()) {
			input.init();
			if (dataHandler.isPrototype()){
				throw new IllegalArgumentException("Data handler is not configured correctly!");
			}
		}
	}

	@Override
	protected synchronized void process_close() {
		if (isOpen()) {
			input.terminate();
		}
	}

	@Override
	public AbstractSource<W> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof AccessPO)) {
			return false;
		}
		// TODO: Check for Equality
		return false;
	}

}
