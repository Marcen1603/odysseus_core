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
public class MinutesFunction extends AbstractFunction<Integer> {

    private static final long serialVersionUID = 2515314773204038929L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {{ SDFDatatype.DATE },{ SDFDatatype.DATE }};

    public MinutesFunction() {
    	super("minutes",2,accTypes, SDFDatatype.INTEGER);
    }
    
    @Override
    public Integer getValue() {
        Date a = (Date) getInputValue(0);
        Date b = (Date) getInputValue(1);

        return (int) ((b.getTime() - a.getTime()) / 1000 / 60);
    }

}
