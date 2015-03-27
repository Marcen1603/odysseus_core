package de.uniol.inf.is.odysseus.core;

import java.text.NumberFormat;

public class WriteOptions {
	
	// Standardfields
	char delimiter;
	Character textSeperator;
	NumberFormat floatingFormatter;
	NumberFormat numberFormatter;
	boolean withMetadata;
	
	// Add field, not via constructor
	String nullValueString = "";
	
	public WriteOptions(char delimiter, Character textSeperator,
			NumberFormat floatingFormatter, NumberFormat numberFormatter,
			boolean withMetadata) {
		super();
		this.delimiter = delimiter;
		this.textSeperator = textSeperator;
		this.floatingFormatter = floatingFormatter;
		this.numberFormatter = numberFormatter;
		this.withMetadata = withMetadata;
	}
	
	public void setNullValueString(String nullValueString) {
		this.nullValueString = nullValueString;
	}
	
	public String getNullValueString() {
		return nullValueString;
	}
	
	public char getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}
	public Character getTextSeperator() {
		return textSeperator;
	}
	public boolean hasTextSeperator(){
		return textSeperator != null;
	}
	public void setTextSeperator(Character textSeperator) {
		this.textSeperator = textSeperator;
	}
	public NumberFormat getFloatingFormatter() {
		return floatingFormatter;
	}
	public boolean hasFloatingFormatter(){
		return floatingFormatter != null;
	}
	public void setFloatingFormatter(NumberFormat floatingFormatter) {
		this.floatingFormatter = floatingFormatter;
	}
	public NumberFormat getNumberFormatter() {
		return numberFormatter;
	}
	public boolean hasNumberFormatter(){
		return numberFormatter != null;
	}
	public void setNumberFormatter(NumberFormat numberFormatter) {
		this.numberFormatter = numberFormatter;
	}
	public boolean isWithMetadata() {
		return withMetadata;
	}
	public void setWithMetadata(boolean withMetadata) {
		this.withMetadata = withMetadata;
	}
	
	

}
