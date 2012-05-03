package de.uniol.inf.is.odysseus.wrapper.protobuf.gc2012;

import com.google.protobuf.GeneratedMessage;

import de.uniol.inf.is.odysseus.wrapper.protobuf.IProtobufDatatypeProvider;
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
