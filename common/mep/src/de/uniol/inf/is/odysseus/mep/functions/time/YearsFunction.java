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
public class YearsFunction extends AbstractFunction<Integer> {

   /**
     * 
     */
    private static final long serialVersionUID = 4965582195235900439L;
private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DATE };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public String getSymbol() {
        return "years";
    }

    @Override
    public Integer getValue() {
        Calendar a = Calendar.getInstance();
        a.setTime((Date) getInputValue(0));
        Calendar b = Calendar.getInstance();
        b.setTime((Date) getInputValue(1));
        int years = 0;
        while ((a.before(b)) || (a.equals(b))) {
            years++;
            a.add(Calendar.YEAR, 1);
        }
        if (years > 0) {
            years--;
        }
        return years;
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
