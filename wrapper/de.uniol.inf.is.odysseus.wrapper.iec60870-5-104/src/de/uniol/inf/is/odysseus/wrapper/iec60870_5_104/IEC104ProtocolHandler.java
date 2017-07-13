package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.openmuc.j60870.APdu;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.internal.ConnectionSettings;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

// TODO javaDoc
public class IEC104ProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends AbstractProtocolHandler<T> {

	/**
	 * The name of the protocol handler for query languages.
	 */
	private static final String name = "IEC60870-5-104";

	/**
	 * The buffersize for encoding APdus. Taken from
	 * org.openmuc.j60870.Connection
	 */
	private static final int buffersize = 255;

	private final ConnectionSettings connSettings = new ConnectionSettings();

	public IEC104ProtocolHandler() {
		super();
	}

	public IEC104ProtocolHandler(ITransportDirection direction, IAccessPattern access,
			IStreamObjectDataHandler<T> datahandler, OptionMap optionsMap) {
		super(direction, access, datahandler, optionsMap);
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		return new IEC104ProtocolHandler<>(direction, access, dataHandler, options);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> obj) {
		return obj != null && obj instanceof IEC104ProtocolHandler;
	}

	private void transferASdu(ASdu asdu) {
		byte[] bytes = new byte[buffersize];
		int length = asdu.encode(bytes, 0, connSettings);
		ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, length);
		getTransfer().transfer(getDataHandler().readData(buffer));
	}

	@Override
	public void process(InputStream message) {
		try {
			transferASdu(new APdu(new DataInputStream(message), connSettings).getASdu());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		try {
			transferASdu(new APdu(new DataInputStream(new ByteBufferInputStream(message)), connSettings).getASdu());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void process(String[] message) {
		StringBuilder builder = new StringBuilder();
		Arrays.asList(message).stream().forEach(string -> builder.append(string + "\n"));
		try {
			transferASdu(
					new APdu(new DataInputStream(new ByteArrayInputStream(builder.toString().getBytes())), connSettings)
							.getASdu());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void process(T message) {
		getTransfer().transfer(message);
	}

	@Override
	public void process(T message, int port) {
		getTransfer().transfer(message, port);
	}

}