package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class ObjectSinkStreamHandler implements ISinkStreamHandler<Object> {
	OutputStream outS;
	ObjectOutputStream dout;
	Socket socket;

	public ObjectSinkStreamHandler(Socket socket) {
		this.socket = socket;
		try {
			outS = socket.getOutputStream();
			dout = new ObjectOutputStream(outS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void transfer(Object o) throws IOException {
		dout.writeObject(o);
		dout.flush();

	}

	public void done() throws IOException {
		dout.writeObject(Integer.valueOf(0));
		dout.flush();
		dout.close();
		socket.close();
	}

}