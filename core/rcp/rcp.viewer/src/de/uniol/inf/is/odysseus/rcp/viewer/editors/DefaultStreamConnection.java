/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.viewer.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamElementListener;

public class DefaultStreamConnection<In> extends AbstractSink<In> implements
		IStreamConnection<In> {

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
		// logger.debug( "StreamManager created" );
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
	protected void process_next(In element, int port, boolean isReadOnly) {
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
				// logger.error( "Bei der Verarbeitung des Datenelements " +
				// element.toString() + " trat eine Exception auf!", ex);
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
					process_next(collectedObjects.get(i),
							collectedPorts.get(i), true);
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

	public void notifyListenersPunctuation(PointInTime point, int port) {
		synchronized (listeners) {
			for (IStreamElementListener<In> l : listeners) {
				if (l != null)
					l.punctuationElementRecieved(point, port);
			}
		}
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}
}
