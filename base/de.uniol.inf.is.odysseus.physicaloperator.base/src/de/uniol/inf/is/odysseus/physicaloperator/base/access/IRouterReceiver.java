package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.nio.ByteBuffer;

public interface IRouterReceiver {
	public void process(ByteBuffer buffer);
	public void done();
}
