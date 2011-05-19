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
package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

public class ByteBufferReceiverPO<W> extends AbstractSource<W> implements
		IRouterReceiver {
	
	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ByteBufferReceiverPO.class);
		}
		return _logger;
	}

	private IObjectHandler<W> handler;
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private Router router;
	private String host;
	private int port;
	boolean opened;

	public ByteBufferReceiverPO(IObjectHandler<W> handler, String host, int port)
			throws IOException {
		super();
		this.handler = handler;
		router = Router.getInstance();
		this.host = host;
		this.port = port;
		setName("ByteBufferReceiverPO "+host+":"+port);
		this.opened = false;
	}

	@SuppressWarnings("unchecked")
	public ByteBufferReceiverPO(ByteBufferReceiverPO<W> byteBufferReceiverPO) {
		super();
		handler = (IObjectHandler<W>) byteBufferReceiverPO.handler.clone();
		size = byteBufferReceiverPO.size;
		currentSize = byteBufferReceiverPO.currentSize;
		router = byteBufferReceiverPO.router;
		host = byteBufferReceiverPO.host;
		port = byteBufferReceiverPO.port;
		opened = byteBufferReceiverPO.opened;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		getLogger().debug("Process_open");
		if (!opened) {
			try {
				router.connectToServer(this, host, port);
				opened = true;
			} catch (Exception e) {
				throw new OpenFailedException(e);
			}
		}
	}
	
	@Override
	protected void process_close(){
		getLogger().debug("Process_close");
		if (opened){
			try {
				router.disconnectFromServer(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		opened = false;
	}

	@Override
	public void done() {
		propagateDone();
	}

	@Override
	public void process(ByteBuffer buffer) {
		try {
			while (buffer.remaining() > 0) {

				// logger.debug("size "+size+" remaining "+buffer.remaining()+" currentSize "+currentSize);

				// ACHTUNG! ES KANN SEIN, DASS "SIZE" NOCH NICHT VOLLST�NDIG
				// �BERTRAGEN IST
				// Neues Object?
				// TODO: Reihenfolge mit der die Size kodiert wird festlegen!!!
				if (size == -1) {
					while (sizeBuffer.position() < 4 && buffer.remaining() > 0) {
						sizeBuffer.put(buffer.get());
					}
					// Wenn alles �bertragen
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
						// logger.debug("NEW OBJEKT STARTED WITH SIZE "+size);
					}
				}
				// Es kann auch direkt nach der size noch was im Puffer sein!
				// Und Size kann gesetzt worden sein
				if (size != -1) {
					// Ist das was dazukommt kleiner als die finale Gr��e?
					if (currentSize + buffer.remaining() < size) {
						currentSize = currentSize + buffer.remaining();
						handler.put(buffer);
					} else {
						// Splitten (wir sind mitten in einem Objekt
						// 1. alles bis zur Grenze dem Handler �bergeben
						// logger.debug(" "+(size-currentSize));
						handler.put(buffer, size - currentSize);
						// 2. das fertige Objekt weiterleiten
						transfer();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void transfer() throws IOException,
			ClassNotFoundException {
		W toTrans = handler.create();
		//logger.debug("Transfer "+toTrans);
		transfer(toTrans);
		size = -1;
		sizeBuffer.clear();
		currentSize = 0;
	}

	@Override
	public ByteBufferReceiverPO<W> clone(){
		return new ByteBufferReceiverPO<W>(this);
	}

	@Override
	public String toString() {
		return super.toString() + " " + host + " " + port;
	}
	
	@Override
	public String getSourceName() {
		return toString();
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof ByteBufferReceiverPO)) {
			return false;
		}
		ByteBufferReceiverPO bbrpo = (ByteBufferReceiverPO) ipo;
		if(this.handler.equals(bbrpo.handler) && this.port == bbrpo.port && this.host.equals(bbrpo.host)) {
			return true;
		}
		return false;
	}
}
