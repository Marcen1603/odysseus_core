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
public class DayOfMonthStringFunction extends AbstractDateStringFunction {

    private static final long serialVersionUID = 75715072917626307L;

    public DayOfMonthStringFunction() {
    	super("dayofmonth");
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
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

}
