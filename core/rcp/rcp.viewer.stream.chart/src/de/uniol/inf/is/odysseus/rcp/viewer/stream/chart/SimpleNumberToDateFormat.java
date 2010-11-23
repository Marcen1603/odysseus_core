package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleNumberToDateFormat extends NumberFormat {
	
	private static final long serialVersionUID = -2329525234684181945L;
	
	private SimpleDateFormat sdf;

	public SimpleNumberToDateFormat(String datePattern){
		this.sdf = new SimpleDateFormat(datePattern);
	}
	
	
	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		long numberLong = (long)number;
		return this.format(numberLong, toAppendTo, pos);		
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		Date date = new Date(number);
		StringBuffer sb = sdf.format(date, toAppendTo, pos);
		return sb;
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		return this.sdf.parse(source, parsePosition).getTime();		
	}

}
