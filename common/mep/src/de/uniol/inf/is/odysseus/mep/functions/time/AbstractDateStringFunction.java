/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING }, { SDFDatatype.STRING } };

    public AbstractDateStringFunction(String symbol) {
    	super(symbol, 2, accTypes, SDFDatatype.INTEGER);
    }
    
    protected DateFormat getDateTimeFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }
}
