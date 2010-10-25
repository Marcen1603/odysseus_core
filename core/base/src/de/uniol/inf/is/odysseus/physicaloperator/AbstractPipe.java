package de.uniol.inf.is.odysseus.physicaloperator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.event.IEventListener;
import de.uniol.inf.is.odysseus.event.IEventType;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractPipe<R, W> extends AbstractSource<W> implements
		IPipe<R, W> {

	// ------------------------------------------------------------------------
	// Delegation to simulate multiple inheritance
	// ------------------------------------------------------------------------

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
		
		@Override
		public void close(List<PhysicalSubscription<ISink<?>>> callPath) {
			callCloseOnChildren(callPath);
		}
		
		public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
			return AbstractPipe.this.delegatedIsSemanticallyEqual(ipo);
		}

	};

	final protected DelegateSink delegateSink = new DelegateSink();

	// ------------------------------------------------------------------------

	public enum OutputMode {
		NEW_ELEMENT, MODIFIED_INPUT, INPUT
	};

	abstract public OutputMode getOutputMode();

	public AbstractPipe() {
	};

	public AbstractPipe(AbstractPipe<R, W> pipe) {
		super(pipe);
		delegateSink.setInputPortCount(pipe.getInputPortCount());
	}

	@Override
	public boolean isSink() {
		return true;
	}

	protected int getInputPortCount() {
		return this.delegateSink.noInputPorts;
	}

	// Classes for Objects not implementing IClone (e.g. ByteBuffer, String,
	// etc.)
	// MUST override this method (else there will be a ClassCastException)
	@SuppressWarnings("unchecked")
	protected R cloneIfNessessary(R object, boolean exclusive, int port) {
		if (getOutputMode() == OutputMode.MODIFIED_INPUT) {
			object = (R) ((IClone) object).clone();
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

	// ------------------------------------------------------------------------
	// OPEN
	// ------------------------------------------------------------------------
	
	@Override
	public void open() throws OpenFailedException {
		this.delegateSink.open();
	}
	
	@Override
	public synchronized void open(ISink<? super W> caller, int sourcePort,
			int sinkPort,List<PhysicalSubscription<ISink<?>>> callPath) throws OpenFailedException {
		super.open(caller, sourcePort, sinkPort, callPath);
		this.delegateSink.open(callPath);
	}

	public void delegatedProcessOpen() throws OpenFailedException {
		process_open();
	}

	// ------------------------------------------------------------------------
	// PROCESS
	// ------------------------------------------------------------------------

	@Override
	protected void process_open() throws OpenFailedException {
	}

	@Override
	public void process(R object, int port, boolean exclusive) {
		this.delegateSink.process(object, port, exclusive);
	}

	private void delegatedProcess(R object, int port, boolean exclusive) {
		process_next(cloneIfNessessary(object, exclusive, port), port);
	}

	@Override
	public void process(Collection<? extends R> object, int port,
			boolean exclusive) {
		delegateSink.process(object, port, exclusive);
	}

	abstract protected void process_next(R object, int port);

	// ------------------------------------------------------------------------
	// CLOSE and DONE
	// ------------------------------------------------------------------------

	@Override
	public void close(ISink<? super W> caller, int sourcePort, int sinkPort,  List<PhysicalSubscription<ISink<?>>> callPath) {
		super.close(caller, sourcePort, sinkPort, callPath);
		this.delegateSink.close(callPath);
	}
	
	@Override
	public void close() {
		this.delegateSink.close();
	}


	@Override
	protected void process_close() {
	}

	@Override
	protected void process_done() {
	}

	protected void process_done(int port) {
	}

	@Override
	final public synchronized void done(int port) {
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

	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

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

	@Override
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
	public void subscribe(IEventListener listener, IEventType type) {
		if (type instanceof POEventType) {
			switch ((POEventType) type) {
			case ProcessInit:
			case ProcessDone:
				this.delegateSink.subscribe(listener, type);
			default:
				super.subscribe(listener, type);
			}
		} else {
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

	// ------------------------------------------------------------------------
	// Other methods
	// ------------------------------------------------------------------------

	@Override
	abstract public AbstractPipe<R, W> clone();

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}
	
	public boolean delegatedIsSemanticallyEqual(IPhysicalOperator ipo) {
		return process_isSemanticallyEqual(ipo);
	}


}
