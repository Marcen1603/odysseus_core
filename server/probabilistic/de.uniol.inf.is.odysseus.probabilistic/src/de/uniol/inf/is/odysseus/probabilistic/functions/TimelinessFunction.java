/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@Deprecated
public class TimelinessFunction extends AbstractFunction<Double> {

	private static final InfoService INFO = InfoServiceFactory
			.getInfoService(TimelinessFunction.class);
    /**
     * 
     */
    private static final long serialVersionUID = 4703040530419998760L;

    public TimelinessFunction() {
        super("timeliness", 1, TimelinessFunction.ACC_TYPES, SDFDatatype.DOUBLE, false);
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final Double getValue() {
        Objects.requireNonNull(this.getInputValue(0));
        Objects.requireNonNull(this.getMetaAttribute());
        final double frequency = this.getNumericalInputValue(0);
        final PointInTime applicationTime = PointInTime.currentPointInTime();
        final PointInTime streamTime = ((ITimeInterval) this.getMetaAttribute()).getStart();
        final PointInTime difference = applicationTime.minus(streamTime);
        double timeliness = (1.0 - (difference.getMainPoint() / (1000.0 / frequency)));
        if (timeliness < 0.0) {
            timeliness = 0.0;
        }
        return timeliness;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = { SDFDatatype.NUMBERS };

}
