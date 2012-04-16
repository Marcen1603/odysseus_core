package de.uniol.inf.is.odysseus.wrapper.google.protobuf.grandchallenge;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import de.uniol.inf.is.odysseus.wrapper.google.protobuf.base.ProtobufTypeRegistry;

import debs.challenge.msg.CManufacturingMessages.CDataPoint;

public class RegisterDatatype implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		ProtobufTypeRegistry.getInstance().registerMessageType(CDataPoint.getDescriptor()
				.getFullName(), CDataPoint.getDefaultInstance());
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
