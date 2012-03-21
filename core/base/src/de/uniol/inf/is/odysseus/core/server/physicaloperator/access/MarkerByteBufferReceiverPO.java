package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

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
