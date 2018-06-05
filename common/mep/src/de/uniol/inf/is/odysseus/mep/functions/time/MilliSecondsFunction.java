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
public class MilliSecondsFunction extends AbstractFunction<Long> {

    /**
     * 
     */
    private static final long serialVersionUID = 2900489679406729858L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.DATE }, { SDFDatatype.DATE } };

    public MilliSecondsFunction() {
        super("milliseconds", 2, accTypes, SDFDatatype.LONG);
    }

    @Override
    public Long getValue() {
        Date a = (Date) getInputValue(0);
        Date b = (Date) getInputValue(1);

        return new Long((b.getTime() - a.getTime()));
    }

}
