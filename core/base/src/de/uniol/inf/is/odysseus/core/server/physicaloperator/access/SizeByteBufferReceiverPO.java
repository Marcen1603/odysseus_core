package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

@Deprecated
public class SizeByteBufferReceiverPO<W> extends
		ByteBufferReceiverPO<W> {

	public SizeByteBufferReceiverPO(IObjectHandler<W> objectHandler,
			IAccessConnection accessHandler) {
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
