/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
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
    		String timezone = (String)getInputValue(2);
    		init(dateFormatString, timezone);
    	}
        return dateFormat.format(date);
    }

	private void init(String dateFormatString, String timezone) {
		dateFormat = new SimpleDateFormat(dateFormatString);
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
	}

    @Override
    public SDFDatatype determineType(IMepExpression<?>[] args) {
    	// just used to init dateFormat
    	if (args.length == 3 && args[0] != null){
    		init((String)args[1].getValue(), (String)args[2].getValue());
    	}
    	return super.determineType(args);
    }
    
}
