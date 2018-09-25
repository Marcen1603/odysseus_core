/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class DayOfMonthFunction extends AbstractUnaryDateFunction<Integer>{

    private static final long serialVersionUID = 2805980627339017456L;

    public DayOfMonthFunction() {
    	super("dayofmonth", SDFDatatype.INTEGER);
    }
    
    @Override
    public Integer getValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) getInputValue(0));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

}
