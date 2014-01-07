package de.uniol.inf.is.odysseus.p2p_new.util.connect.udp;

import java.net.InetAddress;
import java.util.Objects;

class ConnectionData {
	
	private final InetAddress address;
	private final int port;
	
	public ConnectionData( InetAddress address, int port ) {
		this.address = address;
		this.port = port;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof ConnectionData)) {
			return false;
		}
		if( obj == this ) {
			return true;
		}
		
		ConnectionData d = (ConnectionData)obj;
		return d.port == port && d.address.equals(address);
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, port);
	}
}
