package de.uniol.inf.is.odysseus.wrapper.shiproutes.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteRootElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.MPlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.enums.DataItemTypes;

public class ShipRouteProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {

	private final Logger LOG = LoggerFactory
			.getLogger(ShipRouteProtocolHandler.class);
	private static final String PROTOCOL_HANDLER_NAME = "SHIP_ROUTES";

	private KeyValueObject<? extends IMetaAttribute> next = null;

	private BufferedReader reader;
	private int delay;
	private IShipRouteRootElement shipRouteRootElement;
	private int counter;
	private String iecMessageBuffer = "";

	public ShipRouteProtocolHandler() {

	}

	public ShipRouteProtocolHandler(ITransportDirection direction,
			IAccessPattern access,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler);
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			Map<String, String> options,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		ShipRouteProtocolHandler instance = new ShipRouteProtocolHandler(
				direction, access, dataHandler);
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
		if (!(other instanceof ShipRouteProtocolHandler)) {
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

		if (parseRouteElement(currentString)){
			if (shipRouteRootElement != null) {
				this.next = this.shipRouteRootElement.toMap();
				this.next.setMetadata("object", shipRouteRootElement);
				return true;
			}			
		}

		return false;
	}

	private boolean parseRouteElement(String jsonString) {
		StringTokenizer tokenizer = new StringTokenizer(jsonString, "{}", true);
		while (tokenizer.hasMoreElements()) {
			String token = (String) tokenizer.nextElement();
			if (token.contains("{")) {
				counter++;
			} else if (token.contains("}")) {
				counter--;
			}

			iecMessageBuffer += token;

			if (counter == 0) {
				// if json message is complete
				JsonParser jp = new JsonParser();
				JsonElement jsonElement = jp.parse(iecMessageBuffer);
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				DataItemTypes itemType = DataItemTypes.parse(jsonObject.get(
						"data_item_id").getAsString());

				if (itemType != null) {
					Gson gson = new Gson();

					switch (itemType) {
					case Route:
						shipRouteRootElement = gson.fromJson(iecMessageBuffer,
								RouteDataItem.class);
						break;
					case Manoeuvre:
						shipRouteRootElement = gson.fromJson(iecMessageBuffer,
								MPlanDataItem.class);
						break;
					case Prediction:
						shipRouteRootElement = gson.fromJson(iecMessageBuffer,
								PredictionDataItem.class);
						break;
					default:
						break;
					}
				}

				iecMessageBuffer = "";
				return true;
			}
		}
		return false;
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
		String shipRoutePacket = (new String(destinationArray)).trim();

		if (parseRouteElement(shipRoutePacket)){
			if (shipRouteRootElement != null) {
				KeyValueObject<? extends IMetaAttribute> map = this.shipRouteRootElement.toMap();
				map.setMetadata("object", shipRouteRootElement);
				getTransfer().transfer(map);
			}			
		}

	}

	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> object)
			throws IOException {
		try {
			Object obj = object.getMetadata("object");
			if (!(obj instanceof IShipRouteRootElement)) {
				return;
			}
			this.shipRouteRootElement = (IShipRouteRootElement) obj;
			Gson gson = new Gson();
			String jsonString = gson.toJson(shipRouteRootElement);

			getTransportHandler().send(jsonString.getBytes());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
