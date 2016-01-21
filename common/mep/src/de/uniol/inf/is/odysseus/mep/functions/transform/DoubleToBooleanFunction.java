/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @deprecated Use {@link ToBooleanFromNumberFunction}
 */
@Deprecated
public class DoubleToBooleanFunction extends AbstractFunction<Boolean> {

   
    private static final long serialVersionUID = -5506465071311430245L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { {SDFDatatype.DOUBLE} };

    public DoubleToBooleanFunction() {
    	super("doubleToBoolean",1,accTypes,SDFDatatype.BOOLEAN);
    }
    
    @Override
    public Boolean getValue() {
        return getNumericalInputValue(0).doubleValue() != 0.0;
    }
}
