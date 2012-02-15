package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

public class RouterConnection implements IAccessConnection, IConnectionListener {

	final private Router router;
	final private String host;
	final private int port;
	final private boolean autoReconnect;

	// private int tries = 0;

	public RouterConnection(String host, int port, boolean autoconnect)
			throws IOException {
		this.host = host;
		this.port = port;
		this.autoReconnect = autoconnect;
		
		if (autoReconnect == true) {
			throw new IllegalArgumentException(
					"Auto Reconnect currently not supported");
		}

		router = Router.getInstance();
		router.addConnectionListener(this);
	}

	public RouterConnection(RouterConnection routerConnection) {
		this.router = routerConnection.router;
		this.host = routerConnection.host;
		this.port = routerConnection.port;
		this.autoReconnect = routerConnection.autoReconnect;
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

	public void reconnect() {
		// TODO: Implement this
		// if (this.opened && this.autoReconnect) {
		// if (waitingForNextReconnect < 0) {
		// waitingForNextReconnect = 0;
		// }
		// getLogger().info(
		// "Reconnect in " + waitingForNextReconnect + " ms ...");
		// try {
		// Thread.sleep(waitingForNextReconnect);
		// } catch (InterruptedException e1) {
		// }
		// getLogger().info("Trying to reconnect...");
		// process_close();
		// try {
		// process_open();
		// } catch (OpenFailedException e) {
		// if (e.getCause() instanceof IOException) {
		// reconnect();
		// }
		// e.printStackTrace();
		// }
		// // failed again... increase waittime
		// waitingForNextReconnect = ((tries * tries) * 100);
		// tries++;
		// }
	}

	@Override
	public RouterConnection clone() {
		return new RouterConnection(this);
	}

	@Override
	public void open(IAccessConnectionListener caller)
			throws OpenFailedException {
		try {
			router.connectToServer(caller, host, port);
		} catch (Exception e) {
			throw new OpenFailedException(e);
		}
	}

	@Override
	public void close(IAccessConnectionListener caller) throws IOException {
		router.disconnectFromServer(caller);
	}

}
