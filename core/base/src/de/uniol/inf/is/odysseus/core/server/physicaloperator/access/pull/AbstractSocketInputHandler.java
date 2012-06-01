package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

abstract public class AbstractSocketInputHandler<T> extends AbstractInputHandler<T> {

	private String hostname;
	private int port;
	private String user;
	private String password;
	private Socket socket;
	
	public AbstractSocketInputHandler(){
		// Default constructor needed for service
	}
	
	public AbstractSocketInputHandler(String hostname, int port, String user, String password){
		this.hostname = hostname;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	@Override
	public void init() {
		try {
			socket = new Socket(this.hostname, this.port);
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
	
	public InputStream getInputStream(){
		if (socket != null){
			try {
				return socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void terminate() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
