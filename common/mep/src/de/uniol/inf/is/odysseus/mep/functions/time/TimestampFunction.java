/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Returns the stream time
 * 
 * @author Christian Kuka <christian@kuka.cc>, Marco Grawunder
 * 
 */
public class TimestampFunction extends AbstractFunction<Long> {

    /**
     * 
     */
    private static final long serialVersionUID = -9167197876743665507L;
    private static final SDFDatatype[] accTypes = SDFDatatype.getTypes().toArray(new SDFDatatype[1]); 

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        return accTypes;
    }

    @Override
    public String getSymbol() {
        return "timestamp";
    }

    @Override
    public Long getValue() {
        PointInTime streamTime = ((ITimeInterval) getInputMetadata(0)).getStart();
        return streamTime.getMainPoint();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.LONG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean optimizeConstantParameter() {
        return false;
    }

}
