package de.uniol.inf.is.odysseus.core;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;

public class ConversionOptions {

	private static final String DEFAULT_CHARSET = "UTF-8";
	public static final String DELIMITER = "delimiter";
	public static final String CSV_DELIMITER = "csv.delimiter";
	public static final String TEXT_DELIMITER = "textdelimiter";
	public static final String CSV_TEXT_DELIMITER = "csv.textdelimiter";
	public static final String CSV_FLOATING_FORMATTER = "csv.floatingformatter";
	
	public static final String DECIMAL_SEPARATOR = "decimalseparator";
	public static final String EXPONENT_SEPARATOR = "exponentseparator";
	public static final String GROUPING_SEPARATOR = "groupingseparator";
	
	public static final String CSV_NUMBER_FORMATTER = "csv.numberformatter";
	public static final String NULL_VALUE_TEXT = "nullvaluetext";
	public static final String CHARSET = "charset";

	public static final ConversionOptions defaultOptions = new ConversionOptions(',', (Character)null,
			(NumberFormat) null, (NumberFormat) null);

	final private char delimiter;
	final private NumberFormat floatingFormatter;
	final private NumberFormat numberFormatter;

	final private Character textSeperator;
	final String nullValueString;
	final private Charset charset;
	final CharsetDecoder decoder;
	final CharsetEncoder encoder;

	public ConversionOptions(char delimiter, Character textSeperator, NumberFormat floatingFormatter,
			NumberFormat numberFormatter) {
		super();

		this.charset = Charset.forName(DEFAULT_CHARSET);
		this.decoder = charset.newDecoder();
		this.encoder = charset.newEncoder();

		this.delimiter = delimiter;
		this.textSeperator = textSeperator;
		this.floatingFormatter = floatingFormatter;
		this.numberFormatter = numberFormatter;
		this.nullValueString = "null";

	}

	public ConversionOptions(OptionMap optionsMap) {
		super();

		this.charset = Charset.forName(optionsMap.get(CHARSET, DEFAULT_CHARSET));
		this.decoder = charset.newDecoder();
		this.encoder = charset.newEncoder();

		if (optionsMap.containsKey(ConversionOptions.DELIMITER)) {
			delimiter = determineDelimiter(optionsMap.get(ConversionOptions.DELIMITER));
		} else if (optionsMap.containsKey(ConversionOptions.CSV_DELIMITER)) {
			delimiter = determineDelimiter(optionsMap.get(ConversionOptions.CSV_DELIMITER));
		} else {
			delimiter = ',';
		}
		
		DecimalFormatSymbols dfs = createDFS(optionsMap);

		if (optionsMap.containsKey(ConversionOptions.CSV_FLOATING_FORMATTER)) {
			floatingFormatter = new DecimalFormat(optionsMap.get(ConversionOptions.CSV_FLOATING_FORMATTER),dfs);		
		}else {
			floatingFormatter = null;
		}
		
		
		if (optionsMap.containsKey(ConversionOptions.CSV_NUMBER_FORMATTER)) {
			numberFormatter = new DecimalFormat(optionsMap.get(ConversionOptions.CSV_NUMBER_FORMATTER), dfs);
		}else {
			numberFormatter = null;
		}

	
		
		this.textSeperator = optionsMap.getCharacter(TEXT_DELIMITER, null);
		
		this.nullValueString = optionsMap.get(NULL_VALUE_TEXT, "");

	}

	private DecimalFormatSymbols createDFS(OptionMap optionsMap) {
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		
		if (optionsMap.containsKey(ConversionOptions.DECIMAL_SEPARATOR)) {
			dfs.setDecimalSeparator(optionsMap.getChar(ConversionOptions.DECIMAL_SEPARATOR,'.'));
		}
		if (optionsMap.containsKey(ConversionOptions.EXPONENT_SEPARATOR)) {
			dfs.setDecimalSeparator(optionsMap.getChar(ConversionOptions.EXPONENT_SEPARATOR,'e'));
		}
		if (optionsMap.containsKey(ConversionOptions.GROUPING_SEPARATOR)) {
			dfs.setDecimalSeparator(optionsMap.getChar(ConversionOptions.GROUPING_SEPARATOR,','));
		}
		return dfs;
	}

	public ConversionOptions(ConversionOptions convOpts, OptionMap optionsMap) {
		if (optionsMap.containsKey(CHARSET)) {
			this.charset = Charset.forName(optionsMap.get(CHARSET));
		}else{
			this.charset = convOpts.charset;
		}
		this.decoder = charset.newDecoder();
		this.encoder = charset.newEncoder();

		if (optionsMap.containsKey(ConversionOptions.DELIMITER)) {
			delimiter = determineDelimiter(optionsMap.get(ConversionOptions.DELIMITER));
		} else if (optionsMap.containsKey(ConversionOptions.CSV_DELIMITER)) {
			delimiter = determineDelimiter(optionsMap.get(ConversionOptions.CSV_DELIMITER));
		} else {
			delimiter = convOpts.delimiter;
		}
		
		DecimalFormatSymbols dfs = createDFS(optionsMap);
		
		if (optionsMap.containsKey(ConversionOptions.CSV_FLOATING_FORMATTER)) {
			floatingFormatter = new DecimalFormat(optionsMap.get(ConversionOptions.CSV_FLOATING_FORMATTER), dfs);
		}else {
			floatingFormatter = convOpts.floatingFormatter;
		}
		if (optionsMap.containsKey(ConversionOptions.CSV_NUMBER_FORMATTER)) {
			numberFormatter = new DecimalFormat(optionsMap.get(ConversionOptions.CSV_NUMBER_FORMATTER), dfs);
		}else {
			numberFormatter = convOpts.numberFormatter;
		}

		Character tmp = optionsMap.getCharacter(TEXT_DELIMITER, convOpts.textSeperator);
		
		if (tmp == null) {
			this.textSeperator = '\"';
		}else {
			this.textSeperator = tmp;
		}
		
		this.nullValueString = optionsMap.get(NULL_VALUE_TEXT, convOpts.nullValueString);
	}

	public String getNullValueString() {
		return nullValueString;
	}

	public char getDelimiter() {
		return delimiter;
	}

	public Character getTextSeperator() {
		return textSeperator;
	}

	public boolean hasTextSeperator() {
		return textSeperator != null;
	}

	public NumberFormat getFloatingFormatter() {
		return floatingFormatter;
	}

	public boolean hasFloatingFormatter() {
		return floatingFormatter != null;
	}

	public NumberFormat getNumberFormatter() {
		return numberFormatter;
	}

	public boolean hasNumberFormatter() {
		return numberFormatter != null;
	}

	public Charset getCharset() {
		return charset;
	}

	public CharsetDecoder getDecoder() {
		return decoder;
	}

	public CharsetEncoder getEncoder() {
		return encoder;
	}

	static public char determineDelimiter(String v) {
		char ret;
		if (v.equals("\\t")) {
			ret = '\t';
		} else {
			ret = v.toCharArray()[0];
		}
		return ret;
	}

}
