/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class DayOfMonthFunction extends AbstractDateFunction{

    /**
     * 
     */
    private static final long serialVersionUID = 2805980627339017456L;


    @Override
    public String getSymbol() {
        return "dayofmonth";
    }

    @Override
    public Integer getValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) getInputValue(0));
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
