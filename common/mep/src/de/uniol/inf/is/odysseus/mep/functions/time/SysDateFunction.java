/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Returns the application time
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class SysDateFunction extends AbstractFunction<Date> {

     /**
     * 
     */
    private static final long serialVersionUID = -3364300386518966013L;
    private static final SDFDatatype[] accTypes = new SDFDatatype[] {};

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException(
                    "negative argument index not allowed");
        }
        if (argPos >= 0) {
            throw new IllegalArgumentException(this.getSymbol()
                    + " has no argument(s).");
        }
        return accTypes;
    }

    @Override
    public String getSymbol() {
        return "sysdate";
    }

    @Override
    public Date getValue() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DATE;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean optimizeConstantParameter() {
        return false;
    }

}
