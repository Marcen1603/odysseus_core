/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Computes the month difference between two dates
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MonthsFunction extends AbstractFunction<Integer> {

    /**
     * 
     */
    private static final long serialVersionUID = -4615281071966679283L;
    private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DATE };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public String getSymbol() {
        return "months";
    }

    @Override
    public Integer getValue() {
        Calendar a = Calendar.getInstance();
        a.setTime((Date) getInputValue(0));
        Calendar b = Calendar.getInstance();
        b.setTime((Date) getInputValue(1));
        int months = 0;
        while ((a.before(b)) || (a.equals(b))) {
            months++;
            a.add(Calendar.MONTH, 1);
        }
        if (months > 0) {
            months--;
        }
        return months;
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
