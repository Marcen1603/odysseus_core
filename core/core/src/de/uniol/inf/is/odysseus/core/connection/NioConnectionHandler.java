package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public class NioConnectionHandler implements IAccessConnectionHandler<ByteBuffer>, IConnectionListener {

	Logger logger = LoggerFactory.getLogger(NioConnectionHandler.class);
	
	final private NioConnection nioConnection;
	final private String host;
	final private int port;
	final private boolean autoReconnect;
	final private String user;
	final private String password;
	private IAccessConnectionListener<ByteBuffer> caller;

	private int tries = 0;
	private int waitingForNextReconnect;

	public NioConnectionHandler(String host, int port, boolean autoconnect,
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

		nioConnection = NioConnection.getInstance();
		nioConnection.addConnectionListener(this);
	}
	
	public NioConnectionHandler(Map<String, String> options) throws NumberFormatException, IOException{
		this(options.get("host"), Integer.parseInt(options.get("port")), Boolean.parseBoolean(options.get("autoconnect")),
				options.get("user"), options.get("password"));
	}

	@Override
	public IAccessConnectionHandler<ByteBuffer> getInstance(
			Map<String, String> options) {
		try {
			return new NioConnectionHandler(options);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public NioConnectionHandler(NioConnectionHandler nioConnection) {
		this.nioConnection = nioConnection.nioConnection;
		this.host = nioConnection.host;
		this.port = nioConnection.port;
		this.autoReconnect = nioConnection.autoReconnect;
		this.user = nioConnection.user;
		this.password = nioConnection.password;
	}
	
	@Override
	public String getName() {
		return "NIO";
	}

	@Override
	public void notify(IConnection nioConnection, ConnectionMessageReason reason) {
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
	public NioConnectionHandler clone() {
		return new NioConnectionHandler(this);
	}

	@Override
	public void open(IAccessConnectionListener<ByteBuffer> caller)
			throws OpenFailedException {
		try {
			this.caller = caller;
			nioConnection.connectToServer(caller, host, port, user, password);
		} catch (Exception e) {
			throw new OpenFailedException(e);
		}
	}

	@Override
	public void close(IAccessConnectionListener<ByteBuffer> caller) throws IOException {
		nioConnection.disconnectFromServer(caller);
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
