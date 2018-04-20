package de.uniol.inf.is.odysseus.wrapper.iec60870;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.communication.IAPDUHandler;
import de.uniol.inf.ei.oj104.communication.IASDUHandler;
import de.uniol.inf.ei.oj104.communication.ICommunicationHandler;
import de.uniol.inf.ei.oj104.communication.StandardAPDUHandler;
import de.uniol.inf.ei.oj104.exception.IEC608705104ProtocolException;
import de.uniol.inf.ei.oj104.model.APDU;
import de.uniol.inf.ei.oj104.model.ASDU;
import de.uniol.inf.ei.oj104.model.DataUnitIdentifier;
import de.uniol.inf.ei.oj104.model.IInformationObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransfer;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

// TODO javaDoc
// It can handle the following tuple schemes: (1) one attribute, an ASDU; (2) two attributes, first DataUnitIdentifier and second List of IInformationObjects
// Creates a tuple with DataUnitIdentifier and List of InformationObjects
public class IEC104ClientProtocolHandler extends AbstractProtocolHandler<Tuple<IMetaAttribute>>
		implements IASDUHandler, ICommunicationHandler {

	// TODO also for KV?
	// TODO abstract Protocol handler useful?

	private static final Logger logger = LoggerFactory.getLogger(IEC104ClientProtocolHandler.class);

	private static final String name = "104Client";

	private IAPDUHandler apduHandler = new StandardAPDUHandler();

	public IEC104ClientProtocolHandler() {
		super();
		apduHandler.setASDUHandler(this);
		apduHandler.setCommunicationHandler(this);
	}

	public IEC104ClientProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<Tuple<IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		apduHandler.setASDUHandler(this);
		apduHandler.setCommunicationHandler(this);
	}

	@Override
	public IEC104ClientProtocolHandler createInstance(ITransportDirection direction, IAccessPattern access,
			OptionMap options, IStreamObjectDataHandler<Tuple<IMetaAttribute>> dataHandler) {
		return new IEC104ClientProtocolHandler(direction, access, options, dataHandler);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		super.open();

		try {
			// send start data transfer message
			apduHandler.startDataTransfer();
		} catch (IEC608705104ProtocolException e) {
			logger.error("Error while sending startDT command!", e);
		}
	}

	@Override
	public void close() throws IOException {
		try {
			// send stop data transfer message
			apduHandler.stopDataTransfer();
		} catch (IEC608705104ProtocolException e) {
			logger.error("Error while sending stopDT command!", e);
		}

		super.close();
	}

	private void process(byte[] message) {
		try {
			logger.debug("Received (bytes): {}", message);

			APDU apdu = new APDU();
			apdu.fromBytes(message);
			logger.debug("Received (APDU): {}", message);

			apduHandler.handleAPDU(apdu);
		} catch (IEC608705104ProtocolException | IOException e) {
			logger.error("Error while reading message {}!", message, e);
		}
	}

	@Override
	public void process(InputStream message) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(message));
			char[] buffer = new char[200];
			int length = bufferedReader.read(buffer, 0, 200);
			process(new String(buffer, 0, length));
		} catch (IOException e) {
			logger.error("Error while reading message {}!", message, e);
		}
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		process(message.array());
	}

	@Override
	public void process(String message) {
		process(message.getBytes());
	}

	@Override
	public void process(String[] message) {
		StringBuilder builder = new StringBuilder();
		Arrays.asList(message).stream().forEach(string -> builder.append(string + "\n"));
		process(builder.toString().getBytes());
	}

	@Override
	public void process(Tuple<IMetaAttribute> message) {
		// It can handle the following tuple schemes: (1) one attribute, an ASDU; (2)
		// two attributes, first DataUnitIdentifier and second List of
		// IInformationObjects
		Object firstAttribute = null, secondAttribute;
		switch (message.getAttributes().length) {
		case 1:
			// ASDU
			firstAttribute = message.getAttribute(0);
			if (firstAttribute instanceof ASDU) {
				try {
					apduHandler.buildAndSendAPDU((ASDU) firstAttribute);
				} catch (IEC608705104ProtocolException | IOException e) {
					logger.error("Error while building and sending ASDU from {}", firstAttribute);
				}
				break;
			} else if (!(firstAttribute instanceof DataUnitIdentifier)) {
				logger.error("Mal formatted tuple: first attribute must be an ASDU or a DataUnitIdentifier!");
				break;
			}
			// else no break; use firstAttribute (DataUnitIdentifier) in snd. case
		case 2:
			// DataUnitIdentifier + List<IInformationObject>
			secondAttribute = message.getAttribute(1);
			if (secondAttribute instanceof List) {
				List<IInformationObject> ioList = ((List<?>) secondAttribute).stream().map(object -> {
					if (object instanceof IInformationObject) {
						return (IInformationObject) object;
					} else {
						logger.error("Mal formatted tuple: second attribute must be a list of IInformationObjects!");
						return null;
					}
				}).collect(Collectors.toList());
				try {
					apduHandler.buildAndSendAPDU(new ASDU((DataUnitIdentifier) firstAttribute, ioList));
				} catch (IEC608705104ProtocolException | IOException e) {
					logger.error("Error while building and sending ASDU from {} and {}", firstAttribute,
							secondAttribute);
				}
				break;
			} else {
				logger.error("Mal formatted tuple: second attribute must be a list of IInformationObjects!");
				break;
			}
		default:
			logger.error(
					"Mal formatted tuple: first attribute must be an ASDU or a DataUnitIdentifier; second attribute must be a list of IInformationObjects if first is a DataUnitIdentifier!");
			break;
		}
	}

	@Override
	public void process(Tuple<IMetaAttribute> message, int port) {
		process(message);
	}

	@Override
	public Future<Optional<ASDU>> handleASDU(ASDU asdu) {
		// Creates a tuple with DataUnitIdentifier and List of InformationObjects
		ITransfer<Tuple<IMetaAttribute>> transfer = getTransfer();

		Tuple<IMetaAttribute> tuple = new Tuple<>(2, false);
		tuple.setAttribute(0, asdu.getDataUnitIdentifier());
		tuple.setAttribute(1, asdu.getInformationObjects());
		transfer.transfer(tuple);

		// No response ASDU to server
		return ConcurrentUtils.constantFuture(Optional.empty());
	}

	@Override
	public void closeConnection() {
		try {
			close();
		} catch (IOException e) {
			logger.error("Error while closing connection!", e);
		}
	}

	@Override
	public void send(APDU apdu) throws IOException, IEC608705104ProtocolException {
		ITransportHandler transportHandler = getTransportHandler();
		byte[] bytes = apdu.toBytes();
		transportHandler.send(bytes);
		logger.debug("Sent (APDU): {}", apdu);
		logger.debug("Sent (bytes): {}", bytes);
	}

}