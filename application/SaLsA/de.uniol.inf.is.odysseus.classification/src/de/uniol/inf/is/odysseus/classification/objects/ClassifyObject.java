package de.uniol.inf.is.odysseus.classification.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import de.uniol.inf.is.odysseus.classification.datatype.Classification;
import de.uniol.inf.is.odysseus.classification.sdf.schema.SDFClassificationDataype;
import de.uniol.inf.is.odysseus.classification.segmentation.IEPFSegmentation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClassifyObject extends AbstractFunction<List<List<Classification>>> {
    /**
     * 
     */
    private static final long           serialVersionUID = 7083439025768903560L;
    public static final SDFDatatype[][] accTypes         = new SDFDatatype[][] { { SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING } };

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A multi line string.");
        }

        return IEPFSegmentation.accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "ClassifyObject";
    }

    @Override
    public List<List<Classification>> getValue() {
        final MultiLineString segments = (MultiLineString) this.getInputValue(0);
        final List<List<Classification>> classifyObjects = new ArrayList<List<Classification>>();
        for (int i = 0; i < segments.getNumGeometries(); i++) {
            final LineString segment = (LineString) segments.getGeometryN(i);
            final TypeDetails typeDetails = ObjectRuleRegistry.getTypeDetails(segment);
            final List<Classification> object = new ArrayList<Classification>();
            final Iterator<IObjectType> iter = typeDetails.iterator();
            while (iter.hasNext()) {
                final IObjectType type = iter.next();
                final Geometry polygon = ObjectRuleRegistry.getPredictedPolygon(segment, type);
                final Classification classification = new Classification(polygon, type.toString(),
                        typeDetails.getTypeAffinity(type));
                object.add(classification);
            }
            classifyObjects.add(object);
        }
        return classifyObjects;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFClassificationDataype.CLASSIFY_OBJECT_LIST;
    }

}
