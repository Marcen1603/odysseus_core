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
public class DaysFunction extends AbstractFunction<Integer> {

    /**
     * 
     */
    private static final long serialVersionUID = 6462413832840844826L;
    private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DATE };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public String getSymbol() {
        return "days";
    }

    @Override
    public Integer getValue() {
        Calendar a = Calendar.getInstance();
        a.setTime((Date) getInputValue(0));
        Calendar b = Calendar.getInstance();
        b.setTime((Date) getInputValue(1));
        int days = 0;
        while (a.compareTo(b) <= 0) {
            days++;
            a.add(Calendar.DATE, 1);
        }
        if (days > 0) {
            days--;
        }
        return days;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s): two dates");
        }
        return accTypes;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.INTEGER;
    }

}
