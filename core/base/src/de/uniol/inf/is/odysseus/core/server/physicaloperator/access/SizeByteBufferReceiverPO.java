package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.server.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.server.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.server.objecthandler.SizeByteBufferHandler;
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
