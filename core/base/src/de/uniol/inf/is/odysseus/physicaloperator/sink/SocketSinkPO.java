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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;


public class SocketSinkPO<T> extends AbstractSink<T> {

	static private Logger logger = LoggerFactory.getLogger(SocketSinkPO.class);
	@SuppressWarnings("rawtypes")
	public List<IStreamHandler> subscribe = new ArrayList<IStreamHandler>();
	private ConnectionListener listener;

	public SocketSinkPO(int serverPort, IStreamHandlerFactory streamHandlerFactory) {
		listener = new ConnectionListener(serverPort, streamHandlerFactory, subscribe);
	}
	
	public SocketSinkPO(SocketSinkPO<T> socketSinkPO) {
		throw new RuntimeException("Clone not supported");
	}

	public void startListener(int port) {
		listener.start();
	}

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		synchronized (subscribe) {
			for (IStreamHandler<T> sh : subscribe) {
				sh.transfer(object);
			}
			if (subscribe.size() == 0) {
				logger.warn("No recever for object");
			}
		}

	}

	protected void process_done() {
		synchronized (subscribe) {
			for (IStreamHandler<T> sh : subscribe) {
				sh.done();
			}
		}
	}

	@Override
	public SocketSinkPO<T> clone() {
		return new SocketSinkPO<T>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		throw new RuntimeException("process punctuation not implemented");
		// TODO: How to implement punctuations? New Concepts on client side needed to detect punctuations 
	}

}
