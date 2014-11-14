/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DateToStringFunction extends AbstractFunction<String> {

    /**
     * 
     */
    private static final long serialVersionUID = -4863774536996054256L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFDatatype.DATE }, new SDFDatatype[] { SDFDatatype.STRING } };

    public DateToStringFunction() {
    	super("toString",2,accTypes, SDFDatatype.STRING);
    }
    
    @Override
    public String getValue() {
        Date date = (Date) getInputValue(0);
        String dateFormatString = (String) getInputValue(1);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        return dateFormat.format(date);
    }

}
