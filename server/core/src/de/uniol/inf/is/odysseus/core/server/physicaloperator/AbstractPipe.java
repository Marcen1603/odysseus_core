/**********************************************************************************
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractPipe<R extends IStreamObject<?>, W extends IStreamObject<?>> extends AbstractSource<W>
		implements IPipe<R, W> {

	Logger logger = LoggerFactory.getLogger(AbstractPipe.class);

	// ------------------------------------------------------------------------
	// Delegation to simulate multiple inheritance
	// ------------------------------------------------------------------------

	protected class DelegateSink extends AbstractSink<R> {

		@Override
		protected void process_next(R object, int port) {
			AbstractPipe.this.delegatedProcess(object, port);
		}

		@Override
		public void processPunctuation(IPunctuation punctuation, int port) {
			AbstractPipe.this.delegatedProcessPunctuation(punctuation, port);
		}

		@Override
		protected void process_open(){
			AbstractPipe.this.delegatedProcessOpen();
		}

		@Override
		protected void process_close() {
			AbstractPipe.this.delegatedProcessClose();
		}

		@Override
		protected final ISink<R> getInstance() {
			return AbstractPipe.this;
		}

		@Override
		public void close(List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath,
				List<IOperatorOwner> forOwners) {
			if (logger.isTraceEnabled()) {
				logger.trace(MessageFormat.format("Closing {0} for {1}", getName(), forOwners));
			}
			internal_close(callPath, forOwners, false);
		}

		protected void setOpen(boolean state) {
			sinkOpen.set(state);
		}

		@Override
		public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
			return AbstractPipe.this.delegatedIsSemanticallyEqual(ipo);
		}

		// Needed because isOpen needs to be called from AbstractPipe
		@Override
		public boolean isOpen() {
			return AbstractPipe.this.isOpen();
		}

		public boolean sinkIsOpen() {
			return super.isOpen();
		}

		public boolean sinkIsStarted() {
			return super.isStarted();
		}

		@Override
		protected void sourceUnsubscribed(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> subscription) {
			AbstractPipe.this.sourceUnsubscribed(subscription);
		}

		@Override
		public void partial(IOperatorOwner id, int sheddingFactor) {
			AbstractPipe.this.partial(id, sheddingFactor);
		}
	}

	public enum OutputMode {
		NEW_ELEMENT, MODIFIED_INPUT, INPUT
	}

	protected final DelegateSink delegateSink = new DelegateSink();
	private SDFMetaAttributeList metadataAttributeSchema = new SDFMetaAttributeList();
	private boolean metadataCalculated = false;

	/**
	 * In case of circular dependencies, done should propagate only once (to prevent
	 * endless loops).
	 */
	private boolean didNotPropagateDoneBefore = true;

	// ------------------------------------------------------------------------

	public AbstractPipe() {
	}

	public AbstractPipe(AbstractPipe<R, W> pipe) {
		super(pipe);
		delegateSink.setInputPortCount(pipe.getInputPortCount());
	}

	@Override
	public final boolean isSink() {
		return true;
	}

	@Override
	public final int getInputPortCount() {
		return this.delegateSink.noInputPorts;
	}
	
	@Override
	public void setInputPortCount(int count) {
		this.delegateSink.setInputPortCount(count);
	}

	public final SDFSchema getInputSchema(int port) {
		return this.delegateSink.getInputSchema(port);
	}

	// Returns the input mode of input port 0
	// Deprecated soon, use getInputMode instead
	public abstract OutputMode getOutputMode();

	public enum InputMode {
		SINK, // Elements entering this operator will not leave this operator
		MODIFIED_INPUT, // Elements entering this operator will leave this operator modified
		INPUT // Elements entering this operator will leave this operator unmodified
	}

	// Returns whether an element entering this operator via the specified will be
	// modified,
	// passed on, or if the element will not leave this operator to another operator
	// Default implementation returns getOutputMode() mapped to appropriate values
	// of InputMode
	public InputMode getInputPortMode(int inputPort) {
		switch (getOutputMode()) {
		case NEW_ELEMENT:
			return InputMode.SINK;
		case MODIFIED_INPUT:
			return InputMode.MODIFIED_INPUT;
		case INPUT:
			return InputMode.INPUT;
		default:
			return null;
		}
	}

	// ------------------------------------------------------------------------
	// OPEN
	// ------------------------------------------------------------------------

	@Override
	public final synchronized void open(IOperatorOwner owner){
		didNotPropagateDoneBefore = true;
		reconnectSinks();
		this.delegateSink.open(owner);
	}

	@Override
	public final synchronized void open(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		didNotPropagateDoneBefore = true;
		// First: Call open for the source part. Activate subscribers and call
		// process_open
		super.open(caller, sourcePort, sinkPort, callPath, forOwners);
		// Second: Call open for the sink part. Call open on connected sources
		// and do not call process_open
		this.delegateSink.open(callPath, forOwners);
	}

	public void delegatedProcessOpen(){
		process_open();
	}

	@Override
	public boolean isOpen() {
		return super.isOpen() || this.delegateSink.sinkIsOpen();
	}

	@Override
	public boolean isOpenFor(IOperatorOwner owner) {
		return this.delegateSink.isOpenFor(owner);
	}

	/**
	 * If stateful operator: override {@link #process_open_internal()} instead of
	 * this method.
	 */
	@Override
	protected void process_open() {
	}

	// ------------------------------------------------------------------------
	// START
	// ------------------------------------------------------------------------

	@Override
	public final synchronized void start(IOperatorOwner owner) {
		this.delegateSink.start(owner);
	}

	@Override
	public final synchronized void start(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		super.start(caller, sourcePort, sinkPort, callPath, forOwners);
		this.delegateSink.start(callPath, forOwners);
	}

	@Override
	protected void process_start(){
	}

	@Override
	public boolean isStarted() {
		return super.isStarted() || this.delegateSink.sinkIsStarted();
	}

	// ------------------------------------------------------------------------
	// SUSPEND/RESUME
	// ------------------------------------------------------------------------
	@Override
	public final void suspend(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		super.suspend(caller, sourcePort, sinkPort, callPath, forOwners);
		this.delegateSink.suspend(callPath, forOwners);
	}

	@Override
	public final void resume(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		super.resume(caller, sourcePort, sinkPort, callPath, forOwners);
		this.delegateSink.resume(callPath, forOwners);
	}

	// ------------------------------------------------------------------------
	// PROCESS
	// ------------------------------------------------------------------------

	@Override
	public void process(R object, int port) {
		this.delegateSink.process(object, port);
	}

	private void delegatedProcess(R object, int port) {
		process_next(object, port);
	}

	private void delegatedProcessPunctuation(IPunctuation punctuation, int port) {
		processPunctuation(punctuation, port);
	}

	protected abstract void process_next(R object, int port);

	// ------------------------------------------------------------------------
	// CLOSE and DONE
	// ------------------------------------------------------------------------

	@Override
	public final void close(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		this.delegateSink.close(callPath, forOwners);
		super.close(caller, sourcePort, sinkPort, callPath, forOwners);
	}

	@Override
	public final void close(IOperatorOwner owner) {
		// Hint: This method can only be called
		// from a query
		this.delegateSink.close(owner);

		if (!hasOpenSinkSubscriptions()) {
			// is this process_close correct?
			process_close();
			// The are cases where elements are connected that
			// are no roots of this query
			// here we need to call close by hand
			closeAllSinkSubscriptions();
			open.set(false);
			started.set(false);
		}
	}

	public void delegatedProcessClose() {
		process_close();
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
	public final void done(int port) {
		process_done(port);
		this.delegateSink.done(port);
		if (isDone() && didNotPropagateDoneBefore) {
			didNotPropagateDoneBefore = false;
			propagateDone();
		}
	}


	/**
	 * Every ISink can have additional conditions, if it is done (e.g. in a buffer
	 * every item must be processed) by overriding this Method these conditions can
	 * be set If this method is overridden, the overrider is responsible for calling
	 * done(propagateDone) again!
	 *
	 * @return true if done can be propagated (Default is true)
	 */
	@Override
	public boolean isDone() {
		return this.delegateSink.isDone();
	}

	// ------------------------------------------------------------------------
	// suspend and resume
	// ------------------------------------------------------------------------

	@Override
	public void suspend(IOperatorOwner id) {
		this.delegateSink.suspend(id);
	}

	@Override
	public void resume(IOperatorOwner id) {
		this.delegateSink.resume(id);
	}

	// ------------------------------------------------------------------------
	// partial
	// ------------------------------------------------------------------------
	@Override
	public void partial(IOperatorOwner id, int sheddingFactor) {
		delegateSink.partial(id, sheddingFactor, this);
	}

	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

	@Override
	public void subscribeToSource(ISource<IStreamObject<?>> source, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		this.delegateSink.subscribeToSource(source, sinkInPort, sourceOutPort, schema);
	}

	public void subscribeToSource(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub) {
		this.delegateSink.subscribeToSource(sub);
	}

	protected void newSourceSubscribed(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub) {
		// Override if needed
	}

	@Override
	public void unsubscribeFromSource(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> subscription) {
		this.delegateSink.unsubscribeFromSource(subscription);
		// Override if needed
	}

	protected void sourceUnsubscribed(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub) {
	}

	@Override
	public AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> getSubscribedToSource(int port) {
		return this.delegateSink.getSubscribedToSource(port);
	}

	@Override
	public final List<AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?>> getSubscribedToSource() {
		return Collections.unmodifiableList(delegateSink.getSubscribedToSource());
	}

	@Override
	public void unsubscribeFromSource(ISource<IStreamObject<?>> source, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		this.delegateSink.unsubscribeFromSource(source, sinkInPort, sourceOutPort, schema);
	}

	@Override
	public void unsubscribeFromAllSources() {
		this.delegateSink.unsubscribeFromAllSources();
	}

	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		if (type instanceof POEventType) {
			POEventType poEvent = (POEventType) type;
			if (poEvent.equals(POEventType.ProcessInit) || poEvent.equals(POEventType.ProcessDone)) {
				this.delegateSink.subscribe(listener, type);
			}
		}
		super.subscribe(listener, type);
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
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}

	public boolean delegatedIsSemanticallyEqual(IPhysicalOperator ipo) {
		return process_isSemanticallyEqual(ipo);
	}

	/**
	 * Liefert true, falls zwei Pipes die gleichen Quellen haben und die
	 * entsprechenden Verbindungen die gleichen SinkIn- bzw. Sourceout-Ports nutzen.
	 *
	 * @param o
	 *            zu �berpr�fendes Objekt (idealerweise eine AbstractPipe)
	 */
	public boolean hasSameSources(Object o) {
		// Abbruch, falls das zu �berpr�fende Objekt keine AbstractPipe ist
		if (!(o instanceof AbstractPipe)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		AbstractPipe<R, W> other = (AbstractPipe<R, W>) o;

		Collection<AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?>> thisSubs = this.getSubscribedToSource();
		Collection<AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?>> otherSubs = other
				.getSubscribedToSource();

		// Unterschiedlich viele Quellen
		if (thisSubs.size() != otherSubs.size()) {
			return false;
		}

		// Iteration �ber die Subscriptions zu Quellen
		for (AbstractPhysicalSubscription<?, ?> s1 : thisSubs) {
			boolean foundmatch = false;
			for (AbstractPhysicalSubscription<?, ?> s2 : otherSubs) {
				if ((s1.getSource() == s2.getSource()) && s1.getSinkInPort() == s2.getSinkInPort()
						&& s1.getSourceOutPort() == s2.getSourceOutPort()
						&& ((s1.getSchema() == null && s2.getSchema() == null)
								|| (s1.getSchema().compareTo(s2.getSchema())) == 0)) {
					foundmatch = true;
				}
			}
			// Operatoren haben mindestens eine unterschiedliche Quelle
			if (!foundmatch) {
				return false;
			}
		}
		return true;
	}

	public boolean isContainedIn(IPipe<R, W> ip) {
		return false;
	}

	@Override
	public SDFMetaAttributeList getMetaAttributeSchema() {
		if (!this.metadataCalculated) {
			for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.getSubscribedToSource()) {
				this.metadataAttributeSchema = SDFMetaAttributeList.union(this.metadataAttributeSchema,
						sub.getSource().getMetaAttributeSchema());
			}
			this.metadataCalculated = true;
		}
		return this.metadataAttributeSchema;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof IPipe))
			return false;
		return process_isSemanticallyEqual(ipo);
	}

	@Override
	public boolean hasInput() {
		return delegateSink.hasInput();
	}

	@Override
	protected final AbstractPipe<R, W> clone() {
		throw new IllegalArgumentException("Do not clone physical operators!");
	}

}
