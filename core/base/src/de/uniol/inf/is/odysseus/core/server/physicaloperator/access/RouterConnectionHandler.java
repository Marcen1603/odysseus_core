package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public class RouterConnectionHandler implements IAccessConnectionHandler<ByteBuffer>, IConnectionListener {

	Logger logger = LoggerFactory.getLogger(RouterConnectionHandler.class);
	
	final private Router router;
	final private String host;
	final private int port;
	final private boolean autoReconnect;
	final private String user;
	final private String password;
	private IAccessConnectionListener<ByteBuffer> caller;

	private int tries = 0;
	private int waitingForNextReconnect;

	public RouterConnectionHandler(String host, int port, boolean autoconnect,
			String user, String password) throws IOException {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.autoReconnect = autoconnect;

		if (autoReconnect == true) {
			throw new IllegalArgumentException(
					"Auto Reconnect currently not supported");
		}

		router = Router.getInstance();
		router.addConnectionListener(this);
	}

	public RouterConnectionHandler(RouterConnectionHandler routerConnection) {
		this.router = routerConnection.router;
		this.host = routerConnection.host;
		this.port = routerConnection.port;
		this.autoReconnect = routerConnection.autoReconnect;
		this.user = routerConnection.user;
		this.password = routerConnection.password;
	}

	@Override
	public void notify(IConnection router, ConnectionMessageReason reason) {
		switch (reason) {
		case ConnectionAbort:
			reconnect();
			break;
		case ConnectionClosed:
			break;
		case ConnectionRefused:
			reconnect();
			break;
		case ConnectionOpened:
			break;
		default:
			break;
		}

	}

	@Override
	public void reconnect() {
		if (caller != null) {
			if (caller.isOpened() && this.autoReconnect) {
				if (waitingForNextReconnect < 0) {
					waitingForNextReconnect = 0;
				}
				logger.info(
						"Reconnect in " + waitingForNextReconnect + " ms ...");
				try {
					Thread.sleep(waitingForNextReconnect);
				} catch (InterruptedException e1) {
				}
				logger.info("Trying to reconnect...");
				caller.process_close();
				try {
					caller.process_open();
				} catch (OpenFailedException e) {
					if (e.getCause() instanceof IOException) {
						reconnect();
					}
					e.printStackTrace();
				}
				// failed again... increase waittime
				waitingForNextReconnect = ((tries * tries) * 100);
				tries++;
			}
		}
	}

	@Override
	public RouterConnectionHandler clone() {
		return new RouterConnectionHandler(this);
	}

	@Override
	public void open(IAccessConnectionListener<ByteBuffer> caller)
			throws OpenFailedException {
		try {
			this.caller = caller;
			router.connectToServer(caller, host, port, user, password);
		} catch (Exception e) {
			throw new OpenFailedException(e);
		}
	}

	@Override
	public void close(IAccessConnectionListener<ByteBuffer> caller) throws IOException {
		router.disconnectFromServer(caller);
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return password;
	}

}
