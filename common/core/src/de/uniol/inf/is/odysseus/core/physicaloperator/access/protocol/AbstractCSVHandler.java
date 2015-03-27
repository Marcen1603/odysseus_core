package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.text.DecimalFormat;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

abstract public class AbstractCSVHandler<T> extends LineProtocolHandler<T> {
	protected char delimiter;
	protected Character textDelimiter = null;
	protected DecimalFormat floatingFormatter;
	protected DecimalFormat numberFormatter;
	protected boolean writeMetadata;
	protected boolean trim = false;
	protected boolean addLineNumber = false;
	protected String delimiterString;
	private WriteOptions writeOptions;

	public static final String DELIMITER = "delimiter";
	public static final String CSV_DELIMITER = "csv.delimiter";
	public static final String TEXT_DELIMITER = "textdelimiter";
	public static final String CSV_TEXT_DELIMITER = "csv.textdelimiter";
	public static final String CSV_FLOATING_FORMATTER = "csv.floatingformatter";
	public static final String CSV_NUMBER_FORMATTER = "csv.numberformatter";
	public static final String CSV_WRITE_METADATA = "csv.writemetadata";
	public static final String CSV_TRIM = "csv.trim";
	public static final String ADDLINENUMBERS = "addlinenumber";

	public AbstractCSVHandler() {
		super();
	}	
	
	public AbstractCSVHandler(ITransportDirection direction,
			IAccessPattern access, IDataHandler<T> dataHandler,OptionMap optionsMap) {
		super(direction, access, dataHandler,optionsMap);
		init_internal();
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
			floatingFormatter = new DecimalFormat(
					options.get(CSV_FLOATING_FORMATTER));
		}
		if (options.containsKey(CSV_NUMBER_FORMATTER)) {
			numberFormatter = new DecimalFormat(
					options.get(CSV_NUMBER_FORMATTER));
		}
		if (options.containsKey(CSV_WRITE_METADATA)) {
			writeMetadata = Boolean.parseBoolean(options
					.get(CSV_WRITE_METADATA));
		}
		if (options.containsKey(CSV_TRIM)) {
			trim = Boolean.parseBoolean(options.get(CSV_TRIM));
		}
		if (options.get(ADDLINENUMBERS) != null) {
			addLineNumber = Boolean.parseBoolean(options.get(ADDLINENUMBERS));
		}

		writeOptions = new WriteOptions(delimiter, textDelimiter,
		floatingFormatter, numberFormatter, writeMetadata);
	}
	
	@Override
	public void onConnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

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

	protected abstract T readLine(String line);

	@Override
	public void write(T object) throws IOException {
		StringBuilder out = new StringBuilder();
		getDataHandler().writeCSVData(out, object, writeOptions);
		out.append(System.lineSeparator());
		CharBuffer cb = CharBuffer.wrap(out);
		ByteBuffer encoded = charset.encode(cb);
		byte[] encodedBytes1 = encoded.array();
		byte[] encodedBytes = new byte[cb.limit()];
		System.arraycopy(encodedBytes1, 0, encodedBytes, 0, cb.limit());
		getTransportHandler().send(encodedBytes);
	}
	
	@Override
	protected void process(String token) {
		T retValue = readLine(token);
		getTransfer().transfer(retValue);
	}


}
