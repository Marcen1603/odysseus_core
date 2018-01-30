package de.uniol.inf.is.odysseus.core;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;

public class WriteOptions extends ConversionOptions{
	
	public static final WriteOptions defaultOptions = new WriteOptions(',', (Character)null, (NumberFormat)null,(NumberFormat)null);
	public static final WriteOptions defaultOptions2 = new WriteOptions(';', '\'', (DecimalFormat) null, (DecimalFormat) null); 

	public static final String CSV_WRITE_HEADING = "csv.writeheading";

	final private boolean writeHeading;
	
	private WriteOptions(char delimiter, Character textSeperator,
			NumberFormat floatingFormatter, NumberFormat numberFormatter) {
		super(delimiter, textSeperator, floatingFormatter, numberFormatter);
		this.writeHeading = false;
	}
	
	public WriteOptions(OptionMap optionsMap) {
		super(optionsMap);
		this.writeHeading = optionsMap.getBoolean(CSV_WRITE_HEADING, false);
	}
	
	public boolean isWriteHeading() {
		return writeHeading;
	}
	

}
