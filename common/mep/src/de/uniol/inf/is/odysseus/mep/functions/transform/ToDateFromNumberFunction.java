/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.transform;

import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToDateFromNumberFunction extends AbstractFunction<Date> {
    /**
     * 
     */
    private static final long serialVersionUID = 5676069632517178837L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS };

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s): a date");
        }
        return accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "toDate";
    }

    @Override
    public Date getValue() {
        Number dateNumber = getNumericalInputValue(0);
        return new Date(dateNumber.longValue());
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DATE;
    }

}
