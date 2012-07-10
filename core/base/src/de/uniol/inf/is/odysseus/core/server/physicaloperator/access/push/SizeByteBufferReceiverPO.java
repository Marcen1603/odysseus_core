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
import de.uniol.inf.is.odysseus.core.objecthandler.SizeByteBufferHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

@Deprecated
public class SizeByteBufferReceiverPO<W> extends
		ReceiverPO<ByteBuffer,W> {

	public SizeByteBufferReceiverPO(IObjectHandler<W> objectHandler,
			IAccessConnectionHandler<ByteBuffer> accessHandler) {
		super(objectHandler, new SizeByteBufferHandler<W>(), accessHandler);
	}

	public SizeByteBufferReceiverPO(
			SizeByteBufferReceiverPO<W> byteBufferReceiverPO) {
		super(byteBufferReceiverPO);
	}

	@Override
	public AbstractSource<W> clone() {
		return new SizeByteBufferReceiverPO<W>(this);
	}

}
