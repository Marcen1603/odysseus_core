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
public class DayStringFunction extends AbstractDateStringFunction {

    private static final long serialVersionUID = 6970261543869855556L;

    public DayStringFunction() {
    	super("day");
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
        return calendar.get(Calendar.DATE);
    }


}
