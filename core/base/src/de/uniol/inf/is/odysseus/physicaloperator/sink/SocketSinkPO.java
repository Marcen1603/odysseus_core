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
package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.access.IObjectHandler;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SocketSinkPO extends AbstractSink<Object> {

	static private Logger logger = LoggerFactory.getLogger(SocketSinkPO.class);
	
	public List<ISinkStreamHandler> subscribe = new ArrayList<ISinkStreamHandler>();
	private SinkConnectionListener listener;
	private IObjectHandler objectHandler = null;

	public SocketSinkPO(int serverPort, ISinkStreamHandlerBuilder sinkStreamHandlerBuilder, boolean useNIO, IObjectHandler objectHandler) {
		listener = new SinkConnectionListener(serverPort, sinkStreamHandlerBuilder, subscribe, useNIO);
		this.objectHandler = objectHandler;
		listener.start();
	}
	
	public SocketSinkPO(SocketSinkPO socketSinkPO) {
		throw new RuntimeException("Clone not supported");
	}

	
	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		synchronized (subscribe) {
			Iterator<ISinkStreamHandler> iter = subscribe.iterator();
			Object toTransfer = object;
			if (objectHandler != null){
				objectHandler.put(object);
				toTransfer = objectHandler.getByteBuffer();
			}
			while (iter.hasNext()){
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
			while (iter.hasNext()){
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
	public void processPunctuation(PointInTime timestamp, int port) {
		throw new RuntimeException("process punctuation not implemented");
		// TODO: How to implement punctuations? New Concepts on client side needed to detect punctuations 
	}

}
