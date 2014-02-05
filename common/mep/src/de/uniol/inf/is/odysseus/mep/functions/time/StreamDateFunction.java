/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Returns the stream time
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class StreamDateFunction extends AbstractFunction<Date> {

    /**
     * 
     */
    private static final long serialVersionUID = -9167197876743665507L;
    private static final SDFDatatype[] accTypes = new SDFDatatype[] {};

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= 0) {
            throw new IllegalArgumentException(this.getSymbol() + " has no argument(s).");
        }
        return accTypes;
    }

    @Override
    public String getSymbol() {
        return "streamdate";
    }

    @Override
    public Date getValue() {
        PointInTime streamTime = ((ITimeInterval) getMetaAttribute()).getStart();
        return new Date(TimeUnit.MILLISECONDS.convert(streamTime.getMainPoint(),getBaseTimeUnit()));
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean optimizeConstantParameter() {
        return false;
    }

}
