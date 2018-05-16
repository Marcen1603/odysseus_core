package de.uniol.inf.is.odysseus.wrapper.iec60870;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;

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

/**
 * Abstract handler for the communication protocol IEC 60870-5-104. It uses the
 * library oj104 as implementation of the IEC 60870-5-104. It has three options
 * to configure:
 * <ul>
 * <li>104_ignoreHandshakes: True when startDT, stopDT, sequence numbers and
 * acks shall be ignored. Default = false.</li>
 * <li>104_ignoreTimeouts: True when t1, t2 and t3 shall be ignored. Default =
 * false.</li>
 * <li>104_sendResponses: True, when received Format I APDUs shall be responded.
 * Default = true.</li>
 * </ul>
 * Two scenarios for the usage of the protocol and its options:
 * <ol>
 * <li>Together with a tcp client or server transport handler, acting as server
 * or client and connected to a RTU or master station. In this case, handshakes
 * and timeouts should not be ignored and responses should be sent.</li>
 * <li>Together with a pcap file transport handler, acting as server or client and reading
 * 104 messages from a pcap file. In this case, handshakes and timeouts should
 * be ignored and responses should not be send.
 * </ol>
 * Data format:<br />
 * The protocol handler can only handle tuples with one of the following schemes:
 * <ol>
 * <li>One attribute, an {@link ASDU}.</li>
 * <li>Two attributes, a {@link DataUnitIdentifier} and a list of {@link IInformationObject}s</li>
 * </ol>
 * 
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public abstract class AbstractIEC104ProtocolHandler extends AbstractProtocolHandler<Tuple<IMetaAttribute>>
		implements IASDUHandler, ICommunicationHandler {

	private static final String ignoreHandshakesKey = "104_ignoreHandshakes";

	private static final String ignoreTimeoutsKey = "104_ignoreTimeouts";

	private static final String sendResponsesKey = "104_sendResponses";

	private IAPDUHandler apduHandler = new StandardAPDUHandler();

	public IAPDUHandler getApduHandler() {
		return apduHandler;
	}

	public AbstractIEC104ProtocolHandler() {
		super();
		apduHandler.setASDUHandler(this);
		apduHandler.setCommunicationHandler(this);
	}

	public AbstractIEC104ProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<Tuple<IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		apduHandler.setASDUHandler(this);
		apduHandler.setCommunicationHandler(this);
		init_internal();
	}

	private void init_internal() {
		OptionMap options = optionsMap;
		if (options.containsKey(ignoreHandshakesKey)) {
			boolean ignoreHandshakes = Boolean.parseBoolean(options.get(ignoreHandshakesKey));
			getApduHandler().ignoreHandshakes(ignoreHandshakes);
			getLogger().debug("Set '{}' to '{}'", ignoreHandshakesKey, ignoreHandshakes);
		}
		if (options.containsKey(ignoreTimeoutsKey)) {
			boolean ignoreTimeouts = Boolean.parseBoolean(options.get(ignoreTimeoutsKey));
			getApduHandler().ignoreTimeouts(ignoreTimeouts);
			getLogger().debug("Set '{}' to '{}'", ignoreTimeoutsKey, ignoreTimeouts);
		}
		if (options.containsKey(sendResponsesKey)) {
			boolean sendResponses = Boolean.parseBoolean(options.get(sendResponsesKey));
			getApduHandler().sendResponses(sendResponses);
			getLogger().debug("Set '{}' to '{}'", sendResponsesKey, sendResponses);
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

	protected abstract Logger getLogger();
	
	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		getTransportHandler().processInOpen();
		super.open();
	}

	private void process(byte[] message) {
		try {
			getLogger().trace("Received (bytes): {}", message);

			APDU apdu = new APDU();
			apdu.fromBytes(message);
			getLogger().trace("Received (APDU): {}", message);

			apduHandler.handleAPDU(apdu);
		} catch (IEC608705104ProtocolException | IOException e) {
			getLogger().error("Error while reading message {}!", message, e);
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
			getLogger().error("Error while reading message {}!", message, e);
		}
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		if(message.hasArray()) {
			process(message.array());
		} else {
			byte[] bytes = new byte[message.remaining()];
			message.get(bytes);
			process(bytes);
		}
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
		Object firstAttribute, secondAttribute;
		switch (message.getAttributes().length) {
		case 1:
			// ASDU
			firstAttribute = message.getAttribute(0);
			if (firstAttribute instanceof ASDU) {
				try {
					apduHandler.buildAndSendAPDU((ASDU) firstAttribute);
				} catch (IEC608705104ProtocolException | IOException e) {
					getLogger().error("Error while building and sending ASDU from {}", firstAttribute);
				}
			} else {
				getLogger().error("Mal formatted tuple: only attribute must be an ASDU, or, with two attributes, first attribute a DataUnitIdentifier and second a list of information objects!");
			}
			break;
		case 2:
			// DataUnitIdentifier + List<IInformationObject>
			firstAttribute = message.getAttribute(0);
			secondAttribute = message.getAttribute(1);
			if (!(firstAttribute instanceof DataUnitIdentifier) || !(secondAttribute instanceof List)) {
				getLogger().error("Mal formatted tuple: only attribute must be an ASDU, or, with two attributes, first attribute a DataUnitIdentifier and second a list of information objects!");
				break;
			}
			
			List<IInformationObject> ioList = ((List<?>) secondAttribute).stream().map(object -> {
				if (object instanceof IInformationObject) {
					return (IInformationObject) object;
				} else {
					getLogger()
							.error("Mal formatted tuple: second attribute must be a list of IInformationObjects!");
					return null;
				}
			}).collect(Collectors.toList());
			try {
				apduHandler.buildAndSendAPDU(new ASDU((DataUnitIdentifier) firstAttribute, ioList));
			} catch (IEC608705104ProtocolException | IOException e) {
				getLogger().error("Error while building and sending ASDU from {} and {}", firstAttribute,
						secondAttribute);
			}
			break;
		default:
			getLogger().error("Mal formatted tuple: only attribute must be an ASDU, or, with two attributes, first attribute a DataUnitIdentifier and second a list of information objects!");
			break;
		}
	}

	@Override
	public void process(Tuple<IMetaAttribute> message, int port) {
		process(message);
	}
	
	@Override
	public void write(Tuple<IMetaAttribute> object) throws IOException {
		process(object);
	}

	@Override
	public Optional<ASDU> handleASDU(ASDU asdu) {
		// Creates a tuple with DataUnitIdentifier and List of InformationObjects
		ITransfer<Tuple<IMetaAttribute>> transfer = getTransfer();

		Tuple<IMetaAttribute> tuple = new Tuple<>(2, false);
		tuple.setAttribute(0, asdu.getDataUnitIdentifier());
		tuple.setAttribute(1, asdu.getInformationObjects());
		transfer.transfer(tuple);

		// No response ASDU to server
		return Optional.empty();
	}

	@Override
	public void closeConnection() {
		//XXX not implemented. Calling close() here causes a shutdown of Odysseus (I don't know why).
	}

	@Override
	public void send(APDU apdu) throws IOException, IEC608705104ProtocolException {
		ITransportHandler transportHandler = getTransportHandler();
		byte[] bytes = apdu.toBytes();
		transportHandler.send(bytes);
		getLogger().trace("Sent (APDU): {}", apdu);
		getLogger().trace("Sent (bytes): {}", bytes);
	}

}