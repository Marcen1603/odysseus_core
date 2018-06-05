package de.uniol.inf.is.odysseus.wrapper.ferrybox.physicaloperator.access;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.LineProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 * Protocol Handler to receive custom NMEA string from the FerryBox software by HS Jena
 *
 * @author Henrik Surm
 */
@SuppressWarnings("unused")
public class FerryBoxProtocolHandler extends LineProtocolHandler<KeyValueObject<IMetaAttribute>> {
	private static final Logger LOG = LoggerFactory.getLogger(FerryBoxProtocolHandler.class);
	public static final String NAME = "FerryBox";

	@Override public String getName() { return FerryBoxProtocolHandler.NAME; }

	public FerryBoxProtocolHandler() {
		super();
	}

	public FerryBoxProtocolHandler(ITransportDirection direction, IAccessPattern access,
			IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
	}

	@Override
	public IProtocolHandler<KeyValueObject<IMetaAttribute>> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options, IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler) {
		return new FerryBoxProtocolHandler(direction, access, dataHandler, options);
	}

	private String[] header = null;

	private void processLine(String line)
	{
		if (line.length() == 0) return;

		// The first message fills the header
		if (header == null)
		{
			header = line.split(",");
		}
		else
		{
			// Split received nmea string w/o checksum and fill map
			String[] lines = line.substring(0, line.length()-4).split(",");
			Map<String, Object> event = new HashMap<>();

			// Ignore first entry, since it is the nmea code $icbm
			int i=0;
			for (String entry : lines)
			{
				if (i > 0)
					event.put(header[i], entry);

				i++;
			}

			KeyValueObject<IMetaAttribute> kvObject = KeyValueObject.createInstance(event);
			getTransfer().transfer(kvObject);
		}
	}

	@Override
	public void process(long callerId, final ByteBuffer message)
	{
		String asString = new String(message.array(), 0, message.limit(), getCharset());

		String[] lines = asString.split("\n");
		for (String line : lines)
			processLine(line);
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		return ITransportExchangePattern.InOnly;
	}

	@Override
	public boolean isSemanticallyEqualImpl(final IProtocolHandler<?> other) {
		if (!(other instanceof FerryBoxProtocolHandler)) return false;

		return true;
	}
}
