package de.uniol.inf.is.odysseus.wrapper.google.protobuf.grandchallenge;

import com.google.protobuf.GeneratedMessage;

import de.uniol.inf.is.odysseus.wrapper.google.protobuf.base.IProtobufDatatypeProvider;
import debs.challenge.msg.CManufacturingMessages.CDataPoint;

public class CManufacturingMessageProvider implements IProtobufDatatypeProvider {

	@Override
	public String getName() {
		return CDataPoint.getDescriptor().getFullName();
	}

	@Override
	public GeneratedMessage getMessageType() {
		return CDataPoint.getDefaultInstance();
	}

}
