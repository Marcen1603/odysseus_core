/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TimelinessFunction extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 4703040530419998760L;

    public TimelinessFunction() {
    	super("timeliness",1,ACC_TYPES,SDFProbabilisticDatatype.PROBABILISTIC_BYTE,false);
    }
    
  
    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final Double getValue() {
        Objects.requireNonNull(this.getInputValue(0));
        Objects.requireNonNull(getMetaAttribute());
        double frequency = this.getNumericalInputValue(0);
        PointInTime applicationTime = PointInTime.currentPointInTime();
        PointInTime streamTime = ((ITimeInterval) getMetaAttribute()).getStart();
        PointInTime difference = applicationTime.minus(streamTime);
        double timeliness = (1.0 - difference.getMainPoint() / (1000.0 / frequency));
        if (timeliness < 0.0) {
            timeliness = 0.0;
        }
        return timeliness;
    }

 

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = {SDFDatatype.NUMBERS,SDFDatatype.NUMBERS};

 
}
