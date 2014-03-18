/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class BusinessDaysFunction extends AbstractFunction<Integer> {

    private static final long serialVersionUID = -5640513088700897936L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[]{SDFDatatype.DATE},new SDFDatatype[]{SDFDatatype.DATE} };

    public BusinessDaysFunction() {
    	super("businessDays",2,accTypes, SDFDatatype.INTEGER);
    }
    
    @Override
    public Integer getValue() {
        Calendar a = Calendar.getInstance();
        a.setTime((Date) getInputValue(0));
        Calendar b = Calendar.getInstance();
        b.setTime((Date) getInputValue(1));

        int days = 0;
        Calendar tmpStart = (Calendar) a.clone();
        Calendar tmpEnd = (Calendar) b.clone();

        if (a.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            tmpStart.add(Calendar.DAY_OF_MONTH, 2);
        }
        else if (a.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            tmpStart.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (b.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            tmpEnd.add(Calendar.DAY_OF_MONTH, -1);
        }
        else if (b.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            tmpEnd.add(Calendar.DAY_OF_MONTH, -2);
        }

        Calendar tmpDuration = (Calendar) tmpStart.clone();
        while (tmpDuration.compareTo(tmpEnd) <= 0) {
            days++;
            tmpDuration.add(Calendar.DATE, 1);
        }
        if (days > 0) {
            days--;
        }
        int businessDays = (days / 7) * 5;
        tmpStart.add(Calendar.DAY_OF_MONTH, (days / 7) * 7);
        while (tmpStart.compareTo(tmpEnd) <= 0) {
            if ((tmpStart.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) && (tmpStart.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
                businessDays++;
            }
            tmpStart.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (businessDays > 0) {
            businessDays--;
        }
        return businessDays;
    }

}
