package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

class JxtaBiDiConnection implements IJxtaConnection{

	private static final Logger LOG = LoggerFactory.getLogger(JxtaBiDiConnection.class);
	
	private final List<IJxtaConnectionListener> listeners = Lists.newArrayList();
	private Socket socket;
	
	private OutputStream outputStream;
	private InputStream inputStream;
	
	private RepeatingJobThread readingDataThread;
	
	private boolean isConnected;
	
	JxtaBiDiConnection() {
		
	}
	
	JxtaBiDiConnection( Socket socket ) {
		Preconditions.checkNotNull(socket, "socket must not be null!");
		
		this.socket = socket;
	}

	@Override
	public void connect() throws IOException {
		outputStream = socket.getOutputStream();
		inputStream = socket.getInputStream();
		
		isConnected = true;
		fireConnectEvent();
		
		readingDataThread = new RepeatingJobThread() {

			private final byte[] buffer = new byte[1024];

			@Override
			public void doJob() {
				try {
					final int bytesRead = inputStream.read(buffer);
					if (bytesRead == -1) {
						disconnect();
						stopRunning();
					} else if (bytesRead > 0) {
						final byte[] msg = new byte[bytesRead];
						System.arraycopy(buffer, 0, msg, 0, bytesRead);
						fireMessageReceiveEvent(msg);
					}
				} catch (final IOException ex) {
					LOG.error("Could not read bytes from input stream", ex);

					disconnect();
					stopRunning();
				}
			}

		};
		readingDataThread.setDaemon(true);
		readingDataThread.setName("[" + getClass().getSimpleName() + "] Reading data ");
		readingDataThread.start();
	}
	
	@Override
	public void addListener(IJxtaConnectionListener listener) {
		Preconditions.checkNotNull(listener, "Listener to add must not be null!");
		
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IJxtaConnectionListener listener) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}

	@Override
	public void disconnect() {
		if( readingDataThread != null) {
			readingDataThread.stopRunning();
		}
		
		try {
			outputStream.close();
			inputStream.close();
			
			socket.close();
		} catch (IOException e) {
			LOG.error("Co uld not close JxtaBiDiPipe", e);
		} finally {
			isConnected = false;
			fireDisconnectEvent();
		}
	}
	

	@Override
	public void send(byte[] data) throws IOException {
		outputStream.write(data);
		outputStream.flush();
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}
	
	protected final Socket getSocket() {
		return socket;
	}
	
	protected final void setSocket( Socket socket ) {
		this.socket = socket;
	}
	
	protected final void fireDisconnectEvent() {
		synchronized( listeners ) {
			for( IJxtaConnectionListener listener : listeners ) {
				try {
					listener.onDisconnect(this);
				} catch( Throwable t ) {
					LOG.error("Exception during invoking jxta connection listener", t);
				}
			}
		}
	}
	
	protected final void fireConnectEvent() {
		synchronized( listeners ) {
			for( IJxtaConnectionListener listener : listeners ) {
				try {
					listener.onConnect(this);
				} catch( Throwable t ) {
					LOG.error("Exception during invoking jxta connection listener", t);
				}
			}
		}
	}
	
	protected final void fireMessageReceiveEvent(byte[] data) {
		for (final IJxtaConnectionListener listener : listeners) {
			try {
				listener.onReceiveData(this, data);
			} catch (final Throwable t) {
				LOG.error("Exception in JxtaConnection listener", t);
			}
		}
	}

}
