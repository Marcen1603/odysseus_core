package de.uniol.inf.is.odysseus.wrapper.iec60870;

import java.io.IOException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.exception.IEC608705104ProtocolException;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

// TODO javaDoc
// It can handle the following tuple schemes: (1) one attribute, an ASDU; (2) two attributes, first DataUnitIdentifier and second List of IInformationObjects
// Creates a tuple with DataUnitIdentifier and List of InformationObjects
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

		try {
			// send start data transfer message
			if(!getApduHandler().areHandshakesIgnored()) {
				getApduHandler().startDataTransfer();
			}
		} catch (IEC608705104ProtocolException e) {
			getLogger().error("Error while sending startDT command!", e);
		}
	}

	@Override
	public void close() throws IOException {
		try {
			// send stop data transfer message
			if(!getApduHandler().areHandshakesIgnored()) {
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