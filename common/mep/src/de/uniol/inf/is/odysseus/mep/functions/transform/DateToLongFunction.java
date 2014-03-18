/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.transform;

import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.time.AbstractUnaryDateFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DateToLongFunction extends AbstractUnaryDateFunction<Long> {

    private static final long serialVersionUID = -776275642994567805L;


    public DateToLongFunction() {
    	super("toLong",SDFDatatype.LONG);
	}
    
    @Override
    public Long getValue() {
        Date date = (Date) getInputValue(0);
        return date.getTime();
    }


}
