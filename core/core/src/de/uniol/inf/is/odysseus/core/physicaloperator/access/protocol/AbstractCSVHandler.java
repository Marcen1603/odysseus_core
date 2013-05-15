package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.text.DecimalFormat;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class AbstractCSVHandler<T> extends LineProtocolHandler<T> {

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
		delimiter = options.containsKey("csv.delimiter") ? options.get("csv.delimiter")
				.toCharArray()[0] : ",".toCharArray()[0];
	    if (options.containsKey("csv.floatingformatter")){
	    	floatingFormatter = new DecimalFormat(options.get("csv.floatingformatter"));
	    }
	    if (options.containsKey("csv.numberformatter")){
	    	numberFormatter = new DecimalFormat(options.get("csv.numberformatter"));
	    }
	    if (options.containsKey("csv.writemetadata")){
	    	writeMetadata = Boolean.parseBoolean(options.get("csv.writemetadata"));
	    }
	    
	}

}
