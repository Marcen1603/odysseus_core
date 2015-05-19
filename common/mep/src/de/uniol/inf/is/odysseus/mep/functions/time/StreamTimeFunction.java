/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import de.uniol.inf.is.odysseus.core.IHasAlias;
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
@Deprecated
public class StreamTimeFunction extends AbstractFunction<Long> implements IHasAlias{
	
    private static final long serialVersionUID = -9167197876743665507L;

    public StreamTimeFunction() {
        this("streamtime");
    }

    protected StreamTimeFunction(String name) {
        super(name, 0, new SDFDatatype[0][0], SDFDatatype.LONG, false);
    }

    @Override
    public Long getValue() {
        PointInTime streamTime = ((ITimeInterval) getMetaAttribute()).getStart();
        return streamTime.getMainPoint();
    }

	@Override
	public String getAliasName() {
		return "now";
	}

}
