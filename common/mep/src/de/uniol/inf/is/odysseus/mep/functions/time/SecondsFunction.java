/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SecondsFunction extends AbstractFunction<Integer> {

    /**
     * 
     */
    private static final long serialVersionUID = 4363071806485599553L;
    private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DATE };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public String getSymbol() {
        return "seconds";
    }

    @Override
    public Integer getValue() {
        Date a = (Date) getInputValue(0);
        Date b = (Date) getInputValue(1);

        return (int) ((b.getTime() - a.getTime()) / 1000);
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
