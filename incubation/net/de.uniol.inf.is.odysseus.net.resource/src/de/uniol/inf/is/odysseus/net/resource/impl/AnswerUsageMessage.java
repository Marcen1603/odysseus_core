package de.uniol.inf.is.odysseus.net.resource.impl;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.resource.IResourceUsage;

public class AnswerUsageMessage implements IMessage {

	private IResourceUsage resourceUsage;
	
	public AnswerUsageMessage() {
	}
	
	public AnswerUsageMessage( IResourceUsage resourceUsage ) {
		this.resourceUsage = resourceUsage;
	}
	
	@Override
	public byte[] toBytes() {
		return ResourceUsageBytesConverter.toByteBuffer(resourceUsage).array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		resourceUsage = ResourceUsageBytesConverter.toResourceUsage(bb);
	}
	
	public IResourceUsage getResourceUsage() {
		return resourceUsage;
	}
}
