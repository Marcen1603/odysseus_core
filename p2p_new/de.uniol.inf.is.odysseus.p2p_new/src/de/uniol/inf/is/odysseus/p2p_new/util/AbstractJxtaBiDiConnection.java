package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;
import java.util.List;

import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public abstract class AbstractJxtaBiDiConnection
		implements
			IJxtaConnection,
			PipeMsgListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractJxtaBiDiConnection.class);

	private final PipeAdvertisement pipeAdvertisement;
	private final List<IJxtaConnectionListener> listeners = Lists
			.newArrayList();

	private boolean isConnected;

	public AbstractJxtaBiDiConnection(PipeAdvertisement adv) {
		Preconditions.checkNotNull(adv, "Pipe advertisement must not be null!");

		pipeAdvertisement = adv;
	}

	@Override
	public void removeListener(IJxtaConnectionListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void addListener(IJxtaConnectionListener listener) {
		Preconditions.checkNotNull(listener,
				"Listener to add must not be null!");
		listeners.add(listener);
	}

	@Override
	public void connect() throws IOException {

		getBiDiPipe().setMessageListener(this);
		isConnected = true;

		fireConnectEvent();
	}

	@Override
	public void disconnect() {
		isConnected = false;

		fireDisconnectEvent();
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public void send(byte[] message) throws IOException {
		Message msg = new Message();
		msg.addMessageElement(new ByteArrayMessageElement("bytes", null,
				message, null));

		getBiDiPipe().sendMessage(msg);
	}

	@Override
	public void pipeMsgEvent(PipeMsgEvent event) {
		Message msg = event.getMessage();

		ByteArrayMessageElement bytes = (ByteArrayMessageElement) msg
				.getMessageElement("bytes");
		fireMessageReceiveEvent(bytes.getBytes());
	}

	@Override
	public PipeAdvertisement getPipeAdvertisement() {
		return pipeAdvertisement;
	}

	protected final void fireConnectEvent() {
		for (final IJxtaConnectionListener listener : listeners) {
			try {
				listener.onConnect(this);
			} catch (final Throwable t) {
				LOG.error("Exception in JxtaConnection listener", t);
			}
		}
	}

	protected final void fireDisconnectEvent() {
		for (final IJxtaConnectionListener listener : listeners) {
			try {
				listener.onDisconnect(this);
			} catch (final Throwable t) {
				LOG.error("Exception in JxtaConnection listener", t);
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
	protected abstract JxtaBiDiPipe getBiDiPipe();

}
