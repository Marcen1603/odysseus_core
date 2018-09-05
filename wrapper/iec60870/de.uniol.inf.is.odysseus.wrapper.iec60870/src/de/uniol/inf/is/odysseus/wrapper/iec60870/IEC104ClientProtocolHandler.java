package de.uniol.inf.is.odysseus.wrapper.iec60870;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.exception.IEC608705104ProtocolException;
import de.uniol.inf.ei.oj104.model.ASDU;
import de.uniol.inf.ei.oj104.model.DataUnitIdentifier;
import de.uniol.inf.ei.oj104.model.IInformationObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

/**
 * Client handler for the communication protocol IEC 60870-5-104. It uses the
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
 * <li>Together with a tcp client transport handler and
 * connected to a RTU. In this case, handshakes and timeouts should not be
 * ignored and responses should be sent.</li>
 * <li>Together with a pcap file transport handler and reading
 * 104 messages from a pcap file. In this case, handshakes and timeouts should
 * be ignored and responses should not be send.
 * </ol>
 * Data format:<br />
 * The protocol handler can only handle tuples with one of the following
 * schemes:
 * <ol>
 * <li>One attribute, an {@link ASDU}.</li>
 * <li>Two attributes, a {@link DataUnitIdentifier} and a list of
 * {@link IInformationObject}s</li>
 * </ol>
 * 
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class IEC104ClientProtocolHandler extends AbstractIEC104ProtocolHandler {

	private static final String name = "104Client";

	public IEC104ClientProtocolHandler() {
		super();
	}

	public IEC104ClientProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<Tuple<IMetaAttribute>> dataHandler) {
		super(direction, access, options, dataHandler);
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
	public void open() throws UnknownHostException, IOException {	
		super.open();

		Executors.newSingleThreadExecutor().submit(() -> {
			try {
				// send start data transfer message
				if (!getApduHandler().areHandshakesIgnored()) {
					getApduHandler().startDataTransfer();
				}
			} catch (IEC608705104ProtocolException | IOException e) {
				getLogger().error("Error while sending startDT command!", e);
			}
		});
	}

	@Override
	public void close() throws IOException {
		try {
			// send stop data transfer message
			if (!getApduHandler().areHandshakesIgnored()) {
				getApduHandler().stopDataTransfer();
			}
		} catch (IEC608705104ProtocolException e) {
			getLogger().error("Error while sending stopDT command!", e);
		}

		super.close();
	}

	@Override
	protected Logger getLogger() {
		return LoggerFactory.getLogger(IEC104ClientProtocolHandler.class);
	}

}