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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.sink;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SocketSinkPO extends AbstractSink<IStreamObject<?>> {

	 static private Logger logger =	 LoggerFactory.getLogger(SocketSinkPO.class);

	final private Map<Integer, ISinkStreamHandler> subscribe = new HashMap<>();
	// private List<ISinkStreamHandler> subscribe = new LinkedList<>();
	final private ISinkConnection listener;
	private boolean isStarted;
	final private IObjectHandler objectHandler;
	final private boolean withMetadata;
	private PhysicalSubscription<ISource<? extends IStreamObject<?>>> subscription;

	public SocketSinkPO(int serverPort, String host,
			ISinkStreamHandlerBuilder sinkStreamHandlerBuilder, boolean useNIO,
			boolean loginNeeded, boolean loginWithSessionId,
			IObjectHandler objectHandler, boolean push, boolean withMetadata) {
		if (push) {
			listener = new SinkConnectionConnector(serverPort, host,
					sinkStreamHandlerBuilder, this, useNIO, loginNeeded);
		} else {
			listener = new SinkConnectionListener(serverPort,
					sinkStreamHandlerBuilder, this, useNIO, loginNeeded,
					loginWithSessionId);
		}
		if (objectHandler == null) {
			throw new IllegalArgumentException("ObjectHandler cannot be null!");
		}
		this.objectHandler = objectHandler;
		this.withMetadata = withMetadata;
	}

	public SocketSinkPO(IServerSocketProvider serverSocketProvider,
			ISinkStreamHandlerBuilder sinkStreamHandlerBuilder,
			boolean loginNeeded, boolean loginWithSessionId,
			IObjectHandler objectHandler, boolean withMetadata) {
		if (objectHandler == null) {
			throw new IllegalArgumentException("ObjectHandler cannot be null!");
		}
		listener = new SinkConnectionListener(serverSocketProvider,
				sinkStreamHandlerBuilder, this, loginNeeded, loginWithSessionId);
		this.objectHandler = objectHandler;
		this.withMetadata = withMetadata;
	}

	public SocketSinkPO(SocketSinkPO socketSinkPO) {
		throw new RuntimeException("Clone not supported");
	}

	public void startListening() {
		if (!isStarted) {
			listener.start();
			isStarted = true;
		}
	}

	public void addSubscriber(ISinkStreamHandler temp) {
		Integer port = reserverNextFreePort();
		
		if (subscription == null) {
			List<PhysicalSubscription<ISource<? extends IStreamObject<?>>>> s = getSubscribedToSource();
			if (s.size() != 1) {
				logger.error("MORE THAN ONE SUBSCRIPTION");
			}
			subscription = s.get(0);
		} else {
			Objects.requireNonNull(subscription);
			setInputPortCount(getMaxPort()+1);
			subscription.getTarget().connectSink(this, port,
					subscription.getSourceOutPort(), subscription.getSchema());
		}
		subscribe.put(port, temp);
	}

	private int reserverNextFreePort(){
		for (int i=0;i<Integer.MAX_VALUE;i++){
			if (!subscribe.containsKey(i)){
				return i;
			}
		}
		throw new RuntimeException("No more client connections possible!");
	}
	
	private int getMaxPort(){
		int max = 0;
		for ( Entry<Integer, ISinkStreamHandler> s:subscribe.entrySet()){
			if (s.getKey() > max){
				max = s.getKey();
			}
		}
		return max;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		startListening();
	}

	@Override
	protected void process_close() {
		super.process_close();
	}

	@Override
	protected void process_next(IStreamObject<?> object, int port) {
		synchronized (subscribe) {
			if (subscribe.size() > 0) {
				Object toTransfer = object;
				if (objectHandler != null) {
					objectHandler.put(object, withMetadata);
					toTransfer = objectHandler.getByteBuffer();
				}
				try {
					subscribe.get(port).transfer(toTransfer);
				} catch (IOException e) {
					e.getMessage();
					logger.debug(e.getMessage());
					subscription.getTarget().disconnectSink(this,port,subscription.getSourceOutPort(), subscription.getSchema());
					subscribe.remove(port);
				}
			}
		}

	}

	protected void process_done() {
		synchronized (subscribe) {
			Iterator<Entry<Integer, ISinkStreamHandler>> iter = subscribe.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<Integer, ISinkStreamHandler> sh = iter.next();
				try {
					sh.getValue().done();
				} catch (IOException e) {
					e.printStackTrace();
				}
				iter.remove();
			}
		}
	}

	@Override
	public SocketSinkPO clone() {
		return new SocketSinkPO(this);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// IGNORE
		// throw new RuntimeException("process punctuation not implemented");
		// TODO: How to implement punctuations? New Concepts on client side
		// needed to detect punctuations
	}

	@Override
	public String toString() {
		return super.toString() + " " + listener.toString();
	}

	public void addAllowedSessionId(String securityToken) {
		listener.addSessionId(securityToken);
	}

	public void removeAllowedSessionId(String securityToken) {
		listener.removeSessionId(securityToken);
	}

	@Override
	public void setInputPortCount(int ports) {
		super.setInputPortCount(ports);
	}
}
