/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class MilliSecondStringFunction extends AbstractFunction<Long> {

    /**
     * 
     */
    private static final long serialVersionUID = -5034926903287470775L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING }, { SDFDatatype.STRING } };

    public MilliSecondStringFunction() {
        super("millisecond", 2, accTypes, SDFDatatype.LONG);
    }

    @Override
    public Long getValue() {
        Date date = null;
        try {
            date = getDateTimeFormat(getInputValue(1).toString()).parse(getInputValue(0).toString());
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long mills = calendar.getTimeInMillis();
        return mills;
    }

    private static DateFormat getDateTimeFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }
}
