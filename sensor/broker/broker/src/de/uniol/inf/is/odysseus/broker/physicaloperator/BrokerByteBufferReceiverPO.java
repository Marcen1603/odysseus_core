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
package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ReceiverPO;

/**
 * The BrokerByteBufferReceiverPO is a physical source which is able to receive elements of type W.

 *
 * @param <W> the generic type
 */
@Deprecated
public class BrokerByteBufferReceiverPO<W> extends ReceiverPO<ByteBuffer, W> {
		
	/**
	 * Instantiates a new BrokerByteBufferReceiverPO.
	 *
	 * @param handler the handler which wraps an element
	 * @param host the host
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BrokerByteBufferReceiverPO(IObjectHandler<W> handler, IAccessConnectionHandler<ByteBuffer> accessHandler) throws IOException {
		super(handler, new BrokerByteBufferHandler<W>(), accessHandler);
	}

	/**
	 * Instantiates a new BrokerByteBufferReceiverPO.
	 *
	 * @param byteBufferReceiverPO the original to copy from
	 * @ 
	 */
	public BrokerByteBufferReceiverPO(BrokerByteBufferReceiverPO<W> byteBufferReceiverPO)  {
		super(byteBufferReceiverPO);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#clone()
	 */
	@Override
	public BrokerByteBufferReceiverPO<W> clone()  {
		return new BrokerByteBufferReceiverPO<W>(this);
	}


}
