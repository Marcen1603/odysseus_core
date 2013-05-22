package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;
import java.util.List;

import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.util.JxtaBiDiPipe;
import net.jxta.util.PipeEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

class JxtaBiDiConnection implements IJxtaConnection, PipeMsgListener, PipeEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaBiDiConnection.class);
	private static final int PING_INTERVAL = 2000;
	
	private final List<IJxtaConnectionListener> listeners = Lists.newArrayList();
	private final JxtaBiDiPipe pipe;

	private boolean isDisconnected;
	private RepeatingJobThread pingThread;
	
	JxtaBiDiConnection( JxtaBiDiPipe pipe ) {
		Preconditions.checkNotNull(pipe, "Pipe for jxta bidi connection must not be null!");
		
		this.pipe = pipe;
		this.pipe.setMessageListener(this);
		this.pipe.setPipeEventListener(this);
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
		if( pingThread != null ) {
			pingThread.stopRunning();
		}
		
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

		if( msg.getMessageElement("ping") != null ) {
			// ping message --> do nothing
			return;
		}
		
		ByteArrayMessageElement bytes = (ByteArrayMessageElement) msg.getMessageElement("bytes");
		fireMessageReceiveEvent(bytes.getBytes());
	}
	

	@Override
	public void pipeEvent(int event) {
		System.err.println("Pipe Event: " + event);
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

	@Override
	public void connect() throws IOException {
		fireConnectEvent();
		
		pingThread = new RepeatingJobThread(PING_INTERVAL, "Ping Thread") {

			@Override
			public void doJob() {
				try {
					final Message pingMessage = new Message();
					pingMessage.addMessageElement(new ByteArrayMessageElement("ping", null, new byte[0], null));

					if( !pipe.sendMessage(pingMessage) ) {
						stopRunning();
						disconnect();
					}
				} catch (IOException e) {
					stopRunning();
					disconnect();
				}
			}
		};
		pingThread.start();
	}

	@Override
	public boolean isConnected() {
		return !isDisconnected;
	}
}
