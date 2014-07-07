package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

abstract public class AbstractJSONProtocolHandler<T extends KeyValueObject<?>> extends AbstractProtocolHandler<T> {

	protected String name;

	protected BufferedReader reader;
	protected ArrayList<String> jsonArray;
	protected ObjectMapper mapper;

	protected boolean isDone = false;

	static Logger LOG = LoggerFactory.getLogger(AbstractJSONProtocolHandler.class);

	public AbstractJSONProtocolHandler() {
	}

	public AbstractJSONProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<T> dataHandler) {
		super(direction, access, dataHandler);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (this.getDirection() == ITransportDirection.IN) {
			if (this.getAccessPattern() == IAccessPattern.PULL) {
				reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
			} else {
				// ?
			}
		}
	}

	@Override
	public void close() throws IOException {
		if (reader != null) {
			reader.close();
		}
		super.close();
	}

	@Override
	public boolean hasNext() throws IOException {
		if (jsonArray != null && jsonArray.size() > 0) {
			return true;
		}
		try {
			if (!reader.ready()) {
				throw new IOException("Reader not ready (could be caused by end of file)");
			}
			if (jsonArray == null) {
				jsonArray = new ArrayList<String>();
			}

			JsonNode rootNode = mapper.readValue(reader, JsonNode.class);
			if (rootNode.isArray()) {
				for (JsonNode node : rootNode) {
					jsonArray.add(node.toString());
				}
				return true;
			} else if (rootNode.isObject()) {
				jsonArray.add(rootNode.toString());
				return true;
			} else {
				return false;
			}

			// Something like this should be a bit faster...
			// JsonFactory jsonFactory = mapper.getFactory();
			// JsonParser jp =
			// jsonFactory.createParser(getTransportHandler().getInputStream());
			// if(jp.nextToken() == JsonToken.START_ARRAY) {
			// while(jp.nextToken() != JsonToken.END_ARRAY) {
			// if(jp.getCurrentToken() == JsonToken.START_OBJECT) {
			// String string = this.parseObject(jp);
			// logger.debug(string);
			// jsonArray.add(string);
			// }
			// }
			// } else if(jp.getCurrentToken() == JsonToken.START_OBJECT) {
			// String string = this.parseObject(jp);
			// logger.debug(string);
			// jsonArray.add(string);
			// } else {
			// throw new
			// IOException("Invalid JSON data - data begin neither with an array nor with an object");
			// }
			// jp.close();
		} catch (IOException e) {
			LOG.debug(e.getMessage());
			this.isDone = true;
		}
		return false;

	}

	// private String parseObject(JsonParser jp) throws IOException {
	// String string = "";
	// while (jp.nextToken() != JsonToken.END_OBJECT) {
	// if(jp.getCurrentToken() == JsonToken.END_ARRAY) {
	// throw new
	// IOException("Invalid JSON data - array ends before all objects ended");
	// }
	// // string += jp.getCurrentName();
	// string += jp.getValueAsString();
	// }
	// return string;
	// }

	@Override
	public T getNext() throws IOException {
		if (jsonArray != null && jsonArray.size() > 0) {
			return getDataHandler().readData(jsonArray.remove(0));
		}
		return null;
	}

	@Override
	public void process(String[] message) {
		getTransfer().transfer(getDataHandler().readData(message));
	}

	public void process(ArrayList<T> objects) {
		if (objects.size() > 0) {
			for (T object : objects) {
				if(object != null) {
					getTransfer().transfer(object);
				}
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof JSONProtocolHandler)) {
			return false;
		}
		// the datahandler was already checked in the
		// isSemanticallyEqual-Method of AbstracProtocolHandler
		return true;
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection() == ITransportDirection.IN) {
			return ITransportExchangePattern.InOnly;
		}
		return ITransportExchangePattern.OutOnly;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}
}
