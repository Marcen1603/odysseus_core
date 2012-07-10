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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.MarkerByteBufferHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

@Deprecated
public class MarkerByteBufferReceiverPO<W> extends
		ReceiverPO<ByteBuffer,W> {

	public MarkerByteBufferReceiverPO(IObjectHandler<W> objectHandler,
			IAccessConnectionHandler<ByteBuffer> accessHandler, byte start, byte end) {
		super(objectHandler, new MarkerByteBufferHandler<W>(start, end), accessHandler);
	}

	public MarkerByteBufferReceiverPO(IObjectHandler<W> objectHandler,
			IAccessConnectionHandler<ByteBuffer> accessHandler, byte marker) {
		super(objectHandler, new MarkerByteBufferHandler<W>(marker, marker), accessHandler);
	}

	public MarkerByteBufferReceiverPO(
			MarkerByteBufferReceiverPO<W> byteBufferReceiverPO) {
		super(byteBufferReceiverPO);
	}


	@Override
	public AbstractSource<W> clone() {
		return new MarkerByteBufferReceiverPO<W>(this);
	}

}
