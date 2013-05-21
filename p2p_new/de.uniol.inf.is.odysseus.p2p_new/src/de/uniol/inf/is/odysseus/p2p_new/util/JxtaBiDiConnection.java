package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.util.JxtaBiDiPipe;

class JxtaBiDiConnection implements IJxtaConnection, PipeMsgListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaBiDiConnection.class);
	
	private final List<IJxtaConnectionListener> listeners = Lists.newArrayList();
	private final JxtaBiDiPipe pipe;
	private boolean isDisconnected;
	
	JxtaBiDiConnection( JxtaBiDiPipe pipe ) {
		Preconditions.checkNotNull(pipe, "Pipe for jxta bidi connection must not be null!");
		
		this.pipe = pipe;
		this.pipe.setMessageListener(this);
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
		try {
			pipe.setMessageListener(null);
			pipe.close();
		} catch (IOException e) {
			LOG.error("Co uld not close JxtaBiDiPipe", e);
		} finally {
			isDisconnected = true;
			fireDisconnectEvent();
		}
	}
	

	@Override
	public void send(byte[] data) throws IOException {
		Message msg = new Message();
		msg.addMessageElement(new ByteArrayMessageElement("bytes", null, data, null));

		pipe.sendMessage(msg);
	}

	@Override
	public void pipeMsgEvent(PipeMsgEvent event) {
		Message msg = event.getMessage();

		ByteArrayMessageElement bytes = (ByteArrayMessageElement) msg.getMessageElement("bytes");
		fireMessageReceiveEvent(bytes.getBytes());
	}
	
	protected final JxtaBiDiPipe getPipe() {
		return pipe;
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

	protected final void fireMessageReceiveEvent(byte[] data) {
		for (final IJxtaConnectionListener listener : listeners) {
			try {
				listener.onReceiveData(this, data);
			} catch (final Throwable t) {
				LOG.error("Exception in JxtaConnection listener", t);
			}
		}
	}

	@Override
	public void connect() throws IOException {
		// do nothing
	}

	@Override
	public boolean isConnected() {
		return !isDisconnected;
	}
}
