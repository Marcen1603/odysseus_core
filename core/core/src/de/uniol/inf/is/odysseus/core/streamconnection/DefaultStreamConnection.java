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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class DefaultStreamConnection<In extends IStreamObject<?>> extends ListenerSink<In> {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultStreamConnection.class);

	private final Collection<IPhysicalOperator> operators;
	private final Map<Integer, IPhysicalOperator> portOperatorMap;
	private final Map<IPhysicalOperator, Integer> operatorPortMap;
	private final Collection<IPhysicalOperator> connectedOperators = Lists.newArrayList();
	private final List<ISubscription<? extends ISource<In>>> subscriptions;

	private boolean connected = false;
	private boolean enabled = true;

	private ArrayList<In> collectedObjects = new ArrayList<In>();
	private ArrayList<Integer> collectedPorts = new ArrayList<Integer>();

	private final Collection<IStreamElementListener<In>> listeners = new ArrayList<IStreamElementListener<In>>();
	private final Map<String, Collection<IStreamElementListener<In>>> specialListener = Maps.newHashMap();
	private boolean isOpen = true;

	private Map<String, String> infos;

	public DefaultStreamConnection(IPhysicalOperator operator) {
		this(Lists.newArrayList(operator));
	}

	public DefaultStreamConnection(Collection<IPhysicalOperator> operators) {
		Preconditions.checkNotNull(operators, "List of operators must not be null!");
		Preconditions.checkArgument(!operators.isEmpty(), "List of operators must not be empty!");

		this.operators = operators;
		portOperatorMap = generatePortMap(operators);
		operatorPortMap = generateOperatorMap(portOperatorMap);
		subscriptions = determineSubscriptions(operators);
	}

	@Deprecated
	@Override
	public final ImmutableList<ISubscription<? extends ISource<In>>> getSubscriptions() {
		return ImmutableList.copyOf(subscriptions);
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

	private static Map<IPhysicalOperator, Integer> generateOperatorMap(Map<Integer, IPhysicalOperator> map) {
		Map<IPhysicalOperator, Integer> m = Maps.newHashMap();
		for( Integer port : map.keySet() ) {
			m.put(map.get(port), port);
		}
		return m;
	}

	private static Map<Integer, IPhysicalOperator> generatePortMap(Collection<IPhysicalOperator> ops) {
		Map<Integer, IPhysicalOperator> map = Maps.newHashMap();
		int counter = 0;
		for( IPhysicalOperator op : ops ) {
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
		connected = true;
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
		if (!enabled) {
			collectElement(element, port);

		} else {
			notifyListeners(element, port);
		}
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
		Preconditions.checkNotNull(listener, "Listener to add to DefaultStreamConnection must not be null!");

		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}
	
	@Override
	public void addStreamElementListener(IStreamElementListener<In> listener, String sinkName) {
		Preconditions.checkNotNull(listener, "Listener to add to DefaultStreamConnection must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sinkName), "Sinkname must not be null or empty!");
		
		synchronized(specialListener ) {
			Collection<IStreamElementListener<In>> l = specialListener.get(sinkName);
			if( l == null ) {
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
	public void removeStreamElementListener(IStreamElementListener<In> listener, String sinkName) {
		synchronized(specialListener ) {
			Collection<IStreamElementListener<In>> l = specialListener.get(sinkName);
			if( l != null ) {
				l.remove(listener);
				
				if( l.isEmpty() ) {
					specialListener.remove(l);
				}
			}
		}		
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		notifyListenersPunctuation(punctuation, port);
	}

	@Override
	public boolean isOpen() {
		return isOpen;
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

		for (IPhysicalOperator s : connectedOperators.toArray(new IPhysicalOperator[0])) {
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
	public void addUniqueId(IOperatorOwner owner, String id) {
	}

	@Override
	public void removeUniqueId(IOperatorOwner key) {
	}

	@Override
	public Map<IOperatorOwner, String> getUniqueIds() {
		return null;
	}

	protected final void notifyListeners(In element, int port) {
		LOG.debug("Receiving element from port {}: {}", port, element);
		synchronized (listeners) {
			for (IStreamElementListener<In> l : listeners) {
				try {
					l.streamElementRecieved(element, port);
				} catch (Throwable t) {
					LOG.error("Exception during invoking listener for DefaultStreamConnection", t);
				}
			}
		}
		
		String name = getOperatorNameFromPort(port);
		if( !Strings.isNullOrEmpty(name)) {
			synchronized(specialListener) {
				Collection<IStreamElementListener<In>> l = specialListener.get(name);
				if( l != null ) {
					for( IStreamElementListener<In> ls : l ) {
						try {
							ls.streamElementRecieved(element, port);
						} catch( Throwable t ) {
							LOG.error("Exception during invoking specialized listener for DefaultStreamConnection for sinkname {}", name, t);
						}
					}
				}
			}
		}
	}

	protected final void notifyListenersPunctuation(IPunctuation point, int port) {
		LOG.debug("Receiving punctuation from port {}: {}", port, point);
		synchronized (listeners) {
			for (IStreamElementListener<In> l : listeners) {
				try {
					l.punctuationElementRecieved(point, port);
				} catch (Throwable t) {
					LOG.error("Exception during invoking punctuation listener for DefaultStreamConnection", t);
				}
			}
		}
		
		String name = getOperatorNameFromPort(port);
		if( !Strings.isNullOrEmpty(name)) {
			synchronized(specialListener) {
				Collection<IStreamElementListener<In>> l = specialListener.get(name);
				if( l != null ) {
					for( IStreamElementListener<In> ls : l ) {
						try {
							ls.punctuationElementRecieved(point, port);
						} catch( Throwable t ) {
							LOG.error("Exception during invoking specialized punctuation listener for DefaultStreamConnection for sinkname {}", name, t);
						}
					}
				}
			}
		}
	}

	protected final void notifyListenersSecurityPunctuation(ISecurityPunctuation sp, int port) {
		LOG.debug("Receiving security punctuation from port {}: {}", port, sp);
		synchronized (listeners) {
			for (IStreamElementListener<In> l : listeners) {
				try {
					l.securityPunctuationElementRecieved(sp, port);
				} catch (Throwable t) {
					LOG.error("Exception during invoking security punctuation listener for DefaultStreamConnection", t);
				}
			}
		}
		
		String name = getOperatorNameFromPort(port);
		if( !Strings.isNullOrEmpty(name)) {
			synchronized(specialListener) {
				Collection<IStreamElementListener<In>> l = specialListener.get(name);
				if( l != null ) {
					for( IStreamElementListener<In> ls : l ) {
						try {
							ls.securityPunctuationElementRecieved(sp, port);
						} catch( Throwable t ) {
							LOG.error("Exception during invoking specialized security punctuation listener for DefaultStreamConnection for sinkname {}", name, t);
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
		if( operator instanceof ISource ) {
			ISource<In> source = (ISource<In>)operator;
			source.connectSink(this, operatorPortMap.get(operator), 0, operator.getOutputSchema());
		} else {
			ISink<?> sink = (ISink<?>)operator;
			Collection<?> subs = sink.getSubscribedToSource();
			for( Object sub : subs ) {
				PhysicalSubscription<ISource<In>> physSub = (PhysicalSubscription<ISource<In>>)sub;
				physSub.getTarget().connectSink(this, operatorPortMap.get(operator), 0, physSub.getTarget().getOutputSchema());
			}
		}
		
		connectedOperators.add(operator);
	}

	@SuppressWarnings("unchecked")
	private void disconnect(IPhysicalOperator operator) {
		if( operator instanceof ISource ) {
			ISource<In> source = (ISource<In>)operator;
			source.disconnectSink(this, operatorPortMap.get(operator), 0, operator.getOutputSchema());
		} else {
			ISink<?> sink = (ISink<?>)operator;
			Collection<?> subs = sink.getSubscribedToSource();
			for( Object sub : subs ) {
				PhysicalSubscription<ISource<In>> physSub = (PhysicalSubscription<ISource<In>>)sub;
				physSub.getTarget().disconnectSink(this, operatorPortMap.get(operator), 0, physSub.getTarget().getOutputSchema());
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
	
	
	@SuppressWarnings("unchecked")
	private static <In> List<ISubscription<? extends ISource<In>>> determineSubscriptions(Collection<IPhysicalOperator> operators) {
		List<ISubscription<? extends ISource<In>>> subscriptions = Lists.newLinkedList();
		for (IPhysicalOperator operator : operators) {
			subscriptions.addAll((Collection<? extends ISubscription<? extends ISource<In>>>) determineSubscriptions(operator));
		}
		return subscriptions;
	}

	@SuppressWarnings("unchecked")
	private static <In> List<ISubscription<? extends ISource<In>>> determineSubscriptions(IPhysicalOperator operator) {
		Preconditions.checkArgument(operator.isSink() || operator.isSource(), "Operator must be sink and/or source!");

		List<ISubscription<? extends ISource<In>>> subs = Lists.newLinkedList();

		if (operator.isSource()) {
			subs.add((ISubscription<? extends ISource<In>>) new PhysicalSubscription<ISource<?>>((ISource<?>) operator, 0, 0, operator.getOutputSchema()));
		} else {
			Collection<?> subscriptions = ((ISink<?>) operator).getSubscribedToSource();

			for (Object subscription : subscriptions) {
				PhysicalSubscription<ISource<?>> sub = (PhysicalSubscription<ISource<?>>) subscription;
				subs.add((ISubscription<? extends ISource<In>>) new PhysicalSubscription<ISource<?>>(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema()));
			}
		}

		return subs;
	}
	
}
