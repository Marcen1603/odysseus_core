package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.nio.ByteBuffer;

public interface IByteBufferHandler<T> {

	void init();
	void done();
	void process(ByteBuffer buffer, IObjectHandler<T> objectHandler, IAccessConnection accessHandler, ITransferHandler transferHandler);
	IByteBufferHandler<T> clone();
}
