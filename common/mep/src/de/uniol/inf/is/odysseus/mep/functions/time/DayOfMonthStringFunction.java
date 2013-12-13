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

    /**
     * 
     */
    private static final long serialVersionUID = 75715072917626307L;

    @Override
    public String getSymbol() {
        return "dayofmonth";
    }

    @Override
    public Integer getValue() {
        Date date = null;
        try {
            date = getDateFormat().parse(getInputValue(0).toString());
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean optimizeConstantParameter() {
        return true;
    }

}
