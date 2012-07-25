/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.sick.physicaloperator.access;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.sick.SICKConstants;

/**
 * Protocol Handler for the SICK protocol supporting LMS100 and LMS151 laser
 * scanner
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SICKProtocolHandler<T> extends AbstractByteBufferHandler<T> {
	private static final Logger LOG = LoggerFactory
			.getLogger(SICKProtocolHandler.class);
	private final Charset charset = Charset.forName("utf-8");
	protected ByteBufferHandler<T> objectHandler;
	private byte start;
	private byte end;

	@Override
	public String getName() {
		return "SICK";
	}

	@Override
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		SICKProtocolHandler<T> instance = new SICKProtocolHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);
		instance.objectHandler = new ByteBufferHandler<T>(dataHandler);
		instance.start = SICKConstants.START;
		instance.end = SICKConstants.END;
		instance.setByteOrder(options.get("byteorder"));
		transportHandler.addListener(instance);
		return instance;
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		try {
			this.write(SICKConstants.START_SCAN.getBytes(charset));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void close() throws IOException {
		this.write(SICKConstants.STOP_SCAN.getBytes(charset));
		getTransportHandler().close();
	}

	@Override
	public void process(ByteBuffer message) {
		message.flip();
		int startPosition = 0;
		try {
			while (message.remaining() > 0) {
				byte value = message.get();
				if (value == end) {
					int endPosition = message.position() - 1;
					message.position(startPosition);
					objectHandler.put(message, endPosition - startPosition);
					message.position(endPosition + 1);
					startPosition = message.position();
					T object = objectHandler.create();
					if (object != null) {
						getTransfer().transfer(object);
					}
				}
				if (value == start) {
					objectHandler.clear();
					startPosition = message.position();
				}
			}
			if (startPosition >= 0) {
				message.position(startPosition);
				objectHandler.put(message);
				message.compact();
			}

		} catch (BufferUnderflowException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(byte[] message) throws IOException {
		try {
			final byte[] messageBuffer = new byte[message.length + 2];
			messageBuffer[0] = SICKConstants.START;
			System.arraycopy(message, 0, messageBuffer, 1, message.length);
			messageBuffer[messageBuffer.length - 1] = SICKConstants.END;
			getTransportHandler().write(messageBuffer);

			LOG.debug(String.format("SICK: Send message %s", message));
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
