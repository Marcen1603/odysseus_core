package de.uniol.inf.is.odysseus.peer.transmission;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public abstract class AbstractTransmissionReceiver implements ITransmissionReceiver{

	private static final Logger LOG = LoggerFactory.getLogger(AbstractTransmissionReceiver.class);

	private final Collection<ITransmissionReceiverListener> listeners = Lists.newArrayList();

	@Override
	public final void addListener(ITransmissionReceiverListener listener) {
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}

	@Override
	public final void removeListener(ITransmissionReceiverListener listener) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}

	protected final void fireDataEvent( byte[] data ) {
		synchronized( listeners ) {
			for( ITransmissionReceiverListener listener : listeners ) {
				try {
					listener.onReceiveData(this, data);
				} catch( Throwable t ) {
					LOG.error("Exeption in transmission listener", t);
				}
			}
		}
	}
	
	protected final void firePunctuation( IPunctuation punc ) {
		synchronized( listeners ) {
			for( ITransmissionReceiverListener listener : listeners ) {
				try {
					listener.onReceivePunctuation(this, punc);
				} catch( Throwable t ) {
					LOG.error("Exeption in transmission listener", t);
				}
			}
		}
	}
	
	protected final void fireDoneEvent() {
		synchronized( listeners ) {
			for( ITransmissionReceiverListener listener : listeners ) {
				try {
					listener.onReceiveDone(this);
				} catch( Throwable t ) {
					LOG.error("Exeption in transmission listener", t);
				}
			}
		}
	}
}
