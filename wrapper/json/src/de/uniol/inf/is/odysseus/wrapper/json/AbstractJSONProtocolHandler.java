package de.uniol.inf.is.odysseus.wrapper.json;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

abstract public class AbstractJSONProtocolHandler<T extends KeyValueObject<?>> extends AbstractProtocolHandler<T> {

	protected String name;

	protected InputStreamReader reader;
	protected ObjectMapper mapper;
	String next = null;

	protected boolean isDone = false;

	static Logger LOG = LoggerFactory.getLogger(AbstractJSONProtocolHandler.class);

	public AbstractJSONProtocolHandler() {
	}

	public AbstractJSONProtocolHandler(ITransportDirection direction, IAccessPattern access,
			IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (this.getDirection() != null && this.getDirection() == ITransportDirection.IN) {
			if (this.getAccessPattern() == IAccessPattern.PULL) {
				reader = new InputStreamReader(getTransportHandler().getInputStream());
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

	@Override
	public boolean hasNext() throws IOException {
		if (next == null) {
			next = getNextJsonObjectOrArray();
		}

		return !(next == null);
	}

	@Override
	public T getNext() throws IOException {
		if (hasNext()) {
			return getDataHandler().readData(getNextJsonObjectOrArray());
		}
		return null;
	}

	/**
	 * <p>
	 * Reads the next complete JSON object/array from InputStreamReader. This
	 * method skips all chars of the stream reader until the start of a JSON
	 * object/array.
	 *
	 * <p>
	 * Note: Finding the next JSON object/array in the input stream fails when
	 * the input stream starts in between two quotation marks of a string value
	 * that has an opening bracket.
	 *
	 * Specification for read in: http://json.org/
	 *
	 * @return The next complete JSON object or array as string or {@code null}
	 *         when no next object or array exists.
	 */
	private String getNextJsonObjectOrArray() throws IOException {
		if (next != null) {
			String returnValue = next;
			next = null;
			return returnValue;
		}
		if (reader == null) {
			LOG.error("Reader is null.");
			return null;
		}
		if (!reader.ready()) {
			isDone = true;
			return null;
		}

		synchronized (this.reader) {
			try {
				StringBuilder str = new StringBuilder();
				StringBuilder skippedChars = new StringBuilder();

				char c = readNextChar(reader);

				// skip all chars that are not '{' or '[' (start of JSON
				// objects)
				while (c != '{' && c != '[') {
					if (!Character.isWhitespace(c)) {
						skippedChars.append(c);
					}
					c = readNextChar(reader);
				}
				if (skippedChars.length() > 0 && !"".equals(skippedChars.toString().trim()))
					LOG.warn("Skipped chars: " + skippedChars.toString());

				str.append(c);

				int openCurlyBrackets = 0;
				int openSquareBrackets = 0;

				if (c == '{') {
					++openCurlyBrackets;
				} else if (c == '[') {
					++openSquareBrackets;
				}

				// Read all chars until the end of the object. The end of the
				// object is reached then all open brackets are closed.
				while (openCurlyBrackets != 0 || openSquareBrackets != 0) {
					c = readNextChar(reader);
					str.append(c);
					switch (c) {
					case '{':
						++openCurlyBrackets;
						break;
					case '}':
						--openCurlyBrackets;
						break;
					case '[':
						++openSquareBrackets;
						break;
					case ']':
						--openSquareBrackets;
						break;
					case '"':
						// skip string value
						// JSON specification allows only double but not single
						// quotation marks for string values, should this
						// implementation also support single marks?
						char prev = ' ';
						do {
							prev = c;
							c = readNextChar(reader);
							str.append(c);
						} while (!(c == '"' && prev != '\\'));
						break;
					default:
						break;
					}
				}

				return str.toString();
			} catch (IllegalStateException e) {
				isDone = true;
				return null;
			}
		}
	}

	private char readNextChar(InputStreamReader reader) throws IllegalStateException, IOException {
		int data = reader.read();
		if (data == -1) {
			// LOG.info("End of stream.");
			throw new IllegalStateException("End of stream.");
		}
		return (char) data;
	}

	public void process(ArrayList<T> objects) {
		if (objects.size() > 0) {
			for (T object : objects) {
				if (object != null) {
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
		if (this.getDirection() != null && this.getDirection() == ITransportDirection.IN) {
			return ITransportExchangePattern.InOnly;
		}
		return ITransportExchangePattern.OutOnly;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}
}
