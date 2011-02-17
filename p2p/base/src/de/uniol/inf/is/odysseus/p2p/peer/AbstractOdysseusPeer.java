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
package de.uniol.inf.is.odysseus.p2p.peer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.collection.Pair;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerFactory;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

/**
 * 
 * 
 * @author Mart Koehler, Marco Grawunder
 * 
 */
public abstract class AbstractOdysseusPeer implements IOdysseusPeer {

	static private Logger logger = LoggerFactory
			.getLogger(AbstractOdysseusPeer.class);
	private HashMap<String, Pair<P2PQuery, IExecutionListener>> queryList;
	private List<IExecutionHandler<?>> executionHandler;
	private IMessageSender<?, ?, ?> messageSender;
	final private ISocketServerListener socketServerListener;
	private IExecutionListenerFactory executionListenerFactory;
	final private ILogListener log;
	
	String name;

	public AbstractOdysseusPeer(ISocketServerListener socketServerListener, ILogListener log) {
		this.queryList = new HashMap<String, Pair<P2PQuery, IExecutionListener>>();
		this.executionHandler = new ArrayList<IExecutionHandler<?>>();
		this.socketServerListener = socketServerListener;
		this.log = log;
	}
	
	public ILogListener getLog() {
		return log;
	}

	@Override
	public abstract void startPeer();

	@Override
	public abstract void stopPeer();

	@Override
	public abstract void initLocalMessageHandler();

	@Override
	public abstract void initMessageSender();

	@Override
	public abstract void initLocalExecutionHandler();

	@Override
	public abstract Object getServerResponseAddress();

	@Override
	public void bindExecutionListenerFactory(IExecutionListenerFactory factory) {
		logger.info("Binding ExecutionListenerFactory: " + factory.getName());
		this.executionListenerFactory = factory;
	}

	@Override
	public void unbindExecutionListenerFactory(IExecutionListenerFactory factory) {
		if (this.executionListenerFactory == factory) {
			this.executionListenerFactory = null;
		}
	}

	@Override
	public void bindExecutionHandler(IExecutionHandler<?> handler) {
		logger.info("Binding Execution Handler: " + handler.getName());
		if (handler.getPeer() == null) {
			handler.setPeer(this);
		}
		this.executionHandler.add(handler);
	}

	@Override
	public void unbindExecutionHandler(IExecutionHandler<?> handler) {
		if (this.executionHandler.contains(handler)) {
			this.executionHandler.remove(handler);
		}
	}

	@Override
	public synchronized void registerMessageHandler(
			IMessageHandler messageHandler) {
		getSocketServerListener().registerMessageHandler(messageHandler);
	}

	@Override
	public synchronized void registerMessageHandler(
			List<IMessageHandler> messageHandler) {
		getSocketServerListener().registerMessageHandler(messageHandler);
	}

	protected ISocketServerListener getSocketServerListener() {
		return this.socketServerListener;
	}

	@Override
	public P2PQuery getQuery(String queryID) {
		synchronized (queryList) {
			Pair<P2PQuery, IExecutionListener> q = queryList.get(queryID);
			return q != null ? q.getE1() : null;
		}
	}
	
	@Override
	public Set<String> getQueryIds(){
		return Collections.unmodifiableSet(queryList.keySet());
	}
	
	@Override
	public int getQueryCount() {
		synchronized (queryList) {
			return queryList.size();
		}
	}

	@Override
	public boolean hasQuery(String queryId) {
		synchronized (queryList) {
			return queryList.containsKey(queryId);
		}
	}

	@Override
	public IExecutionListener getListenerForQuery(String queryID) {
		synchronized (queryList) {
			Pair<P2PQuery, IExecutionListener> q = queryList.get(queryID);
			return q != null ? q.getE2() : null;
		}
	}

	@Override
	public int getQueryCount(Lifecycle lifecycle) {
		int count = 0;
		synchronized (queryList) {
			for (Entry<String, Pair<P2PQuery, IExecutionListener>> q : queryList
					.entrySet()) {
				if (q.getValue().getE1().getStatus() == lifecycle) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public int getQueryCount(List<Lifecycle> lifecycle) {
		int count = 0;
		synchronized (queryList) {
			for (Entry<String, Pair<P2PQuery, IExecutionListener>> q : queryList
					.entrySet()) {
				if (lifecycle.contains(q.getValue().getE1().getStatus())) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public void addQuery(P2PQuery query) {
		synchronized (queryList) {
			// Fuer alle Peers, welche die Ausfuehrungsumgebung nutzen
			if (getExecutionListenerFactory() != null) {
				P2PQuery actualQuery = getQuery(query.getId());

				if (actualQuery != null) {
					actualQuery.updateWith(query);
				} else {
					List<IExecutionHandler<?>> executionHandlerCopy = new ArrayList<IExecutionHandler<?>>();
					for (IExecutionHandler<?> handler : getExecutionHandler()) {
						executionHandlerCopy.add(handler.clone());
					}

					IExecutionListener listener = getExecutionListenerFactory()
							.getNewInstance(query, executionHandlerCopy);
					queryList.put(query.getId(),
							new Pair<P2PQuery, IExecutionListener>(query,
									listener));
				}
			}
			// Für alle Peers bspw. Thin-Peer, welche nicht aktiv an der
			// Anfrageausführung mitwirken und daher nur die Anfrage intern in
			// einer Liste verwalten möchten
			// Kann zu Fehlern führen, wenn man nicht beachtet, dass value null
			// ist.
			else {
				if (!queryList.containsKey(query.getId())) {
					queryList
							.put(query.getId(),
									new Pair<P2PQuery, IExecutionListener>(
											query, null));
				}
			}
		}
	}

	protected IExecutionListenerFactory getExecutionListenerFactory() {
		return this.executionListenerFactory;
	}

	@Override
	public void removeQuery(String queryId) {
		synchronized (queryList) {
			queryList.remove(queryId);
		}
	}

	@Override
	public void deregisterMessageHandler(IMessageHandler messageHandler) {
		getSocketServerListener().deregisterMessageHandler(messageHandler);
	}

	protected synchronized Collection<IMessageHandler> getMessageHandlerList() {
		return getSocketServerListener().getMessageHandler();
	}

	protected synchronized List<IExecutionHandler<?>> getExecutionHandler() {
		return this.executionHandler;
	}

	@Override
	public IMessageSender<?, ?, ?> getMessageSender() {
		return messageSender;
	}

	protected void setMessageSender(IMessageSender<?, ?, ?> messageSender) {
		this.messageSender = messageSender;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
