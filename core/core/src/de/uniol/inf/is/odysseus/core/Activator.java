package de.uniol.inf.is.odysseus.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.CSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.LineProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.MarkerByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SizeByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.TextProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TcpSocketHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;
	
	
	public static BundleContext getBundleContext(){
		return bundleContext;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void start(BundleContext context) throws Exception {	
		bundleContext = context;
		
		ProtocolHandlerRegistry.register(new LineProtocolHandler());
		ProtocolHandlerRegistry.register(new CSVProtocolHandler());
		ProtocolHandlerRegistry.register(new SimpleCSVProtocolHandler());
		ProtocolHandlerRegistry.register(new TextProtocolHandler());
		ProtocolHandlerRegistry.register(new SizeByteBufferHandler());
		ProtocolHandlerRegistry.register(new MarkerByteBufferHandler());
		
		
		TransportHandlerRegistry.register(new TcpSocketHandler());
		TransportHandlerRegistry.register(new FileHandler());
		TransportHandlerRegistry.register(new NonBlockingTcpHandler());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		bundleContext = null;
	}

}
