package de.uniol.inf.is.odysseus.peer.transmission.socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketDataChannel {

	private final Socket socket;
	
	private final BufferedOutputStream out;
	
	public SocketDataChannel(Socket socket) throws IOException {
		this.socket = socket;
		
		out = new BufferedOutputStream(socket.getOutputStream());
	}
	
	public void close() {
		if( out != null ) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
		
		if( socket != null ) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	public void write( byte[] data ) throws IOException {
		out.write(data);
		out.flush();
	}
}
