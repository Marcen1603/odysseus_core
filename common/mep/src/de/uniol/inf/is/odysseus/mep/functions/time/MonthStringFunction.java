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
public class MonthStringFunction extends AbstractDateStringFunction {


    /**
     * 
     */
    private static final long serialVersionUID = -7178932074154225687L;

    @Override
    public String getSymbol() {
        return "month";
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
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean optimizeConstantParameter() {
        return true;
    }


}
