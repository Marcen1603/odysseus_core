package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

class ObjectStreamHandler extends Thread implements IStreamHandler<Object> {
	OutputStream outS;
	ObjectOutputStream dout;
	Socket socket;
	List<Object> outputQueue = new LinkedList<Object>();

	public ObjectStreamHandler(Socket socket) {
		this.socket = socket;
		try {
			outS = socket.getOutputStream();
			dout = new ObjectOutputStream(outS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void transfer(Object o) {
		synchronized (outputQueue) {
			outputQueue.add(o);
		}
		notifyAll();
	}

	public void done() {
		try {
			dout.writeObject(Integer.valueOf(0));
			dout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
			dout.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (!interrupted()) {
			synchronized (this) {
				try {
					wait(1000);
					synchronized (outputQueue) {
						for (Object o : outputQueue) {
							try {
								dout.writeObject(o);
								dout.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						outputQueue.clear();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}