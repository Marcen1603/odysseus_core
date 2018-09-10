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
public class HourStringFunction extends AbstractDateStringFunction {

    private static final long serialVersionUID = -6681108594943533499L;

    public HourStringFunction() {
    	super("hour");
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
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

}
