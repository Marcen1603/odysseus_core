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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SocketSinkPO extends AbstractSink<IStreamObject<?>> {

	// static private Logger logger =
	// LoggerFactory.getLogger(SocketSinkPO.class);

	public List<ISinkStreamHandler> subscribe = new ArrayList<ISinkStreamHandler>();
	private ISinkConnection listener;
	private boolean isStarted;
	private IObjectHandler objectHandler = null;

	public SocketSinkPO(int serverPort, String host, ISinkStreamHandlerBuilder sinkStreamHandlerBuilder, boolean useNIO, boolean loginNeeded, IObjectHandler objectHandler, boolean push) {
		if (push) {
			listener = new SinkConnectionConnector(serverPort, host, sinkStreamHandlerBuilder, subscribe, useNIO, loginNeeded);
		} else {
			listener = new SinkConnectionListener(serverPort, sinkStreamHandlerBuilder, subscribe, useNIO, loginNeeded);
		}
		if (objectHandler == null) {
			throw new IllegalArgumentException("ObjectHandler cannot be null!");
		}
		this.objectHandler = objectHandler;
	}

	public SocketSinkPO(SocketSinkPO socketSinkPO) {
		throw new RuntimeException("Clone not supported");
	}
	
	public void startListening() {
		if( !isStarted ) {
			listener.start();
			isStarted = true;
		}
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
			Object toTransfer = object;
			if (objectHandler != null) {
				objectHandler.put(object);
				toTransfer = objectHandler.getByteBuffer();
			}

			Iterator<ISinkStreamHandler> iter = subscribe.iterator();
			while (iter.hasNext()) {
				ISinkStreamHandler sh = iter.next();
				try {
					sh.transfer(toTransfer);
				} catch (IOException e) {
					e.getMessage();
					iter.remove();
				}
			}
		}

	}

	protected void process_done() {
		synchronized (subscribe) {
			Iterator<ISinkStreamHandler> iter = subscribe.iterator();
			while (iter.hasNext()) {
				ISinkStreamHandler sh = iter.next();
				try {
					sh.done();
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
		return super.toString()+" "+listener.toString();
	}
}
