/**
 * 
 */
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

/**
 * Protocol handler for SVM formated data.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SVMProtocolHandler<T extends Tuple<IMetaAttribute>> extends
		LineProtocolHandler<T> {

	private final Logger LOG = LoggerFactory
			.getLogger(SVMProtocolHandler.class);
	protected char delimiter;
	protected char textDelimiter;
	protected DecimalFormat floatingFormatter;
	protected DecimalFormat numberFormatter;
	protected boolean writeMetadata;
	protected boolean trim = false;
	protected boolean addLineNumber = false;
	protected String delimiterString;

	public static final String DELIMITER = "delimiter";
	public static final String SVM_DELIMITER = "svm.delimiter";
	public static final String TEXT_DELIMITER = "textdelimiter";
	public static final String SVM_TEXT_DELIMITER = "svm.textdelimiter";
	public static final String SVM_FLOATING_FORMATTER = "svm.floatingformatter";
	public static final String SVM_NUMBER_FORMATTER = "svm.numberformatter";
	public static final String SVM_WRITE_METADATA = "svm.writemetadata";
	public static final String SVM_TRIM = "svm.trim";
	public static final String ADDLINENUMBERS = "addlinenumber";

	public static final String NAME = "SVM";

	public SVMProtocolHandler() {
		super();
	}
	
	public SVMProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,
			OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		init_internal();
	}
	
	private void init_internal() {
		OptionMap options = optionsMap;
		if (options.containsKey(DELIMITER)) {
			delimiter = options.get(DELIMITER).toCharArray()[0];
		} else {
			delimiter = options.containsKey(SVM_DELIMITER) ? options.get(
					SVM_DELIMITER).toCharArray()[0] : " ".toCharArray()[0];
		}
		// only calc once
		delimiterString = Character.toString(delimiter);
		if (options.containsKey(SVM_FLOATING_FORMATTER)) {
			floatingFormatter = new DecimalFormat(
					options.get(SVM_FLOATING_FORMATTER));
		}
		if (options.containsKey(SVM_NUMBER_FORMATTER)) {
			numberFormatter = new DecimalFormat(
					options.get(SVM_NUMBER_FORMATTER));
		}
		if (options.containsKey(SVM_WRITE_METADATA)) {
			writeMetadata = Boolean.parseBoolean(options
					.get(SVM_WRITE_METADATA));
		}
		if (options.containsKey(SVM_TRIM)) {
			trim = Boolean.parseBoolean(options.get(SVM_TRIM));
		}
		if (options.get(ADDLINENUMBERS) != null) {
			addLineNumber = Boolean.parseBoolean(options.get(ADDLINENUMBERS));
		}
		if (options.containsKey(TEXT_DELIMITER)) {
			textDelimiter = options.get(TEXT_DELIMITER).toCharArray()[0];
		} else {
			textDelimiter = options.containsKey(SVM_TEXT_DELIMITER) ? options
					.get(SVM_TEXT_DELIMITER).toCharArray()[0] : "'"
					.toCharArray()[0];
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public T getNext() throws IOException {
		String line = super.getNextLine(reader);
		if (line != null) {
			return readLine(line);
		}
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void process(long callerId, ByteBuffer message) {
		// TODO: check if callerId is relevant
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(message.array())));
		if (!firstLineSkipped && !readFirstLine) {
			try {
				reader.readLine();
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
			firstLineSkipped = true;
		}
		String line;
		try {
			line = reader.readLine();
			if (line != null) {
				T retValue = readLine(line);
				getTransfer().transfer(retValue);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void write(T object) throws IOException {
		StringBuilder out = new StringBuilder();
		StringBuffer retBuff = new StringBuffer();

		if (object.getAttributes().length > 0) {
			for (int i = 0; i < object.getAttributes().length; ++i) {
				Object curAttribute = object.getAttributes()[i];
				if (curAttribute instanceof Number) {
					if ((((Number) curAttribute).doubleValue() != 0.0)
							&& (i > 0)) {
						if (i > 0) {
							retBuff.append(delimiter);
						}
						retBuff.append(i).append(":");
						if ((curAttribute instanceof Double || curAttribute instanceof Float)
								&& floatingFormatter != null) {
							retBuff.append(floatingFormatter
									.format(curAttribute));
						} else if (!((curAttribute instanceof Double || curAttribute instanceof Float))
								&& numberFormatter != null) {
							retBuff.append(numberFormatter.format(curAttribute));
						} else {
							retBuff.append(curAttribute);
						}
					} else if (i == 0) {
						if ((curAttribute instanceof Double || curAttribute instanceof Float)
								&& floatingFormatter != null) {
							retBuff.append(floatingFormatter
									.format(curAttribute));
						} else if (!((curAttribute instanceof Double || curAttribute instanceof Float))
								&& numberFormatter != null) {
							retBuff.append(numberFormatter.format(curAttribute));
						} else {
							retBuff.append(curAttribute);
						}
					}
				} else {
					retBuff.append(i).append(":");
					if (textDelimiter != 0 && curAttribute instanceof String) {
						retBuff.append(textDelimiter)
								.append(curAttribute.toString())
								.append(textDelimiter);
					} else {
						retBuff.append(curAttribute.toString());
					}
				}
			}
		} else {
			retBuff.append("null");
		}
		out.append(retBuff.toString());
		out.append(System.lineSeparator());
		getTransportHandler()
				.send(getCharset().encode(CharBuffer.wrap(out)).array());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		SVMProtocolHandler<T> instance = new SVMProtocolHandler<T>(direction,
				access, dataHandler, options);
		return instance;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return NAME;
	}

	protected T readLine(String line) {
		List<String> elements = new LinkedList<String>();
		StringBuffer elem = new StringBuffer();
		boolean overreadModus1 = false;
		boolean overreadModus2 = false;

		for (char c : line.toCharArray()) {

			if (c == textDelimiter) {
				overreadModus1 = !overreadModus1;
				// elem.append(c);
			} else {
				if (overreadModus1 || overreadModus2) {
					elem.append(c);
				} else {
					if (delimiter == c) {
						elements.add(elem.toString());
						elem = new StringBuffer();
					} else {
						elem.append(c);
					}
				}

			}
		}
		elements.add(elem.toString());
		String[] ret = new String[getDataHandler().getSchema().size()
				+ (addLineNumber ? 1 : 0)];
		Arrays.fill(ret, "0");
		int i = 0;
		if (addLineNumber) {
			ret[i++] = super.lineCounter + "";
		}
		if (trim) {
			ret[i++] = elements.get(0).trim();
		} else {
			ret[i++] = elements.get(0);
		}
		for (; i < elements.size(); i++) {
			String e[] = elements.get(i).split(":");
			int index = Integer.parseInt(e[0]);
			if (index < ret.length) {
				if (trim) {
					ret[index] = e[1].trim();
				} else {
					ret[index] = e[1];
				}
			}

		}
		List<String> toRead = Arrays.asList(ret);
		T retValue = getDataHandler().readData(toRead.iterator());
		return retValue;
	}

	// public static void main(String[] args) {
	// SVMProtocolHandler<Tuple> handler = new SVMProtocolHandler<Tuple>();
	// handler.init(new HashMap<String, String>());
	// handler.readLine("-1 1:1 4:4 5:5");
	// handler.readLine("1 1:6 2:5 3:4 4:3 5:2 6:1");
	// handler.readLine("1 7:1");
	// Tuple<?> t1 = new Tuple<>(new Object[] { -1, 1, 0, 0, 4, 5, 0, 0 },
	// false);
	// Tuple<?> t2 = new Tuple<>(new Object[] { 1, 6, 5, 4, 3, 2, 1, 0 },
	// false);
	// Tuple<?> t3 = new Tuple<>(new Object[] { 1, 0, 0, 0, 0, 0, 0, 1 },
	// false);
	//
	// try {
	// handler.write(t1);
	// handler.write(t2);
	// handler.write(t3);
	// }
	// catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}
