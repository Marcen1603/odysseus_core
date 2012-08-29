package de.uniol.inf.is.odysseus.classification.udf;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;

/**
 * User defined function to record and subtract the background from a scan point
 * cloud
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@UserDefinedFunction(name = "SubtractBackground")
public class SubtractBackground implements
        IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

    private int               pos;
    private long              time       = 0l;
    private long              start      = 0l;
    private PolarCoordinate[] background = null;

    /*
     * (non-Javadoc)
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction
     * #init(java.lang.String)
     */
    @Override
    public void init(String initString) {
        String[] args = initString.split(" ");
        for (String arg : args) {
            String[] nameValueArray = arg.split("=");
            String name = nameValueArray[0];
            String value = nameValueArray[1];
            if (name.equalsIgnoreCase("pos")) {
                this.pos = Integer.parseInt(value);
            }
            if (name.equalsIgnoreCase("time")) {
                this.time = Long.parseLong(value);
            }
        }

    }

    /*
     * (non-Javadoc)
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction
     * #process(java.lang.Object, int)
     */
    @Override
    public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) {
        Tuple<?> out;
        long tupleTime = ((ITimeInterval) in.getMetadata()).getStart().getMainPoint();
        if (this.start == 0l) {
            this.start = tupleTime;
        }
        if ((background == null) || ((tupleTime - this.start) < this.time)) {
            this.recordBackground(in, this.pos);
            out = in.clone();
            out.setAttribute(pos, new PolarCoordinate[0]);
        }
        else {
            out = this.subtractBackground(in, this.pos);
        }
        return out;
    }

    /*
     * (non-Javadoc)
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction
     * #getOutputMode()
     */
    @Override
    public OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    /**
     * Subtracts the recorded background from attribute at the given pos from
     * the given {@link Tuple tuple}
     * 
     * @param in
     *            The tuple
     * @param pos
     *            The attribute position
     * @return The tuple including the foreground
     */
    private Tuple<?> subtractBackground(Tuple<? extends IMetaAttribute> in, int pos) {
        Tuple<?> out = in.clone();
        PolarCoordinate[] points = (PolarCoordinate[]) in.getAttribute(pos);
        List<PolarCoordinate> foreground = new ArrayList<PolarCoordinate>();
        for (int i = 0; i < points.length; i++) {
            if (points[i].r < this.background[i].r) {
                foreground.add(points[i]);
            }
        }
        out.setAttribute(pos, foreground.toArray(new PolarCoordinate[foreground.size()]));
        return out;
    }

    /**
     * Records the given {@link PolarCoordinate polar coordinate} array
     * 
     * @param in
     *            The tuple
     * @param pos
     *            The attribute position
     */
    private void recordBackground(Tuple<? extends IMetaAttribute> in, int pos) {
        PolarCoordinate[] points = (PolarCoordinate[]) in.getAttribute(pos);
        if (this.background == null) {
            this.background = points.clone();
        }
        else {
            for (int i = 0; i < points.length; i++) {
                if (points[i].r < this.background[i].r) {
                    this.background[i] = points[i];
                }
            }
        }
    }
}
