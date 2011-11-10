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

public class ByteBufferReceiverPO<W> extends AbstractSource<W> implements IRouterReceiver {

	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ByteBufferReceiverPO.class);
		}
		return _logger;
	}

	private IObjectHandler<W> objectHandler;
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private Router router;
	private String host;
	private int port;
	boolean opened;
	private boolean autoReconnect = false;
	private long waitingForNextReconnect = 0;
	private int tries = 0;	

	public ByteBufferReceiverPO(IObjectHandler<W> handler, String host, int port) throws IOException {
		super();
		this.objectHandler = handler;
		router = Router.getInstance();
		router.addConnectionListener(this);
		this.host = host;
		this.port = port;
		setName("ByteBufferReceiverPO " + host + ":" + port);
		this.opened = false;
		this.autoReconnect = false;
	}

	@SuppressWarnings("unchecked")
	public ByteBufferReceiverPO(ByteBufferReceiverPO<W> byteBufferReceiverPO) {
		super();
		objectHandler = (IObjectHandler<W>) byteBufferReceiverPO.objectHandler.clone();
		size = byteBufferReceiverPO.size;
		currentSize = byteBufferReceiverPO.currentSize;
		router = byteBufferReceiverPO.router;
		host = byteBufferReceiverPO.host;
		port = byteBufferReceiverPO.port;
		opened = byteBufferReceiverPO.opened;
		autoReconnect = byteBufferReceiverPO.autoReconnect;
	}

	public boolean isAutoReconnectEnabled() {
		return autoReconnect;
	}

	public void setAutoReconnectEnabled(boolean autoReconnect) {
		this.autoReconnect = autoReconnect;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		getLogger().debug("Process_open");
		if (!opened) {
			try {
				sizeBuffer.clear();
				size = -1;
				objectHandler.clear();
				router.connectToServer(this, host, port);
				opened = true;
			} catch (Exception e) {
				throw new OpenFailedException(e);
			}
		}
	}

	@Override
	protected void process_close() {
		getLogger().debug("Process_close");
		if (opened) {
			try {
				opened = false; // Do not read any data anymore
				router.disconnectFromServer(this);
				sizeBuffer.clear();
				size = -1;
				objectHandler.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void done() {
		propagateDone();
	}

	@Override
	public synchronized void process(ByteBuffer buffer) {
		if (opened) {
			try {
				while (buffer.remaining() > 0) {
					
					// size ist dann ungleich -1 wenn die vollständige 
					// Größeninformation übertragen wird
					// Ansonsten schon mal soweit einlesen
					if (size == -1) {
						while (sizeBuffer.position() < 4 && buffer.remaining() > 0) {
							sizeBuffer.put(buffer.get());
						}
						// Wenn alles übertragen
						if (sizeBuffer.position() == 4) {
							sizeBuffer.flip();
							size = sizeBuffer.getInt();
						}
					}
					// Es kann auch direkt nach der size noch was im Puffer
					// sein!
					// Und Size kann gesetzt worden sein
					if (size != -1) {
						// Ist das was dazukommt kleiner als die finale Größe?
						if (currentSize + buffer.remaining() < size) {
							currentSize = currentSize + buffer.remaining();
							objectHandler.put(buffer);
						} else {
							// Splitten (wir sind mitten in einem Objekt
							// 1. alles bis zur Grenze dem Handler übergeben
							// logger.debug(" "+(size-currentSize));
							objectHandler.put(buffer, size - currentSize);
							// 2. das fertige Objekt weiterleiten
							transfer();
							// Dann in der While-Schleife weiterverarbeiten
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				reconnect();
			}
		}
	}

	private void reconnect() {
		if (this.opened && this.autoReconnect) {			
			if(waitingForNextReconnect<0){
				waitingForNextReconnect = 0;
			}
			getLogger().info("Reconnect in " + waitingForNextReconnect + " ms ...");
			try {
				Thread.sleep(waitingForNextReconnect);
			} catch (InterruptedException e1) {
			}
			getLogger().info("Trying to reconnect...");
			process_close();
			try {
				process_open();
			} catch (OpenFailedException e) {				
				if (e.getCause() instanceof IOException) {
					reconnect();
				}
				e.printStackTrace();
			}
			// failed again... increase waittime
			waitingForNextReconnect = ((tries*tries)*100);
			tries++;
		}
	}

	private synchronized void transfer() throws IOException, ClassNotFoundException {

		W toTrans = null;
		try {
			toTrans = objectHandler.create();
		} catch (Exception e) {
			getLogger().error(e.getMessage() + ". Terminating Processing ...");
			e.printStackTrace();
			process_done();
			process_close();
			return;
		}
		// logger.debug("Transfer "+toTrans);
		transfer(toTrans);
		size = -1;
		sizeBuffer.clear();
		currentSize = 0;
		tries = 0;
	}

	@Override
	public ByteBufferReceiverPO<W> clone() {
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
		if (!(ipo instanceof ByteBufferReceiverPO)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		ByteBufferReceiverPO bbrpo = (ByteBufferReceiverPO) ipo;
		if (this.objectHandler.equals(bbrpo.objectHandler) && this.port == bbrpo.port && this.host.equals(bbrpo.host)) {
			return true;
		}
		return false;
	}

	@Override
	public void notify(Router router, ConnectionMessageReason reason) {
		switch (reason) {
		case ConnectionAbort:
			reconnect();
			break;
		case ConnectionClosed:
			break;
		case ConnectionRefused:
			reconnect();
			break;
		case ConnectionOpened:
			break;
		default:
			break;
		}

	}
}
