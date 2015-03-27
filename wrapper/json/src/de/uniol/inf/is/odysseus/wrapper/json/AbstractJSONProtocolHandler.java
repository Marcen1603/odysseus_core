package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
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

	public AbstractJSONProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (this.getDirection() == ITransportDirection.IN) {
			if (this.getAccessPattern() == IAccessPattern.PULL) {
				reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
			} 
		}
		isDone = false;
	}

	@Override
	public void close() throws IOException {
		if (reader != null) {
			reader.close();
		}
		super.close();
	}

	/**
	 * Specification for read in: http://json.org/json-de.html
	 */
	@Override
	public boolean hasNext() throws IOException {
		if (jsonArray != null && jsonArray.size() > 0) {
			return true;
		}
		try {
			if (!reader.ready()) {
				throw new IOException("Reader in AbstractJSONProtocolHandler not ready (could be caused by end of file)");
			}
			if (jsonArray == null) {
				jsonArray = new ArrayList<String>();
			}
			
			JsonParser jp = mapper.getFactory().createParser(reader);
			while(jp.nextToken() != null) {
				if(jp.getCurrentToken() == JsonToken.START_OBJECT) {
					jsonArray.add(parseJsonObject(jp));
				} else if(jp.getCurrentToken() == JsonToken.START_ARRAY) {
					while(jp.nextToken() != JsonToken.END_ARRAY) {
						if(jp.getCurrentToken() == JsonToken.START_OBJECT) {
							jsonArray.add(parseJsonObject(jp));
						} else {
							LOG.debug("Wrong JSON format? File starts with array, but no object is following.");
							return false;
						}
					}
				} else {
					LOG.debug("Data didn't begin with Json-Object or Json-Array! Trying to find one of them...");
				}
			}
			jp.close();
			if(jsonArray.size() > 0) {
				return true;   
			}
		} catch (IOException e) {
//			e.printStackTrace();
			LOG.debug(e.getMessage());
			this.isDone = true;
		} 
		return false;

	}
		
	private String parseJsonObject(JsonParser jp) throws JsonGenerationException, IOException {
		StringWriter stringWriter = new StringWriter();
		JsonGenerator jg = mapper.getFactory().createGenerator(stringWriter);
		jg.writeStartObject();
		while (jp.nextToken() != JsonToken.END_OBJECT) {
			jg.writeFieldName(jp.getCurrentName());
			if(jp.nextToken() == JsonToken.START_OBJECT) {
				jg.writeRawValue(this.parseJsonObject(jp));
			} else if(jp.getCurrentToken() == JsonToken.START_ARRAY) {
				this.parseArray(jp, jg);
			} else {
				if(jp.getCurrentToken().isNumeric()) {
					NumberType numberType = jp.getNumberType();
					if (numberType == NumberType.LONG) {
						jg.writeNumber((Long) jp.getNumberValue());
					} else {
						jg.writeNumber((Integer) jp.getNumberValue());
					}
				} else if (jp.getCurrentToken().isBoolean()) {
					jg.writeBoolean(jp.getBooleanValue());
				} else if(jp.getCurrentToken() == null) {
					jg.writeNull();
				} else 	{
					jg.writeString(jp.getText());
				}
			}
		}
		jg.writeEndObject();
		jg.flush();
		String retVal = stringWriter.toString();
		jg.close();
		return retVal;
	}
	
	private void parseArray(JsonParser jp, JsonGenerator jg) throws JsonGenerationException, IOException {
		jg.writeStartArray();
		while (jp.nextToken() != JsonToken.END_ARRAY) {
			if(jp.getCurrentToken().isNumeric()) {
				jg.writeNumber((Integer) jp.getNumberValue());
			} else if (jp.getCurrentToken().isBoolean()) {
				jg.writeBoolean(jp.getBooleanValue());
			} else if(jp.getCurrentToken() == null) {
				jg.writeNull();
			} else 	{
				jg.writeString(jp.getText());
			}
		}
		jg.writeEndArray();
	}

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
