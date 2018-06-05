/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class WeekStringFunction extends AbstractDateStringFunction {


    private static final long serialVersionUID = 1291666144673163996L;

    public WeekStringFunction() {
    	super("week");
    }
    
    @Override
    public Integer getValue() {
        Date date = null;
        try {
           date = getDateTimeFormat(getInputValue(1).toString()).parse(getInputValue(0).toString());
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

}
