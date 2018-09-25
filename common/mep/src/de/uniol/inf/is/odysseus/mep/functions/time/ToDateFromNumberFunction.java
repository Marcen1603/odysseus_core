/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToDateFromNumberFunction extends AbstractFunction<Date> {
    private static final long serialVersionUID = 5676069632517178837L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS };

    public ToDateFromNumberFunction() {
    	super("toDate",1,accTypes,SDFDatatype.DATE);
    }
    
    @Override
    public Date getValue() {
    	Number dateNumber = getNumericalInputValue(0);
        return new Date(dateNumber.longValue());
    }

 }
