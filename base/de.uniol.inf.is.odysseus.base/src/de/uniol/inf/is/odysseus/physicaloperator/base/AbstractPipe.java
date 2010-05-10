package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.CloseFailedException;
import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractPipe<R, W> extends AbstractSource<W> implements
		IPipe<R, W> {
	protected class DelegateSink extends AbstractSink<R> {

		@Override
		public void unsubscribeFromSource(
				PhysicalSubscription<ISource<? extends R>> subscription) {
			synchronized (this.subscribedTo) {
				if (this.subscribedTo.remove(subscription)) {
					subscription.getTarget().unsubscribeSink(
							this.getInstance(), subscription.getSinkInPort(),
							subscription.getSourceOutPort(),
							subscription.getSchema());
				}
			}
		}

		@Override
		public void unsubscribeFromAllSources() {
			synchronized (this.subscribedTo) {
				Iterator<PhysicalSubscription<ISource<? extends R>>> it = this.subscribedTo
						.iterator();
				while (it.hasNext()) {
					PhysicalSubscription<ISource<? extends R>> subscription = it
							.next();
					it.remove();
					subscription.getTarget().unsubscribeSink(this.getInstance(),
							subscription.getSinkInPort(),
							subscription.getSourceOutPort(),
							subscription.getSchema());
				}
			}
		}

		@Override
		protected void process_next(R object, int port, boolean exclusive) {
			AbstractPipe.this.delegatedProcess(object, port, exclusive);
		}

		@Override
		protected void setInputPortCount(int ports) {
			super.setInputPortCount(ports);
			// inputExclusive = new boolean[ports];
			// Arrays.fill(inputExclusive, false);
		}

		@Override
		final protected ISink<R> getInstance() {
			return AbstractPipe.this;
		}

		@Override
		public AbstractSink<R> clone() throws CloneNotSupportedException {
			throw new CloneNotSupportedException();
		}

	};

	final protected DelegateSink delegateSink = new DelegateSink();

	public enum OutputMode {
		NEW_ELEMENT, MODIFIED_INPUT, INPUT
	};

	// private static final Logger logger = LoggerFactory
	// .getLogger(AbstractPipe.class);

	abstract protected void process_next(R object, int port);

	// private boolean[] inputExclusive;

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
	public void close() {
		try {
			process_close();
		} catch (CloseFailedException e) {
			e.printStackTrace();
		}
		this.delegateSink.close();
		super.close();
	};

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
	final synchronized public void open() throws OpenFailedException {
		super.open();
		delegateSink.open();
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
	protected void process_close() throws CloseFailedException {
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
		// TODO vernuenftig synchronosieren
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
	public void processPunctuation(PointInTime timestamp, int port) {
		this.delegateSink.processPunctuation(timestamp, port);
	}

	@Override
	public void subscribe(POEventListener listener, POEventType type) {
		super.subscribe(listener, type);
		switch (type) {
		case ProcessInit:
		case ProcessDone:
			this.delegateSink.subscribe(listener, type);
		default:
			super.subscribe(listener, type);
		}
	}

	@Override
	public void subscribeToAll(POEventListener listener) {
		super.subscribeToAll(listener);
		this.delegateSink.subscribe(listener, POEventType.ProcessInit);
		this.delegateSink.subscribe(listener, POEventType.ProcessDone);
	}

	@Override
	public void unsubscribe(POEventListener listener, POEventType type) {
		super.unsubscribe(listener, type);
		this.delegateSink.unsubscribe(listener, type);
	}

	@Override
	public void unSubscribeFromAll(POEventListener listener) {
		super.unSubscribeFromAll(listener);
		this.delegateSink.unSubscribeFromAll(listener);
	}

	@Override
	public void sendPunctuation(PointInTime punctuation) {
		synchronized (this.subscriptions) {
			super.sendPunctuation(punctuation);
		}
	}

	@Override
	public AbstractPipe<R, W> clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
