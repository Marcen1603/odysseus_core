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

    private static final long serialVersionUID = -9167197876743665507L;

    protected TimestampFunction(String name) {
    	super(name,1,getAllTypes(1),SDFDatatype.LONG);
    }
    
    public TimestampFunction(){
    	this("timestamp");
    }
    
    @Override
    public Long getValue() {
        PointInTime streamTime = ((ITimeInterval) getInputMetadata(0)).getStart();
        return streamTime.getMainPoint();
    }

}
