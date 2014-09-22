package de.uniol.inf.is.odysseus.p2p_new.data;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.data.endpoint.EndpointDataTransmissionSender;

public abstract class AbstractTransmissionSender implements ITransmissionSender {

	private static final Logger LOG = LoggerFactory.getLogger(EndpointDataTransmissionSender.class);

	private final Collection<ITransmissionSenderListener> listeners = Lists.newArrayList();

	@Override
	public final void addListener(ITransmissionSenderListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public final void removeListener(ITransmissionSenderListener listener) {
		synchronized (listener) {
			listeners.remove(listener);
		}
	}

	protected final void fireOpenEvent() {
		synchronized (listeners) {
			for (ITransmissionSenderListener listener : listeners) {
				try {
					listener.onReceiveOpen(this);
				} catch (Throwable t) {
					LOG.error("Exeption in transmission listener during open", t);
				}
			}
		}
	}

	protected final void fireCloseEvent() {
		synchronized (listeners) {
			for (ITransmissionSenderListener listener : listeners) {
				try {
					listener.onReceiveClose(this);
				} catch (Throwable t) {
					LOG.error("Exeption in transmission listener during close", t);
				}
			}
		}
	}
}
