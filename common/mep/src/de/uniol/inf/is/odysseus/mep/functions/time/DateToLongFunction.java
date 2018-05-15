/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Date;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DateToLongFunction extends AbstractUnaryDateFunction<Long> implements IHasAlias{

    private static final long serialVersionUID = -776275642994567805L;


    public DateToLongFunction() {
    	super("toLong",SDFDatatype.LONG);
	}
    
    @Override
    public String getAliasName() {
    	return "DateInMillis";
    }
    
    @Override
    public Long getValue() {
        Date date = (Date) getInputValue(0);
        return date.getTime();
    }


}
