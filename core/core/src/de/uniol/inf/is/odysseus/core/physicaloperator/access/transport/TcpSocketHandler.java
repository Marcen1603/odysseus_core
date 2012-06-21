package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

public class TcpSocketHandler extends AbstractTransportHandler {

	private String hostname;
	private int port;
	private Socket socket;
	
	
	public TcpSocketHandler() {
		// Needed for DS
	}

	@Override
	public void process_open() throws UnknownHostException, IOException {
		socket = new Socket(this.hostname, this.port);
	}

	@Override
	public void process_close() throws IOException {
		if (socket != null) {
			socket.getInputStream().close();
			socket.close();
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		socket.getOutputStream().write(message);
	}

	@Override
	public ITransportHandler createInstance(Map<String, String> options) {
		TcpSocketHandler th = new TcpSocketHandler();
		th.hostname = options.get("host");
		th.port = Integer.parseInt(options.get("port"));
		return th;
	}

	@Override
	public InputStream getInputStream() {
		if (socket != null) {
			try {
				return socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return "TCP";
	}
	
}
