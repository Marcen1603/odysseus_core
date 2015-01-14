package de.uniol.inf.is.odysseus.p2p_new.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class InetAddressUtil {

	private static final Logger LOG = LoggerFactory.getLogger(InetAddressUtil.class);
	
	// this works even with linux
	public static Optional<String> getRealInetAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
			    NetworkInterface current = interfaces.nextElement();
			    if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
			    	continue;
			    }
			    
			    Enumeration<InetAddress> addresses = current.getInetAddresses();
			    while (addresses.hasMoreElements()){
			        InetAddress current_addr = addresses.nextElement();
			        if (current_addr.isLoopbackAddress()) {
			        	continue;
			        }
			        
			        if( current_addr instanceof Inet4Address ) {
			        	return Optional.of(current_addr.getHostAddress());
			        }
			    }
			}
		} catch( Throwable t ) {
			LOG.error("Could not determine ip-address", t);
		}
		
		return Optional.absent();
	}
	
	public static String replaceWithIPAddressIfNeeded( String text ) {
		if( !Strings.isNullOrEmpty(text) && text.equalsIgnoreCase("ip_address")) {
			Optional<String> optAddress = getRealInetAddress();
			if( optAddress.isPresent() ) {
				return optAddress.get();
			}
		}
		
		return text;
	}
	
}
