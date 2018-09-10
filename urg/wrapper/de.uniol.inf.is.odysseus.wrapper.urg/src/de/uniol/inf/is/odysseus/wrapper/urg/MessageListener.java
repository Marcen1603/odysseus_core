package de.uniol.inf.is.odysseus.wrapper.urg;

import java.nio.ByteBuffer;

public interface MessageListener {
	void messageReceived(ByteBuffer buffer);
}
