package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

abstract public class AbstractCSVHandler<T> extends LineProtocolHandler<T> {

	protected char delimiter;
	protected char textDelimiter;
	protected DecimalFormat floatingFormatter;
	protected DecimalFormat numberFormatter;
	protected boolean writeMetadata;

	public AbstractCSVHandler(ITransportDirection direction,
			IAccessPattern access) {
		super(direction, access);
	}

	public AbstractCSVHandler() {
		super();
	}

	@Override
	protected void init(Map<String, String> options) {
		super.init(options);
		delimiter = options.containsKey("delimiter") ? options.get("delimiter")
				.toCharArray()[0] : ",".toCharArray()[0];
		delimiter = options.containsKey("csv.delimiter") ? options.get(
				"csv.delimiter").toCharArray()[0] : ",".toCharArray()[0];
		textDelimiter = options.containsKey("textdelimiter") ? options.get(
				"textdelimiter").toCharArray()[0] : "'".toCharArray()[0];
		textDelimiter = options.containsKey("csv.textdelimiter") ? options.get(
				"csv.textdelimiter").toCharArray()[0] : "'".toCharArray()[0];

		if (options.containsKey("csv.floatingformatter")) {
			floatingFormatter = new DecimalFormat(
					options.get("csv.floatingformatter"));
		}
		if (options.containsKey("csv.numberformatter")) {
			numberFormatter = new DecimalFormat(
					options.get("csv.numberformatter"));
		}
		if (options.containsKey("csv.writemetadata")) {
			writeMetadata = Boolean.parseBoolean(options
					.get("csv.writemetadata"));
		}

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
		String line = super.getNextLine();
		if (line != null) {
			return readLine(line);
		}
		return null;
	}
	
	@Override
	public void process(ByteBuffer message) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(message.array())));
		if (!firstLineSkipped && !readFirstLine) {
			try {
				reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			firstLineSkipped = true;
		}
		String line;
		try {
			line = reader.readLine();
			if (line != null) {
				T retValue = readLine(line);
				System.out.println(retValue);
				getTransfer().transfer(retValue);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected abstract T readLine(String line);

	@Override
	public void write(T object) throws IOException {
		StringBuilder out = new StringBuilder();
		getDataHandler().writeCSVData(out, object, delimiter, textDelimiter,
				floatingFormatter, numberFormatter, writeMetadata);
		writer.write(out.toString() + System.lineSeparator());
	}

}
