/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Marco Grawunder
 * 
 */
public class ToBooleanFunction extends AbstractFunction<Boolean> {

    private static final long serialVersionUID = 6172939691360862021L;

    public ToBooleanFunction() {
    	super("toBoolean",1,SDFDatatype.BOOLEAN);
    }
   
    @Override
    public Boolean getValue() {
        String s = getInputValue(0).toString();
        return Boolean.parseBoolean(s);
    }

}
