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
package de.uniol.inf.is.odysseus.core.streamconnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransfer;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class DefaultStreamConnection<In extends IStreamObject<?>> extends
		ListenerSink<In> implements ITransfer<In>{

	private static final Logger LOG = LoggerFactory
			.getLogger(DefaultStreamConnection.class);

	private final Collection<IPhysicalOperator> operators;
	private final Map<Integer, IPhysicalOperator> portOperatorMap;
	private final Map<IPhysicalOperator, Integer> operatorPortMap;
	private final Collection<IPhysicalOperator> connectedOperators = Lists
			.newArrayList();
	private final List<ISubscription<ISource<IStreamObject<?>>,?>> subscriptions;

	private final ArrayList<In> collectedObjects = new ArrayList<In>();
	private final ArrayList<Integer> collectedPorts = new ArrayList<Integer>();

	private final Collection<IStreamElementListener<In>> listeners = new ArrayList<IStreamElementListener<In>>();
	private final Map<String, Collection<IStreamElementListener<In>>> specialListener = Maps
			.newHashMap();

	private boolean connected = false;
	private boolean enabled = true;

	private boolean isOpen = true;
	private boolean isDone = true;

	private Map<String, String> infos;

	private ITransferArea<In,In> transferHandler = null;

	public DefaultStreamConnection(IPhysicalOperator operator) {
		this(Lists.newArrayList(operator));
	}

	public DefaultStreamConnection(Collection<IPhysicalOperator> operators) {
		this.operators = Objects.requireNonNull(operators, "List of operators must not be null!");
		// Preconditions.checkArgument(!operators.isEmpty(),
		//		"List of operators must not be empty!");

		portOperatorMap = generatePortMap(operators);
		operatorPortMap = generateOperatorMap(portOperatorMap);
		subscriptions = determineSubscriptions(operators);

	}
	
	@Override
	public ImmutableList<IPhysicalOperator> getConnectedOperators() {
		return ImmutableList.copyOf(operators);
	}

	@Override
	public IPhysicalOperator getOperatorOfPort(int port) {
		return portOperatorMap.get(port);
	}

	@Override
	public int getPortOfOperator(IPhysicalOperator operator) {
		return operatorPortMap.get(operator);
	}

	private static Map<IPhysicalOperator, Integer> generateOperatorMap(
			Map<Integer, IPhysicalOperator> map) {
		Map<IPhysicalOperator, Integer> m = Maps.newHashMap();
		for (Integer port : map.keySet()) {
			m.put(map.get(port), port);
		}
		return m;
	}

	private static Map<Integer, IPhysicalOperator> generatePortMap(
			Collection<IPhysicalOperator> ops) {
		Map<Integer, IPhysicalOperator> map = Maps.newHashMap();
		int counter = 0;
		for (IPhysicalOperator op : ops) {
			map.put(counter++, op);
		}
		return map;
	}

	@Override
	public SDFSchema getOutputSchema() {
		return operators.iterator().next().getOutputSchema();
	}

	@Override
	public final void connect() {
		for (IPhysicalOperator op : operators) {
			connect(op);
		}
		if (transferHandler != null){
			transferHandler.init(this, operators.size());
		}
		connected = true;
	}

	public void setTransferHandler(ITransferArea<In, In> transferHandler) {
		this.transferHandler = transferHandler;
	}

	@Override
	public final void disconnect() {
		for (IPhysicalOperator op : operators) {
			disconnect(op);
		}
		connected = false;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void process(In element, int port) {
		if (transferHandler != null) {
			transferHandler.transfer(element, port);
			transferHandler.newElement(element, port);
		} else {

			process_internal(element, port);
		}
	}

	private void process_internal(In element, int port) {
		if (!enabled) {
			collectElement(element, port);

		} else {
			notifyListeners(element, port);
		}
	}



	@Override
	public void transfer(In object) {
		// Will never be called

	}

	@Override
	public void transfer(In object, int sourceOutPort) {
		process_internal(object, sourceOutPort);
	}

	@Override
	public void propagateDone() {
		// will never be called
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		// Will never be called
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation, int outPort) {
		notifyListenersPunctuation(punctuation, outPort);
	}

	@Override
	public void disable() {
		enabled = false;
	}

	@Override
	public void enable() {
		enabled = true;
		processCollectedElements();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void addStreamElementListener(IStreamElementListener<In> listener) {
		Objects.requireNonNull(listener,  "Listener to add to DefaultStreamConnection must not be null!");

		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	@Override
	public void addStreamElementListener(IStreamElementListener<In> listener,
			String sinkName) {
		Objects.requireNonNull(listener, "Listener to add to DefaultStreamConnection must not be null!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(sinkName),
				//"Sinkname must not be null or empty!");

		synchronized (specialListener) {
			Collection<IStreamElementListener<In>> l = specialListener
					.get(sinkName);
			if (l == null) {
				l = Lists.newArrayList();
				specialListener.put(sinkName, l);
			}
			l.add(listener);
		}
	}

	@Override
	public void removeStreamElementListener(IStreamElementListener<In> listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public void removeStreamElementListener(
			IStreamElementListener<In> listener, String sinkName) {
		synchronized (specialListener) {
			Collection<IStreamElementListener<In>> l = specialListener
					.get(sinkName);
			if (l != null) {
				l.remove(listener);

				if (l.isEmpty()) {
					specialListener.remove(l);
				}
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (transferHandler != null){
			transferHandler.sendPunctuation(punctuation, port);
			transferHandler.newElement(punctuation, port);
		}else{
			sendPunctuation(punctuation, port);
		}
	}

	@Override
	public void setSuppressPunctuations(boolean suppressPunctuations) {
		// IGNORE
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	// @Override
	public void open() throws OpenFailedException {
		LOG.debug("Opening");
		isOpen = true;
	}

	@Override
	public void open(IOperatorOwner id) {
		LOG.warn("Opening with operator owner not implemented");
		open();
	}

	// @Override
	public void close() {
		LOG.debug("Closing");
		isOpen = false;

		for (IPhysicalOperator s : connectedOperators
				.toArray(new IPhysicalOperator[0])) {
			disconnect(s);
		}
	}

	@Override
	public void close(IOperatorOwner id) {
		LOG.warn("Closing with operator owner not implemented");
		close();
	}

	@Override
	public Map<String, String> getParameterInfos() {
		return infos;
	}

	@Override
	public void addParameterInfo(String key, Object value) {
		this.infos.put(key, value.toString());
	}

	@Override
	public void setParameterInfos(Map<String, String> infos) {
		this.infos = infos;
	}

	@Override
	public void addUniqueId(IOperatorOwner owner, Resource id) {
	}

	@Override
	public void removeUniqueId(IOperatorOwner key) {
	}

	@Override
	public Map<IOperatorOwner, Resource> getUniqueIds() {
		return null;
	}

	protected final void notifyListeners(In element, int port) {
		LOG.trace("Receiving element from port {}: {}", port, element);
		IPhysicalOperator senderOperator = portOperatorMap.get(port);
		synchronized (listeners) {
			for (IStreamElementListener<In> l : listeners) {
				try {
					l.streamElementReceived(senderOperator, element, port);
				} catch (Throwable t) {
					LOG.error(
							"Exception during invoking listener for DefaultStreamConnection",
							t);
				}
			}
		}

		String name = getOperatorNameFromPort(port);
		if (!Strings.isNullOrEmpty(name)) {
			synchronized (specialListener) {
				Collection<IStreamElementListener<In>> l = specialListener
						.get(name);
				if (l != null) {
					for (IStreamElementListener<In> ls : l) {
						try {
							ls.streamElementReceived(senderOperator, element,
									port);
						} catch (Throwable t) {
							LOG.error(
									"Exception during invoking specialized listener for DefaultStreamConnection for sinkname {}",
									name, t);
						}
					}
				}
			}
		}
	}

	protected final void notifyListenersPunctuation(IPunctuation point, int port) {
		LOG.debug("Receiving punctuation from port {}: {}", port, point);
		IPhysicalOperator senderOperator = portOperatorMap.get(port);
		synchronized (listeners) {
			for (IStreamElementListener<In> l : listeners) {
				try {
					l.punctuationElementReceived(senderOperator, point, port);
				} catch (Throwable t) {
					LOG.error(
							"Exception during invoking punctuation listener for DefaultStreamConnection",
							t);
				}
			}
		}

		String name = getOperatorNameFromPort(port);
		if (!Strings.isNullOrEmpty(name)) {
			synchronized (specialListener) {
				Collection<IStreamElementListener<In>> l = specialListener
						.get(name);
				if (l != null) {
					for (IStreamElementListener<In> ls : l) {
						try {
							ls.punctuationElementReceived(senderOperator,
									point, port);
						} catch (Throwable t) {
							LOG.error(
									"Exception during invoking specialized punctuation listener for DefaultStreamConnection for sinkname {}",
									name, t);
						}
					}
				}
			}
		}
	}

	private String getOperatorNameFromPort(int port) {
		IPhysicalOperator operator = portOperatorMap.get(port);
		String name = operator.getName();
		return name;
	}

	@SuppressWarnings({ "unchecked" })
	private void connect(IPhysicalOperator operator) {
		if (operator instanceof ISource) {
			ISource<In> source = (ISource<In>) operator;
			source.connectSink((ISink<IStreamObject<?>>) this, operatorPortMap.get(operator), 0,
					operator.getOutputSchema());
		} else {
			ISink<?> sink = (ISink<?>) operator;
			Collection<?> subsToSource = sink.getSubscribedToSource();
			for (Object sourceSub : subsToSource) {
				AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?> physSourceSub = (AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?>) sourceSub;

				ISource<In> source = (ISource<In>) physSourceSub.getSource();
				int sourceOutPort = determineSourceOutPort(source, operator);

				physSourceSub.getSource().connectSink((ISink<IStreamObject<?>>) this,
						operatorPortMap.get(operator), sourceOutPort,
						physSourceSub.getSource().getOutputSchema());
			}
		}

		connectedOperators.add(operator);
	}

	private int determineSourceOutPort(ISource<In> sourceOperator,
			IPhysicalOperator targetOperator) {
		Collection<AbstractPhysicalSubscription<?,ISink<IStreamObject<?>>>> subsToSinks = sourceOperator
				.getSubscriptions();
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sinkSub : subsToSinks) {
			if (sinkSub.getSource().equals(targetOperator)) {
				return sinkSub.getSourceOutPort();
			}
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	private void disconnect(IPhysicalOperator operator) {
		if (operator instanceof ISource) {
			ISource<In> source = (ISource<In>) operator;
			source.disconnectSink((ISink<IStreamObject<?>>) this, operatorPortMap.get(operator), 0,
					operator.getOutputSchema());
		} else {
			ISink<?> sink = (ISink<?>) operator;
			Collection<?> subsToSource = sink.getSubscribedToSource();

			for (Object sourceSub : subsToSource) {
				AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?> physSourceSub = (AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?>) sourceSub;

				ISource<In> source = (ISource<In>) physSourceSub.getSource();
				int sourceOutPort = determineSourceOutPort(source, operator);

				physSourceSub.getSource().disconnectSink((ISink<IStreamObject<?>>) this,
						operatorPortMap.get(operator), sourceOutPort,
						physSourceSub.getSource().getOutputSchema());
			}
		}

		connectedOperators.remove(operator);
	}

	private void collectElement(In element, int port) {
		synchronized (collectedObjects) {
			collectedObjects.add(element);
			collectedPorts.add(port);
		}
	}

	private void processCollectedElements() {
		if (collectedObjects.size() > 0) {
			synchronized (collectedObjects) {
				// gesammelte Daten nachträglich verarbeiten lassen
				for (int i = 0; i < collectedObjects.size(); i++) {
					process(collectedObjects.get(i), collectedPorts.get(i));
				}
				collectedObjects.clear();
				collectedPorts.clear();
			}
		}
	}

	private List<ISubscription<ISource<IStreamObject<?>>,?>> determineSubscriptions(
			Collection<IPhysicalOperator> operators) {
		List<ISubscription<ISource<IStreamObject<?>>,?>> subscriptions = Lists
				.newLinkedList();
		for (IPhysicalOperator operator : operators) {
			subscriptions
					.addAll(determineSubscriptions(operator));
		}
		return subscriptions;
	}

	@SuppressWarnings("unchecked")
	private List<ISubscription<ISource<IStreamObject<?>>,?>> determineSubscriptions(
			IPhysicalOperator operator) {
		// Preconditions.checkArgument(operator.isSink() || operator.isSource(),
				//"Operator must be sink and/or source!");

		List<ISubscription<ISource<IStreamObject<?>>,?>> subs = Lists.newLinkedList();

		if (operator.isSource()) {
			subs.add((ISubscription<ISource<IStreamObject<?>>,?>) new ControllablePhysicalSubscription<ISource<IStreamObject<?>>,ISink<IStreamObject<?>>>(
					 (ISource<IStreamObject<?>>)operator, (ISink<IStreamObject<?>>)this, 0, 0, operator.getOutputSchema()));
		} else {
			Collection<?> subscriptions = ((ISink<?>) operator)
					.getSubscribedToSource();

			for (Object subscription : subscriptions) {
				AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?> sub = (AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?>) subscription;
				subs.add((ISubscription<ISource<IStreamObject<?>>,?>) new ControllablePhysicalSubscription<ISource<IStreamObject<?>>,ISink<IStreamObject<?>>>(
						 sub.getSource(), (ISink<IStreamObject<?>>)this, sub.getSinkInPort(), sub
								.getSourceOutPort(), sub.getSchema()));
			}
		}

		return subs;
	}

	@Override
	public boolean hasInput() {
		return true;
	}

	@Override
	public void setDebug(boolean debug) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOpenFor(IOperatorOwner owner) {
		return false;
	}

	@Override
	public void suspend(IOperatorOwner id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume(IOperatorOwner id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partial(IOperatorOwner id, int sheddingFactor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogicalOperator(ILogicalOperator op) {
		// TODO Auto-generated method stub

	}

	@Override
	public ILogicalOperator getLogicalOperator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInputPortCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void start(IOperatorOwner id) throws StartFailedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void subscribeToSource(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> Subscription) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ImmutableList<ISubscription<ISource<IStreamObject<?>>, ?>> getSubscriptions() {
		return ImmutableList.copyOf(subscriptions);
	}
}
