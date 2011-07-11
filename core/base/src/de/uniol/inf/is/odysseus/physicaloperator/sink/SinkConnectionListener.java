package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SinkConnectionListener extends Thread {

	static private Logger logger = LoggerFactory.getLogger(SocketSinkPO.class);

	final int port;
	final private ISinkStreamHandlerBuilder sinkStreamHandlerBuilder;
	final List<ISinkStreamHandler> subscribe;

	public SinkConnectionListener(int port,
			ISinkStreamHandlerBuilder sinkStreamHandlerBuilder, /*OUT!!*/List<ISinkStreamHandler> subscribe2) {
		this.port = port;
		this.sinkStreamHandlerBuilder = sinkStreamHandlerBuilder;
		this.subscribe = subscribe2;
	}

	@Override
	public void run() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			Socket socket = null;
			try {
				logger.debug("Waiting for Server to connect on "+port);
				socket = server.accept();
				logger.debug("Connection from "
						+ socket.getRemoteSocketAddress());
				if (socket != null) {
					logger.debug("Adding Handler");
					ISinkStreamHandler temp = sinkStreamHandlerBuilder.newInstance(socket);
					synchronized (subscribe) {
						subscribe.add(temp);
					}
					logger.debug("Adding Handler done");

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
