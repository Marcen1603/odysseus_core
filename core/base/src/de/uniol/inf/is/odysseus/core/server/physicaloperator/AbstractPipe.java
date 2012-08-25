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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.event.POEventType;

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
		protected void process_next(R object, int port) {
			if (isOpen()){
				AbstractPipe.this.delegatedProcess(object, port);
			}
		}

		@Override
		public void processPunctuation(PointInTime timestamp, int port) {
			AbstractPipe.this.delegatedProcessPunctuation(timestamp, port);
		}

		@Override
		public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
			AbstractPipe.this.delegatedProcessSecurityPunctuation(sp, port);
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
		public void close(List<PhysicalSubscription<ISink<?>>> callPath) {
			if (isOpen()) {
				callCloseOnChildren(callPath);
			}
		}

		@Override
		public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
			return AbstractPipe.this.delegatedIsSemanticallyEqual(ipo);
		}

	}

	public enum OutputMode {
		NEW_ELEMENT, MODIFIED_INPUT, INPUT
	}
	
	final protected DelegateSink delegateSink = new DelegateSink();
	private SDFMetaAttributeList metadataAttributeSchema = new SDFMetaAttributeList();
	private boolean metadataCalculated = false;

	// ------------------------------------------------------------------------

	public AbstractPipe() {
	}

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
	
	abstract public OutputMode getOutputMode();
	
//	@Override
//	public boolean isTransferExclusive() {
//		// Zun�chst Testen ob das Datum an mehrere Empf�nger
//		// versendet wird --> dann niemals exclusiv
//		boolean ret = super.isTransferExclusive();
//		OutputMode out = getOutputMode();
//		switch (out) {
//		case NEW_ELEMENT:
//			return ret;
//		default: // MODIFIED_INPUT und INPUT
//			// Wenn einer der Eing�nge nicht exclusive ist
//			// das Ergebnis auch nicht exclusive
//			// for (int i = 0; i < inputExclusive.length && ret; i++) {
//			// ret = ret && inputExclusive[i];
//			// }
//			return false;
//		}
//	}

	@Override
	protected boolean needsClone() {
		return getOutputMode() == OutputMode.MODIFIED_INPUT
				|| super.needsClone();
	}
	// ------------------------------------------------------------------------
	// OPEN
	// ------------------------------------------------------------------------

	@Override
	public void open() throws OpenFailedException {
		this.delegateSink.open();
	}

	@Override
	public void open(ISink<? super W> caller, int sourcePort,
			int sinkPort, List<PhysicalSubscription<ISink<?>>> callPath)
			throws OpenFailedException {
		super.open(caller, sourcePort, sinkPort, callPath);
		this.delegateSink.open(callPath);
	}

	public void delegatedProcessOpen() throws OpenFailedException {
		process_open();
	}

	@Override
	public boolean isOpen() {
		return super.isOpen() || this.delegateSink.isOpen();
	}

	// ------------------------------------------------------------------------
	// PROCESS
	// ------------------------------------------------------------------------
	
	@Override
	protected void process_open() throws OpenFailedException {
	}

	@Override
	public void process(R object, int port) {
		this.delegateSink.process(object, port);
	}

	private void delegatedProcess(R object, int port) {
		process_next(object, port);
	}

	private void delegatedProcessPunctuation(PointInTime timestamp, int port) {
		processPunctuation(timestamp, port);
	}

	private void delegatedProcessSecurityPunctuation(ISecurityPunctuation sp, int port) {
		processSecurityPunctuation(sp, port);
	}

	@Override
	public void process(Collection<? extends R> object, int port) {
		delegateSink.process(object, port);
	}

	abstract protected void process_next(R object, int port);

	@Override
	abstract public void processPunctuation(PointInTime timestamp, int port);

	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
		this.transferSecurityPunctuation(sp);
	}
	
	// ------------------------------------------------------------------------
	// CLOSE and DONE
	// ------------------------------------------------------------------------

	@Override
	public void close(ISink<? super W> caller, int sourcePort, int sinkPort,
			List<PhysicalSubscription<ISink<?>>> callPath) {
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
	final public void done(int port) {
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
			int sourceOutPort, SDFSchema schema) {
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
			int sinkInPort, int sourceOutPort, SDFSchema schema) {
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
			POEventType poEvent = (POEventType) type;
			if (poEvent.equals(POEventType.ProcessInit)
					|| poEvent.equals(POEventType.ProcessDone)) {
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
	abstract public AbstractPipe<R, W> clone();

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}

	public boolean delegatedIsSemanticallyEqual(IPhysicalOperator ipo) {
		return process_isSemanticallyEqual(ipo);
	}

	/**
	 * Liefert true, falls zwei Pipes die gleichen Quellen haben und die
	 * entsprechenden Verbindungen die gleichen SinkIn- bzw. Sourceout-Ports
	 * nutzen.
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

		Collection<PhysicalSubscription<ISource<? extends R>>> thisSubs = this
				.getSubscribedToSource();
		Collection<PhysicalSubscription<ISource<? extends R>>> otherSubs = other
				.getSubscribedToSource();

		// Unterschiedlich viele Quellen
		if (thisSubs.size() != otherSubs.size()) {
			return false;
		}
		// Iteration �ber die Subscriptions zu Quellen
		for (PhysicalSubscription<?> s1 : thisSubs) {
			boolean foundmatch = false;
			for (PhysicalSubscription<?> s2 : otherSubs) {
				// Subscription enth�lt gleiche Quelle und gleiche Ports
				if (((ISource<?>) s1.getTarget()) ==
						((ISource<?>) s2.getTarget())
						&& s1.getSinkInPort() == s2.getSinkInPort()
						&& s1.getSourceOutPort() == s2.getSourceOutPort()
						&& s1.getSchema().compareTo(s2.getSchema()) == 0) {
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
			for (PhysicalSubscription<ISource<? extends R>> sub : this
					.getSubscribedToSource()) {
				this.metadataAttributeSchema = SDFMetaAttributeList.union(
						this.metadataAttributeSchema, sub.getTarget()
								.getMetaAttributeSchema());
			}
			this.metadataCalculated = true;
		}
		return this.metadataAttributeSchema;
	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof IPipe))
			return false;
		if (!this.hasSameSources(ipo)){
			return false;
		}
		return process_isSemanticallyEqual(ipo);
	}
}
