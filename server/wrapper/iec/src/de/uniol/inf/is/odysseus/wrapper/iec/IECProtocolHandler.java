package de.uniol.inf.is.odysseus.wrapper.iec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.iec.element.Route;
import de.uniol.inf.is.odysseus.wrapper.iec.parser.IECParser;

public class IECProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {

	private final Logger LOG = LoggerFactory
			.getLogger(IECProtocolHandler.class);
	private static final String PROTOCOL_HANDLER_NAME = "IEC";
	private static final CharSequence ROUTE_START_TAG = "<route";
	private static final CharSequence ROUTE_END_TAG = "</route>";

	private KeyValueObject<? extends IMetaAttribute> next = null;

	private IECParser parser;

	private BufferedReader reader;
	private int delay;
	private Route iec;
	private String iecMessageBuffer = "";
	private boolean waitingForEndTag = false;

	public IECProtocolHandler() {
		parser = new IECParser();
	}

	public IECProtocolHandler(ITransportDirection direction,
			IAccessPattern access,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler);
		parser = new IECParser();
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		IECProtocolHandler instance = new IECProtocolHandler(direction, access,
				dataHandler);
		instance.setOptionsMap(options);
		if (options.containsKey("delay"))
			instance.delay = Integer.parseInt(options.get("delay"));
		return instance;
	}

	@Override
	public String getName() {
		return PROTOCOL_HANDLER_NAME;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		if (!(other instanceof IECProtocolHandler)) {
			return false;
		}
		// the datahandler was already checked in the isSemanticallyEqual-Method
		// of AbstracProtocolHandler
		return true;
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if ((this.getAccessPattern().equals(IAccessPattern.PULL))
				|| (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL)))
			this.reader = new BufferedReader(new InputStreamReader(
					getTransportHandler().getInputStream()));
	}

	@Override
	public void close() throws IOException {
		try {
			if (this.reader != null)
				this.reader.close();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		getTransportHandler().close();
	}

	@Override
	public boolean hasNext() throws IOException {
		if (!reader.ready()) {
			return false;
		}

		String currentString = this.reader.readLine().trim();

		if (!waitingForEndTag) {
			if (currentString.contains(ROUTE_START_TAG)) {
				iecMessageBuffer += currentString;
				waitingForEndTag = true;
			}
			return false;
		} else {
			iecMessageBuffer += currentString;
			if (currentString.contains(ROUTE_END_TAG)) {
				this.parser.parse(iecMessageBuffer);

				// reset after parsing
				iecMessageBuffer = "";
				waitingForEndTag = false;

				// check if iec element is created
				if (this.parser.getIEC() != null) {
					this.iec = this.parser.getIEC();
					this.next = this.iec.toMap();
					this.next.setMetadata("object", iec);
					this.parser.resetParser();
					return true;
				}
				return false;
			}
			return false;
		}
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}
		return next;
	}

	@Override
	public void process(ByteBuffer message) {
		byte[] destinationArray = new byte[message.limit()];
		message.get(destinationArray);
		String iecPacket = (new String(destinationArray)).trim();

		if (!iecPacket.contains(ROUTE_START_TAG)
				&& !iecPacket.contains(ROUTE_END_TAG)) {
			// if iecPacket has no start or ending tag append it to buffer if
			// start tag exists
			if (iecMessageBuffer.contains(ROUTE_START_TAG)) {
				iecMessageBuffer += iecPacket;
			}
		} else {
			for (String iecMessage : iecPacket.split("(?=" + ROUTE_START_TAG
					+ ")|(?<=" + ROUTE_END_TAG + ")")) {
				if (iecMessage.contains(ROUTE_START_TAG)
						&& iecMessage.contains(ROUTE_END_TAG)) {
					// Start and end tag exists, so message can be parsed
					this.parser.parse(iecMessage);

					// reset after parsing
					iecMessageBuffer = "";
					waitingForEndTag = false;

					// check if iec element is created
					if (this.parser.getIEC() != null) {
						this.iec = this.parser.getIEC();
						this.parser.resetParser();
						KeyValueObject<? extends IMetaAttribute> map = this.iec
								.toMap();
						map.setMetadata("object", iec);
						getTransfer().transfer(map);
					}
				} else {
					if (iecMessage.contains(ROUTE_START_TAG)) {
						// iec message only contains start_tag so we need to
						// buffer this
						iecMessageBuffer = iecMessage;
					} else if (iecMessage.contains(ROUTE_END_TAG)) {
						// iec message only contains start_tag so we need to
						// prepend it on buffer
						iecMessageBuffer += iecMessage;
						if (iecMessageBuffer.contains(ROUTE_START_TAG)
								&& iecMessageBuffer.contains(ROUTE_END_TAG)) {
							// Start and end tag exists, so buffer can be parsed
							this.parser.parse(iecMessageBuffer);

							// reset after parsing
							iecMessageBuffer = "";
							waitingForEndTag = false;

							// check if iec element is created
							if (this.parser.getIEC() != null) {
								this.iec = this.parser.getIEC();
								this.parser.resetParser();
								KeyValueObject<? extends IMetaAttribute> map = this.iec
										.toMap();
								map.setMetadata("object", iec);
								getTransfer().transfer(map);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> object)
			throws IOException {
		try {
			Object obj = object.getMetadata("object");
			if (!(obj instanceof Route)) {
				return;
			}
			this.iec = (Route) obj;
			getTransportHandler().send(this.iec.toXML().getBytes());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
