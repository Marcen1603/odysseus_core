package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractPipe<R, W> extends AbstractSource<W> implements
		IPipe<R, W> {
	private class DelegateSink extends AbstractSink<R> {

		@Override
		protected void process_next(R object, int port, boolean exclusive) {
			AbstractPipe.this.delegatedProcess(object, port, exclusive);
		}

		@Override
		protected void setInputPortCount(int ports) {
			super.setInputPortCount(ports);
			inputExclusive = new boolean[ports];
			Arrays.fill(inputExclusive, false);
		}

		@Override
		final protected ISink<R> getInstance() {
			return AbstractPipe.this;
		}

	};

	DelegateSink delegateSink = new DelegateSink();

	public enum OutputMode {
		NEW_ELEMENT, MODIFIED_INPUT, INPUT
	};

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractPipe.class);

	abstract protected void process_next(R object, int port);

	private boolean[] inputExclusive;

	@Override
	public boolean isSink() {
		return true;
	}

	abstract public OutputMode getOutputMode();

	public void close() {
		this.delegateSink.close();
		super.close();
	};

	// Classes for Objects not implementing IClone (e.g. ByteBuffer, String,
	// etc.)
	// MUST override this method (else there will be a ClassCastException)
	@SuppressWarnings("unchecked")
	protected R cloneIfNessessary(R object, boolean exclusive, int port) {
		if (getOutputMode() == OutputMode.MODIFIED_INPUT
				&& !isInputExclusive(port)) {
			object = (R) ((IClone) object).clone();
			setInputExclusive(true, port);
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
			for (int i = 0; i < inputExclusive.length && ret; i++) {
				ret = ret && inputExclusive[i];
			}
			return ret;
		}
	}

	@Override
	public void process(R object, int port, boolean exclusive) {
		setInputExclusive(exclusive, port);
		this.delegateSink.process(object, port, exclusive);
	}

	private void delegatedProcess(R object, int port, boolean exclusive) {
		process_next(cloneIfNessessary(object, exclusive, port), port);
	}

	private void setInputExclusive(boolean exclusive, int port) {
		this.inputExclusive[port] = exclusive;
	}

	private boolean isInputExclusive(int port) {
		return inputExclusive[port];
	}

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
	final public synchronized void done(int port) {
		if (logger.isDebugEnabled()) {
			logger.debug(this + "(" + hashCode() + ") done on port " + port
					+ " called");
		}
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
	public void subscribeTo(ISource<? extends R> source, int port) {
		// TODO vernuenftig synchronosieren
		this.delegateSink.subscribeTo(source, port);
	}

	public Subscription<ISource<? extends R>> getSubscribedTo(int port) {
		return this.delegateSink.getSubscribedTo(port);
	}

	@Override
	final public List<Subscription<ISource<? extends R>>> getSubscribedTo() {
		return delegateSink.getSubscribedTo();
	}

	@Override
	public void unsubscribeSubscriptionTo(ISource<? extends R> source, int port) {
		this.delegateSink.unsubscribeSubscriptionTo(source, port);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}

	@Override
	public void processPunctuation(PointInTime timestamp) {
		this.delegateSink.processPunctuation(timestamp);
	}

	@Override
	public void subscribe(POEventListener listener, POEventType type) {
		super.subscribe(listener, type);
		switch (type) {
		case ProcessInit:
		case ProcessDone:
		case ProcessInitNeg:
		case ProcessDoneNeg:
			this.delegateSink.subscribe(listener, type);
		default:
		}
	}

	@Override
	public void subscribeToAll(POEventListener listener) {
		super.subscribeToAll(listener);
		this.delegateSink.subscribe(listener, POEventType.ProcessInit);
		this.delegateSink.subscribe(listener, POEventType.ProcessDone);
		this.delegateSink.subscribe(listener, POEventType.ProcessInitNeg);
		this.delegateSink.subscribe(listener, POEventType.ProcessDoneNeg);
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
}
