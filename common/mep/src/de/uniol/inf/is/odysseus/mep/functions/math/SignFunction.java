/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryNumberInputFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class SignFunction extends AbstractUnaryNumberInputFunction<Double> {

 
    private static final long serialVersionUID = 5921326492861280908L;

    public SignFunction() {
    	super("sign", SDFDatatype.DOUBLE);
	}
    
    @Override
    public Double getValue() {
        return Math.signum(getNumericalInputValue(0));
    }
 
}
