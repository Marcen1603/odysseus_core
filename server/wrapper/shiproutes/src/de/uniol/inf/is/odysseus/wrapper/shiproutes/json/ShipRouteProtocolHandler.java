package de.uniol.inf.is.odysseus.wrapper.shiproutes.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteRootElement;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre.ManoeuvrePlanDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction.PredictionDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route.RouteDataItem;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.enums.DataItemTypes;

public class ShipRouteProtocolHandler extends
		AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {

	private final Logger LOG = LoggerFactory
			.getLogger(ShipRouteProtocolHandler.class);
	private static final String PROTOCOL_HANDLER_NAME = "SHIP_ROUTES";

	private KeyValueObject<? extends IMetaAttribute> next = null;

	private BufferedReader reader;
	private int delay;
	private List<IShipRouteRootElement> shipRouteRootElements;
	private int counter;
	private String iecMessageBuffer = "";

	public ShipRouteProtocolHandler() {

	}

	public ShipRouteProtocolHandler(ITransportDirection direction,
			IAccessPattern access,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler,
			OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		shipRouteRootElements = new ArrayList<IShipRouteRootElement>();
		if (optionsMap.containsKey("delay")) {
			delay = Integer.parseInt(optionsMap.get("delay"));
		}
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			OptionMap options,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		ShipRouteProtocolHandler instance = new ShipRouteProtocolHandler(
				direction, access, dataHandler, options);
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

		if (parseRouteElement(currentString)) {
			if (!shipRouteRootElements.isEmpty()) {
				this.next = this.shipRouteRootElements.get(0).toMap();
				this.next.setMetadata("object",
						this.shipRouteRootElements.get(0));
				shipRouteRootElements.clear();
				return true;
			}
		}

		return false;
	}

	private boolean parseRouteElement(String jsonString) {
		StringTokenizer tokenizer = new StringTokenizer(jsonString, "{}", true);
		while (tokenizer.hasMoreElements()) {
			String element = (String) tokenizer.nextElement();
			if (element.contains("{")) {
				counter++;
			} else if (element.contains("}")) {
				counter--;
			}

			iecMessageBuffer += element;

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
						shipRouteRootElements.add(gson.fromJson(
								iecMessageBuffer, RouteDataItem.class));
						break;
					case MPlan:
						shipRouteRootElements.add(gson.fromJson(
								iecMessageBuffer, ManoeuvrePlanDataItem.class));
						break;
					case Prediction:
						shipRouteRootElements.add(gson.fromJson(
								iecMessageBuffer, PredictionDataItem.class));
						break;
					default:
						break;
					}
				}

				iecMessageBuffer = "";
			}
		}
		return !shipRouteRootElements.isEmpty();
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

		if (parseRouteElement(shipRoutePacket)) {
			if (!shipRouteRootElements.isEmpty()) {
				for (IShipRouteRootElement element : shipRouteRootElements) {
					KeyValueObject<? extends IMetaAttribute> map = element
							.toMap();
					map.setMetadata("object", element);
					getTransfer().transfer(map);
				}
				shipRouteRootElements.clear();
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
			IShipRouteRootElement element = (IShipRouteRootElement) obj;
			Gson gson = new Gson();
			String jsonString = gson.toJson(element);

			getTransportHandler().send(jsonString.getBytes());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
