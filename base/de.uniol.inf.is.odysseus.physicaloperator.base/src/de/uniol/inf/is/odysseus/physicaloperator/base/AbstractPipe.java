package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POPortEvent;

public abstract class AbstractPipe<R, W> extends AbstractSource<W> implements
		IPipe<R, W> {

	public enum OutputMode  {NEW_ELEMENT, MODIFIED_INPUT, INPUT};
	
	final protected POEvent openInitEvent = new POEvent(this,
			POEventType.OpenInit);
	final private POEvent openDoneEvent = new POEvent(this,
			POEventType.OpenDone);

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractPipe.class);

	abstract protected void process_next(R object, int port);

	final protected ArrayList<Subscription<ISource<? extends R>>> subscribedTo = new ArrayList<Subscription<ISource<? extends R>>>();

	protected POEvent[] processInitEvent = null;
	protected POEvent[] processDoneEvent = null;

	protected int noInputPorts = -1;
	private boolean[] inputExclusive;

	private boolean allInputsDone = false;

	@Override
	public boolean isSink() {
		return true;
	}
	
	abstract public OutputMode getOutputMode();

	public void setNoOfInputPort(int ports) {
		if (ports > noInputPorts) {
			this.noInputPorts = ports;
			processInitEvent = new POEvent[ports];
			processDoneEvent = new POEvent[ports];
			inputExclusive = new boolean[ports];
			for (int i = 0; i < ports; i++) {
				processInitEvent[i] = new POPortEvent(this,
						POEventType.ProcessInit, i);
				processDoneEvent[i] = new POPortEvent(this,
						POEventType.ProcessDone, i);
				inputExclusive[i] = false;
			}
		}
	}

	public void close() {
	};
	
	// Classes for Objects not implementing IClone (e.g. ByteBuffer, String, etc.)
	// MUST override this method (else there will be a ClassCastException)
	protected R cloneIfNessessary(R object, boolean exclusive, int port){
		if (getOutputMode()==OutputMode.MODIFIED_INPUT && !isInputExclusive(port)) {
			object = (R) ((IClone)object).clone();
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
		switch(out){
		case NEW_ELEMENT:
			return ret;		
		default: // MODIFIED_INPUT und INPUT
			// Wenn einer der Eingänge nicht exclusive ist
			// das Ergebnis auch nicht exclusive
			for (int i=0;i<inputExclusive.length && ret;i++){
				ret = ret && inputExclusive[i];
			}
			return ret;
		}
	}

	@Override
	public void process(R object, int port, boolean exclusive) {
		// if (!isOpen()) System.err.println(this+" PROCESS BEFORE OPEN!!!");
		// evtl. spaeter wieder einbauen? Exception?
		fire(processInitEvent[port]);
		process_next(cloneIfNessessary(object, exclusive, port), port);
		fire(processDoneEvent[port]);
	}

	private void setInputExclusive(boolean exclusive, int port) {
		this.inputExclusive[port] = exclusive;
	}
	
	private boolean isInputExclusive(int port){
		return inputExclusive[port];
	}
	
	@Override
	public void process(Collection<? extends R> object, int port,
			boolean exclusive) {
		setInputExclusive(exclusive, port);
		for (R cur : object) {
			fire(processInitEvent[port]);
			process_next(cur, port);
			fire(processDoneEvent[port]);
		}
	}

	@Override
	final synchronized public void open() throws OpenFailedException {
		if (!isOpen() && isActive()) {
			allInputsDone = false;
			process_open();
			fire(openInitEvent);
		}
		synchronized (this.subscribedTo) {
			for (Subscription<ISource<? extends R>> sub : this.subscribedTo) {
				sub.done = false;
				sub.target.open();
			}
		}
		if (!isOpen()) {
			fire(openDoneEvent);
			setOpen(true);
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// empty default Implementation
	}

	@Override
	protected void process_done() {
		// empty default Implementation
	}

	// Method is called on every process_done, use to do port specific
	// processing
	protected void process_done(int port) {

	}

	public boolean isAllInputsDone() {
		return allInputsDone;
	}

	@Override
	final public synchronized void done(int port) {
		// System.out.println(this+" done on port " + port + " called");
		if (logger.isDebugEnabled()) {
			logger.debug(this + "(" + hashCode() + ") done on port " + port
					+ " called");
		}
		process_done(port);
		if (isDone()) {
			propagateDone();
		} else {
			synchronized (this.subscribedTo) {
				boolean allDone = true;
				for (Subscription<ISource<? extends R>> sub : this.subscribedTo) {
					if (sub.port == port) {
						sub.done = true;
					} else {
						allDone = allDone && sub.done;
					}
				}
				this.allInputsDone = allDone;
				if (allDone && isDone()) {
					propagateDone();
				}
			}
		}
	}

	/**
	 * Every ISink can have additional conditions, if it is done (e.g. in a
	 * buffer every item must be processed) by overwriting this Method these
	 * conditions can be set If this method is overwrittten, the overwriter is
	 * responsible for calling done(propagateDone) again!
	 * 
	 * @return true if done can be propagated (Default is true)
	 */
	protected boolean isDone() {
		return allInputsDone;
	}

	@Override
	public void subscribeTo(ISource<? extends R> source, int port) {
		if (port >= this.noInputPorts) {
			setNoOfInputPort(port + 1);
		}
		Subscription<ISource<? extends R>> sub = new Subscription<ISource<? extends R>>(
				source, port);
		synchronized (this.subscribedTo) {
			if (!this.subscribedTo.contains(sub)) {
				this.subscribedTo.add(sub);
				source.subscribe(this, port);
			}
		}
	}

	public Subscription<ISource<? extends R>> getsubscribedTo(int port) {
		synchronized (subscribedTo) {
			for (Subscription<ISource<? extends R>> s : subscribedTo) {
				if (s.port == port) {
					return s;
				}
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	final public List<Subscription<ISource<? extends R>>> getSubscribedTo() {
		synchronized (this.subscribedTo) {
			return (List<Subscription<ISource<? extends R>>>) this.subscribedTo
					.clone();
		}
	}

	@Override
	public void unsubscribeSubscriptionTo(ISource<? extends R> source, int port) {
		if (this.subscribedTo.remove(new Subscription<ISource<? extends R>>(
				source, port))) {
			source.unsubscribe(this, port);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public AbstractPipe<R, W> clone() {
		synchronized (this.subscribedTo) {
			AbstractPipe<R, W> pipe = (AbstractPipe<R, W>) super.clone();
			for (Subscription<ISource<? extends R>> sub : this.subscribedTo) {
				sub.target.subscribe(this, sub.port);
			}
			return pipe;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}

	@Override
	public void processPunctuation(PointInTime timestamp) {
		sendPunctuation(timestamp);
	}
}
