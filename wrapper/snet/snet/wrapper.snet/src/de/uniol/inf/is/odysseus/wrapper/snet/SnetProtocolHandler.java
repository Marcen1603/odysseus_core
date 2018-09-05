package de.uniol.inf.is.odysseus.wrapper.snet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.fraunhofer.iis.kom.wsn.messages.WSNMessageDefault;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.snet.physicaloperator.access.SnetTransportHandler;

public class SnetProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends AbstractProtocolHandler<T> {

	protected ArrayList<WSNMessageDefault> messages;

	public SnetProtocolHandler() {
	}

	public SnetProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,
			OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		SnetProtocolHandler<T> handler = new SnetProtocolHandler<T>(direction,
				access, dataHandler, options);
		return handler;
	}

	@Override
	public String getName() {
		return "snet";
	}

	@Override
	public boolean hasNext() throws IOException {
		if (messages.size() > 0) {
			return true;
		}
		return false;

	}

	@Override
	public void open() throws java.net.UnknownHostException, IOException {
		getTransportHandler().open();
		if (messages == null) {
			messages = ((SnetTransportHandler) getTransportHandler())
					.getInputMessages();
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

	@Override
	public T getNext() {
		WSNMessageDefault msg = messages.get(0);
		
		List<String> values = new ArrayList<String>(10);
		
		values.add(""+msg.getSource());
		values.add(""+msg.getSourceService());
		values.add(""+msg.getDestinationService());
		byte[] payload = msg.getData();

		int timestamp = (payload[payload.length - 26] & 0xFF)
				| ((payload[payload.length - 25] & 0xFF) << 8);
		// System.out.println("timestamp: " + timestamp);
		values.add("" + timestamp);

		int seq = (payload[payload.length - 24] & 0xFF)
				| ((payload[payload.length - 23] & 0xFF) << 8)
				| ((payload[payload.length - 22] & 0xFF) << 16)
				| ((payload[payload.length - 21] & 0xFF) << 24);
		// System.out.println("sequence: " + seq);
		values.add("" + seq);

		int ap = (payload[payload.length - 20] & 0xFF)
				| ((payload[payload.length - 19] & 0xFF) << 8)
				| ((payload[payload.length - 18] & 0xFF) << 16)
				| ((payload[payload.length - 17] & 0xFF) << 24);
		float apf = Float.intBitsToFloat(ap);
		// System.out.println("air pressure: " + apf);
		values.add("" + apf);

		int humidity = (payload[payload.length - 16] & 0xFF)
				| ((payload[payload.length - 15] & 0xFF) << 8)
				| ((payload[payload.length - 14] & 0xFF) << 16)
				| ((payload[payload.length - 3] & 0xFF) << 24);
		float humidityf = Float.intBitsToFloat(humidity);
		// System.out.println("humidity: " + humidityf);
		values.add("" + humidityf);

		int temperature = (payload[payload.length - 12] & 0xFF)
				| ((payload[payload.length - 11] & 0xFF) << 8)
				| ((payload[payload.length - 10] & 0xFF) << 16)
				| ((payload[payload.length - 9] & 0xFF) << 24);
		float temperaturef = Float.intBitsToFloat(temperature);
		// System.out.println("temperature: " + temperaturef);
		values.add("" + temperaturef);

		int co2 = (payload[payload.length - 8] & 0xFF)
				| ((payload[payload.length - 7] & 0xFF) << 8)
				| ((payload[payload.length - 6] & 0xFF) << 16)
				| ((payload[payload.length - 5] & 0xFF) << 24);
		float co2f = Float.intBitsToFloat(co2);
		// System.out.println("co2: " + co2f);
		values.add("" + co2f);

		int ec = (payload[payload.length - 4] & 0xFF)
				| ((payload[payload.length - 3] & 0xFF) << 8)
				| ((payload[payload.length - 2] & 0xFF) << 16)
				| ((payload[payload.length - 1] & 0xFF) << 24);
		float ecf = Float.intBitsToFloat(ec);
		// System.out.println("energy consumption: " + ecf);
		values.add("" + ecf);

		messages.remove(0);

		// System.out.println(values);
		return getDataHandler().readData(values.iterator());
	}

}
