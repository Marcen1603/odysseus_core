package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IEventListener;
import de.uniol.inf.is.odysseus.base.IEventType;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractPipe<R, W> extends AbstractSource<W> implements
		IPipe<R, W> {
	protected class DelegateSink extends AbstractSink<R> {

		@Override
		protected void process_next(R object, int port, boolean exclusive) {
			AbstractPipe.this.delegatedProcess(object, port, exclusive);
		}

		@Override
		protected void process_open() throws OpenFailedException {
			AbstractPipe.this.delegatedProcessOpen();
		}

		@Override
		protected void setInputPortCount(int ports) {
			super.setInputPortCount(ports);
		}

		@Override
		final protected ISink<R> getInstance() {
			return AbstractPipe.this;
		}

		@Override
		public AbstractSink<R> clone() {
			throw new RuntimeException("Clone Not Supported");
		}

		@Override
		public void processPunctuation(PointInTime timestamp, int port) {
		}

		public void open(ISink abstractPipe) throws OpenFailedException {
			super.open(abstractPipe);
		}

		public void close(ISink abstractPipe) {
			super.close(abstractPipe);
		}

	};

	final protected DelegateSink delegateSink = new DelegateSink();

	public enum OutputMode {
		NEW_ELEMENT, MODIFIED_INPUT, INPUT
	};

	abstract protected void process_next(R object, int port);

	// private boolean[] inputExclusive;

	public void delegatedProcessOpen() throws OpenFailedException {
		process_open();
	}

	public AbstractPipe() {
	};

	public AbstractPipe(AbstractPipe<R, W> pipe) {
		super(pipe);
	}

	@Override
	public boolean isSink() {
		return true;
	}

	abstract public OutputMode getOutputMode();

	@Override
	public void close(IPhysicalOperator o, int sourcePort) {
		// process_close();
		close();
		super.close(o, sourcePort);
	};

	public void close() {
		process_close();
		this.delegateSink.close(this);
	}

	protected int getInputPortCount() {
		return this.delegateSink.noInputPorts;
	}

	// Classes for Objects not implementing IClone (e.g. ByteBuffer, String,
	// etc.)
	// MUST override this method (else there will be a ClassCastException)
	@SuppressWarnings("unchecked")
	protected R cloneIfNessessary(R object, boolean exclusive, int port) {
		// boolean exclusive??
		if (getOutputMode() == OutputMode.MODIFIED_INPUT) {
			object = (R) ((IClone) object).clone();
			// setInputExclusive(true, port);
		}
		return object;
	}

	@Override
	public boolean isTransferExclusive() {
		// Zunächst Testen ob das Datum an mehrere Empfänger
		// versendet wird --> dann niemals exclusiv
		boolean ret = super.isTransferExclusive();
		OutputMode out = getOutputMode();
		switch (out) {
		case NEW_ELEMENT:
			return ret;
		default: // MODIFIED_INPUT und INPUT
			// Wenn einer der Eingänge nicht exclusive ist
			// das Ergebnis auch nicht exclusive
			// for (int i = 0; i < inputExclusive.length && ret; i++) {
			// ret = ret && inputExclusive[i];
			// }
			return false;
		}
	}

	@Override
	public void process(R object, int port, boolean exclusive) {
		// setInputExclusive(exclusive, port);
		this.delegateSink.process(object, port, exclusive);
	}

	private void delegatedProcess(R object, int port, boolean exclusive) {
		process_next(cloneIfNessessary(object, exclusive, port), port);
	}

	// private void setInputExclusive(boolean exclusive, int port) {
	// this.inputExclusive[port] = exclusive;
	// }
	//
	// private boolean isInputExclusive(int port) {
	// return inputExclusive[port];
	// }

	@Override
	public void process(Collection<? extends R> object, int port,
			boolean exclusive) {
		delegateSink.process(object, port, exclusive);
	}

	@Override
	final synchronized public void open(IPhysicalOperator sink, int sourcePort)
			throws OpenFailedException {
		super.open(sink, sourcePort);
		this.delegateSink.open(this);
	}

	public void open() throws OpenFailedException {
		this.delegateSink.open(this);
	}

	@Override
	protected void process_open() throws OpenFailedException {
	}

	@Override
	protected void process_done() {
	}

	protected void process_done(int port) {
	}

	@Override
	protected void process_close() {
	}

	@Override
	final public synchronized void done(int port) {
		// if (logger.isDebugEnabled()) {
		// logger.debug(this + "(" + hashCode() + ") done on port " + port
		// + " called");
		// }
		process_done(port);
		this.delegateSink.done(port);
		if (isDone()) {
			propagateDone();
		}
	}

	/**
	 * Every ISink can have additional conditions, if it is done (e.g. in a
	 * buffer every item must be processed) by overriding this Method these
	 * conditions can be set If this method is overridden, the overrider is
	 * responsible for calling done(propagateDone) again!
	 * 
	 * @return true if done can be propagated (Default is true)
	 */
	protected boolean isDone() {
		return this.delegateSink.isDone();
	}

	@Override
	public void subscribeToSource(ISource<? extends R> source, int sinkInPort,
			int sourceOutPort, SDFAttributeList schema) {
		this.delegateSink.subscribeToSource(source, sinkInPort, sourceOutPort,
				schema);
	}

	@Override
	public void unsubscribeFromSource(
			PhysicalSubscription<ISource<? extends R>> subscription) {
		this.delegateSink.unsubscribeFromSource(subscription);

	}

	public PhysicalSubscription<ISource<? extends R>> getSubscribedToSource(
			int port) {
		return this.delegateSink.getSubscribedToSource(port);
	}

	@Override
	final public List<PhysicalSubscription<ISource<? extends R>>> getSubscribedToSource() {
		return Collections.unmodifiableList(delegateSink
				.getSubscribedToSource());
	}

	@Override
	public void unsubscribeFromSource(ISource<? extends R> source,
			int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		this.delegateSink.unsubscribeFromSource(source, sinkInPort,
				sourceOutPort, schema);
	}

	@Override
	public void unsubscribeFromAllSources() {
		this.delegateSink.unsubscribeFromAllSources();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}

	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		if (type instanceof POEventType) {
			switch ((POEventType) type) {
			case ProcessInit:
			case ProcessDone:
				this.delegateSink.subscribe(listener, type);
			default:
				super.subscribe(listener, type);
			}
		}else{
			super.subscribe(listener, type);
		}
	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		super.subscribeToAll(listener);
		this.delegateSink.subscribe(listener, POEventType.ProcessInit);
		this.delegateSink.subscribe(listener, POEventType.ProcessDone);
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		super.unsubscribe(listener, type);
		this.delegateSink.unsubscribe(listener, type);
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		super.unSubscribeFromAll(listener);
		this.delegateSink.unSubscribeFromAll(listener);
	}

	@Override
	abstract public AbstractPipe<R, W> clone();
}
