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
public class DateToLongFunction extends AbstractFunction<Long> {

    /**
     * 
     */
    private static final long serialVersionUID = -776275642994567805L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFDatatype.DATE } };

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
        return "toLong";
    }

    @Override
    public Long getValue() {
        Date date = (Date) getInputValue(0);
        return date.getTime();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.LONG;
    }

}
