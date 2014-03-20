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
public class StreamDateFunction2 extends AbstractFunction<Date> {

    private static final long serialVersionUID = -9167197876743665507L;

    public StreamDateFunction2() {
    	super("streamdate", 1, SDFDatatype.DATE);
    }
    
    @Override
    public Date getValue() {
        PointInTime streamTime;
        if (getArgument(0).isVariable()) {
            streamTime = ((ITimeInterval) getInputMetadata(0)).getStart();
        }
        else {
            streamTime = ((ITimeInterval) getMetaAttribute()).getStart();
        }
        return new Date(TimeUnit.MILLISECONDS.convert(streamTime.getMainPoint(), getBaseTimeUnit()));

    }

}
