package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

abstract public class AbstractCSVHandler<T extends IStreamObject<IMetaAttribute>> extends LineProtocolHandler<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCSVHandler.class);

	protected char delimiter;
	protected Character textDelimiter = null;
	protected DecimalFormat floatingFormatter;
	protected DecimalFormat numberFormatter;
	protected boolean trim = false;
	protected boolean addLineNumber = false;
	protected String delimiterString;
	protected WriteOptions writeOptions;

	private boolean notWrittenFirstLine = true;

	public static final String DELIMITER = "delimiter";
	public static final String CSV_DELIMITER = "csv.delimiter";
	public static final String TEXT_DELIMITER = "textdelimiter";
	public static final String CSV_TEXT_DELIMITER = "csv.textdelimiter";
	public static final String CSV_FLOATING_FORMATTER = "csv.floatingformatter";
	public static final String CSV_NUMBER_FORMATTER = "csv.numberformatter";
	public static final String CSV_WRITE_METADATA = "csv.writemetadata";
	public static final String CSV_TRIM = "csv.trim";
	public static final String ADDLINENUMBERS = "addlinenumber";
	public static final String NULL_VALUE_TEXT = "nullvaluetext";
	public static final String CSV_WRITE_HEADING = "csv.writeheading";

	static byte[] newline;

	static {
		StringBuilder out = new StringBuilder();
		out.append(System.lineSeparator());
		CharBuffer cb = CharBuffer.wrap(out);
		ByteBuffer encoded = charset.encode(cb);
		byte[] encodedBytes1 = encoded.array();
		newline = new byte[cb.limit()];
		System.arraycopy(encodedBytes1, 0, newline, 0, cb.limit());
	}

	public AbstractCSVHandler() {
		super();
	}

	public AbstractCSVHandler(ITransportDirection direction, IAccessPattern access,
			IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		init_internal();
	}

	@Override
	void optionsMapChanged(String key, String value) {
		// simply update
		init_internal();
		super.optionsMapChanged(key, value);
	}

	private void init_internal() {
		OptionMap options = optionsMap;
		if (options.containsKey(DELIMITER)) {
			delimiter = CSVParser.determineDelimiter(options.get(DELIMITER));
		} else if (options.containsKey(CSV_DELIMITER)) {
			delimiter = CSVParser.determineDelimiter(options.get(CSV_DELIMITER));
		} else {
			delimiter = ',';
		}
		// only calc once
		delimiterString = Character.toString(delimiter);
		if (options.containsKey(CSV_FLOATING_FORMATTER)) {
			floatingFormatter = new DecimalFormat(options.get(CSV_FLOATING_FORMATTER));
		}
		if (options.containsKey(CSV_NUMBER_FORMATTER)) {
			numberFormatter = new DecimalFormat(options.get(CSV_NUMBER_FORMATTER));
		}
		if (options.containsKey(CSV_WRITE_METADATA)) {
			INFOSERVICE.error(CSV_WRITE_METADATA+" no longer supported. Use 'writeMetadata' in SENDER instead. Meta data will not be written!", new IllegalArgumentException("Unsupported Parameter"));
		}
		if (options.containsKey(CSV_TRIM)) {
			trim = Boolean.parseBoolean(options.get(CSV_TRIM));
		}
		if (options.get(ADDLINENUMBERS) != null) {
			addLineNumber = Boolean.parseBoolean(options.get(ADDLINENUMBERS));
		}

		writeOptions = new WriteOptions(delimiter, textDelimiter, floatingFormatter, numberFormatter);

		if (options.containsKey(NULL_VALUE_TEXT)) {
			writeOptions.setNullValueString(options.get(NULL_VALUE_TEXT));
		}

		writeOptions.setWriteHeading(options.getBoolean(CSV_WRITE_HEADING, false));
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		notWrittenFirstLine = true;
		super.open();
	}

	@Override
	public T getNext() throws IOException {
		String line = super.getNextLine(reader);
		if (line != null) {
			if (addLineNumber) {
				line = super.lineCounter + delimiterString + line;
			}
			return readLine(line);
		}
		return null;
	}

	final T readLine(String line) {
		return readLine(line, getDataHandler().isHandleMetadata());
	}

	protected abstract T readLine(String line, boolean readMeta);

	@Override
	public void write(T object) throws IOException {
		StringBuilder out = new StringBuilder();
		if (writeOptions.isWriteHeading() && notWrittenFirstLine) {
			notWrittenFirstLine = false;
			writeHeadings(out);
		}
		getDataHandler().writeCSVData(out, object, writeOptions);
		CharBuffer cb = CharBuffer.wrap(out);
		ByteBuffer encoded = charset.encode(cb);
		byte[] encodedBytes1 = encoded.array();
		byte[] encodedBytes = new byte[cb.limit() + newline.length];
		System.arraycopy(encodedBytes1, 0, encodedBytes, 0, cb.limit());
		System.arraycopy(newline, 0, encodedBytes, cb.limit(), newline.length);
		getTransportHandler().send(encodedBytes);
	}

	private void writeHeadings(StringBuilder out) {
		SDFSchema schema = getDataHandler().getSchema();
		if (schema.getType().equals(Tuple.class)){
			out.append(schema.getAttribute(0).getURI(true));
			for (int i=1;i<schema.size();i++){
				out.append(writeOptions.getDelimiter());
				out.append(schema.getAttribute(i).getURI(true));
			}
			out.append(System.lineSeparator());
		}else{
			LOG.warn("Cannot create heading with schema "+schema);
		}
	}

	@Override
	protected void process(String token) {
		T retValue = readLine(token);
		getTransfer().transfer(retValue);
	}

}
