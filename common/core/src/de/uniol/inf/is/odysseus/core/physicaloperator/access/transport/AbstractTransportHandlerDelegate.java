package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class AbstractTransportHandlerDelegate<T extends IStreamObject<IMetaAttribute>> {

	private final static Logger LOG = LoggerFactory
			.getLogger(AbstractTransportHandlerDelegate.class);
	private final static InfoService INFO = InfoServiceFactory.getInfoService(AbstractTransportHandlerDelegate.class);

	private final List<ITransportHandlerListener<T>> transportHandlerListener = new ArrayList<ITransportHandlerListener<T>>();
	private int openCounter = 0;
	private final ITransportExchangePattern exchangePattern;
	final private ITransportHandler callOnMe;

	final private OptionMap optionsMap;
	private SDFSchema schema;
	private ITransportDirection direction;

	public AbstractTransportHandlerDelegate(
			ITransportExchangePattern exchangePattern,
			ITransportDirection direction, ITransportHandler callOnMe,
			OptionMap optionsMap) {
		this.exchangePattern = exchangePattern;
		this.callOnMe = callOnMe;
		this.optionsMap = optionsMap;
		this.direction = direction;
	}

	public void addListener(ITransportHandlerListener<T> listener) {
		this.transportHandlerListener.add(listener);
	}

	public void removeListener(ITransportHandlerListener<T> listener) {
		this.transportHandlerListener.remove(listener);
	}

	public void fireProcess(long callerId, ByteBuffer message) {
		try {
			for (ITransportHandlerListener<T> l : transportHandlerListener) {
				// TODO: flip() erases the contents of the message if
				// it was already flipped or just created...
				// In other words: This method expects that the byte buffer
				// is not fully prepared
				message.flip();
				l.process(callerId, message);
			}
		} catch (Exception e) {
			INFO.warning("Error processing message",e);
			LOG.warn("Error processing message",e);
			throw e;
		}
	}

	public void fireProcess(InputStream message) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.process(message);
		}
	}

	public void fireProcess(T m) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.process(m);
		}
	}

	public void fireProcess(T m, int port) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.process(m, port);
		}
	}

	public void fireProcess(String[] message) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.process(message);
		}
	}

	public void fireProcess(String message) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.process(message);
		}
	}

	public void fireDone() {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.propagateDone();
		}		
	}
	
	public void fireOnConnect(ITransportHandler handler) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.onConnect(handler);
		}
	}

	public void fireOnDisconnect(ITransportHandler handler) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.onDisonnect(handler);
		}
	}

	public ITransportExchangePattern getExchangePattern() {
		return this.exchangePattern;
	}

	final synchronized public void open() throws UnknownHostException,
			IOException {
		LOG.debug("Calling open with " + this.getExchangePattern() + " for "
				+ direction);
		if (openCounter == 0) {
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.InOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.InOptionalOut) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				if (direction == ITransportDirection.IN
						|| direction == ITransportDirection.INOUT) {
					callOnMe.processInOpen();
				}
			}
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.OutOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.OutOptionalIn) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				if (direction == ITransportDirection.OUT
						|| direction == ITransportDirection.INOUT) {
					callOnMe.processOutOpen();
				}

			}
		}
		openCounter++;
	}

	public void start() {
		callOnMe.processInStart();
	}


	final synchronized public void close() throws IOException {
		openCounter--;
		if (openCounter == 0) {
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.InOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.InOptionalOut) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				if (direction == ITransportDirection.IN
						|| direction == ITransportDirection.INOUT) {
					callOnMe.processInClose();
				}
			}
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.OutOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.OutOptionalIn) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				if (direction == ITransportDirection.OUT
						|| direction == ITransportDirection.INOUT)
					;
				callOnMe.processOutClose();
			}
		}
	}

	/**
	 * This method is supposed to retrieve the options for an instance, which
	 * were used during the call of
	 * {@link ITransportHandler#createInstance(IProtocolHandler, Map))} based on
	 * the current configuration. This is useful for comparing and serialising
	 * different TransportHandler-instances.
	 *
	 * @return
	 */
	public OptionMap getOptionsMap() {
		return optionsMap;
	}

	// public void setOptionsMap(Map<String, String> options) {
	// this.optionsMap = options;
	// }

	public void setSchema(SDFSchema schema) {
		this.schema = schema;
	}

	public SDFSchema getSchema() {
		return schema;
	}

	public void optionsMapChanged(String key, String value) {
		throw new UnsupportedOperationException("Sorry. Update of options not supported by "+this.callOnMe.getName());
	}


}
