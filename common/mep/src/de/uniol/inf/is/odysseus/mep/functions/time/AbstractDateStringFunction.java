/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.text.DateFormat;
import java.util.Locale;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractDateStringFunction extends AbstractFunction<Integer> {

    /**
     * 
     */
    private static final long serialVersionUID = -5679329641413535288L;
    private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.STRING };
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);

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
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s): a date string");
        }
        return accTypes;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.INTEGER;
    }

    protected DateFormat getDateFormat() {
        return dateFormat;
    }
}
