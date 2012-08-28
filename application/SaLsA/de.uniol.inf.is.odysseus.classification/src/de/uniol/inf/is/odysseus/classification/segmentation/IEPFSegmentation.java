package de.uniol.inf.is.odysseus.classification.segmentation;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class IEPFSegmentation extends AbstractFunction<Geometry> {
    /**
     * 
     */
    private static final long           serialVersionUID = 4973220087210280849L;

    public static final SDFDatatype[][] accTypes         = new SDFDatatype[][] {
            { SDFSpatialDatatype.SPATIAL_MULTI_POINT }, { SDFDatatype.DOUBLE } };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A matrix and a threashold.");
        }

        return IEPFSegmentation.accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "IEPFSegmentation";
    }

    @Override
    public MultiLineString getValue() {
        return null;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING;
    }

}
