package de.uniol.inf.is.odysseus.p2p_new.util.deprecated;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

@Deprecated
public abstract class AbstractJxtaConnection implements IJxtaConnectionOld {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractJxtaConnection.class);

	private final PipeAdvertisement pipeAdvertisement;
	private final List<IJxtaConnectionListener> listeners = Lists.newArrayList();

	private InputStream socketInputStream;
	private OutputStream socketOutputStream;
	private RepeatingJobThread readingDataThread;

	private boolean isConnected;

	public AbstractJxtaConnection(PipeAdvertisement pipeAdvertisement) {
		this.pipeAdvertisement = Preconditions.checkNotNull(pipeAdvertisement, "pipeAdvertisement must not be null!");
	}

	@Override
	public final void addListener(IJxtaConnectionListener listener) {
		Preconditions.checkNotNull(listener, "Listener to add must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void connect() throws IOException {
		fireConnectEvent();

		socketInputStream = getInputStream();
		socketOutputStream = getOutputStream();

		isConnected = true;
		readingDataThread = new RepeatingJobThread() {

			private final byte[] buffer = new byte[1024];

			@Override
			public void doJob() {
				try {
					final int bytesRead = socketInputStream.read(buffer);
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
		readingDataThread.setName("[" + getClass().getSimpleName() + "] Reading data " + pipeAdvertisement.getPipeID().toString());
		readingDataThread.start();
	}

	@Override
	public void disconnect() {
		if (readingDataThread != null) {
			readingDataThread.stopRunning();
		}

		isConnected = false;
		fireDisconnectEvent();
	}

	@Override
	public final PipeAdvertisement getPipeAdvertisement() {
		return pipeAdvertisement;
	}

	@Override
	public final boolean isConnected() {
		return isConnected;
	}

	@Override
	public final void removeListener(IJxtaConnectionListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		socketOutputStream.write(message);
		socketOutputStream.flush();
	}

	protected final void fireConnectEvent() {
		synchronized (listeners) {
			for (final IJxtaConnectionListener listener : listeners) {
				try {
					listener.onConnect(this);
				} catch (final Throwable t) {
					LOG.error("Exception in JxtaConnection listener", t);
				}
			}
		}
	}

	protected final void fireDisconnectEvent() {
		synchronized (listeners) {
			for (final IJxtaConnectionListener listener : listeners) {
				try {
					listener.onDisconnect(this);
				} catch (final Throwable t) {
					LOG.error("Exception in JxtaConnection listener", t);
				}
			}
		}
	}

	protected final void fireMessageReceiveEvent(byte[] data) {
		Preconditions.checkNotNull(data, "Byte data must not be null!");
		Preconditions.checkArgument(data.length > 0, "Byte data must not be empty!");

		synchronized (listeners) {
			for (final IJxtaConnectionListener listener : listeners) {
				try {
					listener.onReceiveData(this, data);
				} catch (final Throwable t) {
					LOG.error("Exception in JxtaConnection listener", t);
				}
			}
		}
	}

	protected abstract InputStream getInputStream() throws IOException;

	protected abstract OutputStream getOutputStream() throws IOException;
}
