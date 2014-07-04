package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class AbstractTransportHandlerDelegate<T>{
	
	private final List<ITransportHandlerListener<T>> transportHandlerListener = new ArrayList<ITransportHandlerListener<T>>();
	private int openCounter = 0;
	private final ITransportExchangePattern exchangePattern;
	final private ITransportHandler callOnMe;
	
	private Map<String, String> optionsMap;
	private SDFSchema schema;

	
	public AbstractTransportHandlerDelegate(ITransportExchangePattern exchangePattern, ITransportHandler callOnMe) {
		this.exchangePattern = exchangePattern;
		this.callOnMe = callOnMe;
	}

	public void addListener(ITransportHandlerListener<T> listener) {
		this.transportHandlerListener.add(listener);
	}

	public void removeListener(ITransportHandlerListener<T> listener) {
		this.transportHandlerListener.remove(listener);
	}

	public void fireProcess(ByteBuffer message) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			// TODO: flip() erases the contents of the message if
			// it was already flipped or just created...
			// In other words: This method expects that the byte buffer
			// is not fully prepared
			message.flip();
			l.process(message);
		}
	}
	

	public void fireProcess(T m) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.process(m);
		}		
	}

	public void fireProcess(String[] message) {
		for (ITransportHandlerListener<T> l : transportHandlerListener) {
			l.process(message);
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
		if (openCounter == 0) {
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.InOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.InOptionalOut) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				callOnMe.processInOpen();
			}
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.OutOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.OutOptionalIn) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				callOnMe.processOutOpen();
			}
		}
		openCounter++;
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
				callOnMe.processInClose();
			}
			if (getExchangePattern() != null
					&& (getExchangePattern().equals(
							ITransportExchangePattern.OutOnly)
							|| getExchangePattern().equals(
									ITransportExchangePattern.OutOptionalIn) || getExchangePattern()
							.equals(ITransportExchangePattern.InOut))) {
				callOnMe.processOutClose();
			}
		}
	}
	
    /**
     * This method is supposed to retrieve the options for an instance, which were used during the call of {@link ITransportHandler#createInstance(IProtocolHandler, Map))}
     * based on the current configuration. This is useful for comparing and serialising different TransportHandler-instances.
     * @return
     */
	public Map<String, String> getOptionsMap() {
		return optionsMap;
	}
	
	public void setOptionsMap(Map<String, String> options) {
		this.optionsMap = options;
	}
	
	public void setSchema(SDFSchema schema) {
		this.schema = schema;
	}
	
	public SDFSchema getSchema() {
		return schema;
	}


}
