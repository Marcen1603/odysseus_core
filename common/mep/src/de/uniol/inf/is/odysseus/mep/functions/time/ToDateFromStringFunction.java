package de.uniol.inf.is.odysseus.mep.functions.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToDateFromStringFunction extends AbstractFunction<Date> {

	private static final long serialVersionUID = 6255887477026357429L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			new SDFDatatype[] { SDFDatatype.STRING, SDFDatatype.START_TIMESTAMP_STRING },
			new SDFDatatype[] { SDFDatatype.STRING } };

	public ToDateFromStringFunction() {
		super("toDate", 2, accTypes, SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {
		String dateString = getInputValue(0).toString();
		String dateFormatString = getInputValue(1).toString();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
