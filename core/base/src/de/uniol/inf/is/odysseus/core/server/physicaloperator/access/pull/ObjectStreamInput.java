package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;

import de.uniol.inf.is.odysseus.core.server.store.OsgiObjectInputStream;

public class ObjectStreamInput extends SocketInput<ObjectInputStream> {
	
	public ObjectStreamInput(String hostname, int port, String user,
			String password) {
		super(hostname, port, user, password);
	}

	private ObjectInputStream channel;
	
	@Override
	public void init() {
		try {
			super.init();
			this.channel = new OsgiObjectInputStream(getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public boolean hasNext() {
		try {
			return channel.available() != 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ObjectInputStream getNext() {
		return channel;
	}

	@Override
	public void terminate() {
		try {
			channel.close();
			super.terminate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
