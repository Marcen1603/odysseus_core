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
public class YearStringFunction extends AbstractDateStringFunction {


    /**
     * 
     */
    private static final long serialVersionUID = -1986704598369818575L;

    @Override
    public String getSymbol() {
        return "year";
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
        return calendar.get(Calendar.YEAR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean optimizeConstantParameter() {
        return true;
    }


}
