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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class MarkerByteBufferHandler<T> extends AbstractByteBufferHandler<T>{

	private ByteBufferHandler<T> objectHandler;
	private byte start;
	private byte end;
	
	@Override
	public void open() throws UnknownHostException, IOException {	
		getTransportHandler().open();
	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
	}

	@Override
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		MarkerByteBufferHandler<T> instance = new MarkerByteBufferHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);
		instance.objectHandler = new ByteBufferHandler<T>(dataHandler);
		transportHandler.addListener(instance);
		instance.start = Byte.parseByte(options.get("start"));
		instance.end = Byte.parseByte(options.get("end"));
		instance.setByteOrder(options.get("byteorder"));
		return instance;
	}

	@Override
	public String getName() {
		return "MarkerByteBuffer";
	}

	@Override
	public void onConnect(ITransportHandler caller) {
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
	}

	@Override
	public void process(ByteBuffer message) {
		try {
			int pos = 0;
			while (message.remaining() > 0) {
				byte value = message.get();
				if (value == end) {
					int endPosition = message.position() - 1;
					message.position(pos);
					objectHandler.put(message, endPosition - pos);
					message.position(endPosition + 1);
					pos = message.position();
					getTransfer().transfer(objectHandler.create());
				}
				if (value == start) {
					objectHandler.clear();
					pos = message.position();
				}
			}
			if (pos >= 0) {
				message.position(pos);
				objectHandler.put(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BufferUnderflowException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
