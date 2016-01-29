/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Marco Grawunder
 * 
 */
public class DateToStringFunction2 extends AbstractFunction<String> {

	private static final long serialVersionUID = -6154815301726927527L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.DATE }, { SDFDatatype.STRING },{ SDFDatatype.STRING } };

    SimpleDateFormat dateFormat = null;
    
    public DateToStringFunction2() {
    	super("toString",3,accTypes, SDFDatatype.STRING);
    }
    
    @Override
    public String getValue() {
    	Date date = (Date) getInputValue(0);
    	if (dateFormat == null){
    		String dateFormatString = (String) getInputValue(1);
    		dateFormat = new SimpleDateFormat(dateFormatString);
    		dateFormat.setTimeZone(TimeZone.getTimeZone((String)getInputValue(2)));
    	}
        return dateFormat.format(date);
    }

}
