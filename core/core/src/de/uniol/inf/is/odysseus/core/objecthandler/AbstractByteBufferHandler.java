package de.uniol.inf.is.odysseus.core.objecthandler;

import java.nio.ByteOrder;

import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;

abstract public class AbstractByteBufferHandler<R,W> implements IInputDataHandler<R,W> {

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
	
	@Override
    public abstract IInputDataHandler<R,W> clone();
	
}
