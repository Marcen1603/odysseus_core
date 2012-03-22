package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import de.uniol.inf.is.odysseus.core.server.store.OsgiObjectInputStream;

public class ObjectStreamInput extends AbstractInput<ObjectInputStream> {

	final private String hostname;
	final private int port;
	final private String user;
	final private String password;
	private Socket socket;
	private ObjectInputStream channel;

	
	public ObjectStreamInput(String hostname, int port, String user, String password){
		this.hostname = hostname;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	@Override
	public void init() {
		try {
			socket = new Socket(this.hostname, this.port);
			this.channel = new OsgiObjectInputStream(socket.getInputStream());
			// Send login information
			if (user != null && password != null) {
				PrintWriter out = new PrintWriter
					    (socket.getOutputStream(), true);
				out.println(user);
				out.println(password);
			}
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
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
