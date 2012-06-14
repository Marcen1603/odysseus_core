package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.nio.ByteOrder;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandlerListener;

abstract public class AbstractByteBufferHandler<T> extends AbstractProtocolHandler<T> implements ITransportHandlerListener{
	
	protected ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

	public void setByteOrder(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}
	
	protected void setByteOrder(String byteOrderTxt) {
		if ("LITTLE_ENDIAN".equalsIgnoreCase(byteOrderTxt)){
			byteOrder = ByteOrder.LITTLE_ENDIAN;
		}else{
			byteOrder = ByteOrder.BIG_ENDIAN;
		}
	}
}
