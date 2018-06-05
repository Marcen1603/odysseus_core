/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Marco Grawunder
 * 
 */
public abstract class AbstractDateLongFunction extends AbstractFunction<Date> {

    /**
     * 
     */
    private static final long serialVersionUID = -5679329641413535288L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING }, SDFDatatype.DISCRETE_NUMBERS  };

    public AbstractDateLongFunction(String symbol) {
    	super(symbol, 2, accTypes, SDFDatatype.DATE);
    }
    
}
