package de.uniol.inf.is.odysseus.net.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class InetAddressUtil {

	private static final Logger LOG = LoggerFactory.getLogger(InetAddressUtil.class);
	
	// this works even with linux
	public static Optional<String> getRealInetAddress() {
		InetAddress inetAddress = getInetAddress();
		if( inetAddress != null ) {
			return Optional.fromNullable(inetAddress.getHostAddress());
		}
		
		return Optional.absent();
	}

	public static Optional<String> getHostName() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			if( inetAddress != null ) {
				return Optional.fromNullable(inetAddress.getHostName());
			}
		} catch (UnknownHostException e) {
			LOG.error("Could not determine hostname", e);
		}
		
		return Optional.absent();
	}
	
	public static String generateName() {
		Optional<String> optName = getHostName();
		
		if( optName.isPresent() ) {
			return optName.get();
		}
		
		Optional<String> optAddress = getRealInetAddress();
		if( optAddress.isPresent() ) {
			return optAddress.get();
		}
		
		return "OdysseusNode";
	}
	
	public static String replaceWithIPAddressIfNeeded( String text ) {
		if( !Strings.isNullOrEmpty(text) && text.equalsIgnoreCase("ip_address")) {
			return generateName();
		}
		
		return text;
	}
	
	private static InetAddress getInetAddress() {
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
			        	return current_addr;
			        }
			    }
			}
		} catch( Throwable t ) {
			LOG.error("Could not determine ip-address", t);
		}
		
		return null;
	}
}
