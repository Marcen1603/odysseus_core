package de.uniol.inf.is.odysseus.wrapper.iec62056;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import org.openmuc.jdlms.AuthenticationMechanism;
import org.openmuc.jdlms.LogicalDevice;
import org.openmuc.jdlms.SecuritySuite;
import org.openmuc.jdlms.SecuritySuite.EncryptionMechanism;
import org.openmuc.jdlms.datatypes.DataObject;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {

		
		InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
		SecuritySuite securitySuite = SecuritySuite.builder()
		        .setPassword("Password".getBytes(StandardCharsets.US_ASCII))
		        .setAuthenticationMechanism(AuthenticationMechanism.LOW)
		        .setEncryptionMechanism(EncryptionMechanism.NONE)
		        .build();

		
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
