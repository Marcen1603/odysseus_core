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
package de.uniol.inf.is.odysseus.rcp.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class DefaultStreamConnection<In> implements ISink<In>, IStreamConnection<In> {

	Logger logger = LoggerFactory.getLogger(DefaultStreamConnection.class);

	private List<ISubscription<? extends ISource<In>>> subscriptions;
	private boolean connected = false;
	private boolean enabled = false;

	private ArrayList<In> collectedObjects = new ArrayList<In>();
	private ArrayList<Integer> collectedPorts = new ArrayList<Integer>();

	private final Collection<IStreamElementListener<In>> listeners = new ArrayList<IStreamElementListener<In>>();
	private boolean hasExceptions = false;
	
	public DefaultStreamConnection(List<ISubscription<? extends ISource<In>>> subscriptions) {
		if (subscriptions == null || subscriptions.isEmpty())
			throw new IllegalArgumentException("subscriptions is null or empty!");

		this.subscriptions = subscriptions;
	}

	@Override
	public List<ISubscription<? extends ISource<In>>> getSubscriptions() {
		return subscriptions;
	}

	@Override
	public final void connect() {
		if (subscriptions != null) {
			for (ISubscription<? extends ISource<In>> s : subscriptions) {
				s.getTarget().connectSink(this, s.getSinkInPort(), s.getSourceOutPort(), s.getSchema());
			}
			connected = true;
		}

	}

	@Override
	public final void disconnect() {
		if (subscriptions != null) {
			for (ISubscription<? extends ISource<In>> s : subscriptions) {
				s.getTarget().disconnectSink(this, s.getSinkInPort(), s.getSourceOutPort(), s.getSchema());
			}
			connected = false;
		}
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

		
	@Override
	public void process(In element, int port) {
		logger.debug("Objekt:" + element.toString());
		if (hasExceptions)
			return;

		if (enabled) {
			// Objekt zwischenspeichern und nicht verarbeiten lassen
			synchronized (collectedObjects) {
				collectedObjects.add(element);
				collectedPorts.add(port);
			}

		} else {
			try {
				notifyListeners(element, port);
			} catch (Exception ex) {
				logger.error( "Bei der Verarbeitung des Datenelements " + element.toString() + " trat eine Exception auf!", ex);
				hasExceptions = true;
			}
		}
	}

	@Override
	public void disable() {
		enabled = true;
	}

	@Override
	public void enable() {
		enabled = false;
		if (collectedObjects.size() > 0) {
			synchronized (collectedObjects) {
				// gesammelte Daten nachträglich verarbeiten lassen
				for (int i = 0; i < collectedObjects.size(); i++) {
					process(collectedObjects.get(i),
							collectedPorts.get(i));
				}
				collectedObjects.clear();
				collectedPorts.clear();
			}
		}
	}

	@Override
	public boolean isEnabled() {
		return !enabled;
	}

	@Override
	public void addStreamElementListener(IStreamElementListener<In> listener) {
		if (listener == null)
			return;

		synchronized (listeners) {
			if (!listeners.contains(listener))
				listeners.add(listener);
		}
	}

	@Override
	public void notifyListeners(In element, int port) {
		synchronized (listeners) {
			for (IStreamElementListener<In> l : listeners) {
				if (l != null)
					l.streamElementRecieved(element, port);
			}
		}
	}

	@Override
	public void removeStreamElementListener(IStreamElementListener<In> listener) {
		if (listener == null)
			return;

		synchronized (listeners) {
			if (listeners.contains(listener))
				listeners.remove(listener);
		}

	}

	@Override
	public DefaultStreamConnection<In> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		notifyListenersPunctuation(timestamp, port);
	}

	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
	}

	public void notifyListenersPunctuation(PointInTime point, int port) {
		synchronized (listeners) {
			for (IStreamElementListener<In> l : listeners) {
				if (l != null)
					l.punctuationElementRecieved(point, port);
			}
		}
	}

	@Override
	public boolean isSource() {
		return false;
	}

	@Override
	public boolean isSink() {
		return true;
	}

	@Override
	public boolean isPipe() {
		return isSink() && isSource();
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}

	@Override
	public String getName() {
		return "DefaultStreamConnection";
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public SDFSchema getOutputSchema() {
		return getOutputSchema(0);
	}

	@Override
	public SDFSchema getOutputSchema(int port) {
		return null;
	}
	
	@Override
	final public Map<Integer, SDFSchema> getOutputSchemas() {
		return null;
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema) {
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema, int port) {
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	public void addOwner(IOperatorOwner owner) {		
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
	}

	@Override
	public void removeAllOwners() {
	}

	@Override
	public boolean isOwnedBy(IOperatorOwner owner) {
		return false;
	}

	@Override
	public boolean hasOwner() {
		return false;
	}

	@Override
	public List<IOperatorOwner> getOwner() {
		return Lists.newArrayList();
	}

	@Override
	public String getOwnerIDs() {
		return "";
	}

	@Override
	public Collection<String> getProvidedMonitoringData() {
		return Lists.newArrayList();
	}

	@Override
	public boolean providesMonitoringData(String type) {
		return false;
	}

	@Override
	public <T> IMonitoringData<T> getMonitoringData(String type) {
		return null;
	}

	@Override
	public void createAndAddMonitoringData(@SuppressWarnings("rawtypes") IPeriodicalMonitoringData item, long period) {
	}

	@Override
	public void addMonitoringData(String type, IMonitoringData<?> item) {
	}

	@Override
	public void removeMonitoringData(String type) {
	}

	@Override
	public void subscribe(IEventListener listener, IEventType type) {
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
	}

	@Override
	public void subscribeToAll(IEventListener listener) {
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
	}

	@Override
	public void fire(IEvent<?, ?> event) {
	}

	@Override
	public void subscribeToSource(ISource<? extends In> source, int sinkInPort, int sourceOutPort, SDFSchema schema) {
	}

	@Override
	public void unsubscribeFromSource(PhysicalSubscription<ISource<? extends In>> subscription) {
	}

	@Override
	public void unsubscribeFromAllSources() {
	}

	@Override
	public void unsubscribeFromSource(ISource<? extends In> source, int sinkInPort, int sourceOutPort, SDFSchema schema) {
	}

	@Override
	public Collection<PhysicalSubscription<ISource<? extends In>>> getSubscribedToSource() {
		return Lists.newArrayList();
	}

	@Override
	public PhysicalSubscription<ISource<? extends In>> getSubscribedToSource(int i) {
		return null;
	}

	@Override
	public void process(Collection<? extends In> object, int port) {
		for( In obj : object ) {
			process(obj, port);
		}
	}

	@Override
	public void done(int port) {
	}

	@Override
	public void open() throws OpenFailedException {
	}

	@Override
	public void close() {
	}
}
