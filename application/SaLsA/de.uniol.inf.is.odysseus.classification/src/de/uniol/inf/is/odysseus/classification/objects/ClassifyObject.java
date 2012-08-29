package de.uniol.inf.is.odysseus.classification.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.classification.sdf.schema.SDFClassificationDataype;
import de.uniol.inf.is.odysseus.classification.segmentation.IEPFSegmentation;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClassifyObject extends AbstractFunction<List<List<Tuple<?>>>> {
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
    public List<List<Tuple<?>>> getValue() {
        final MultiLineString segments = (MultiLineString) this.getInputValue(0);
        final List<List<Tuple<?>>> classifyObjects = new ArrayList<List<Tuple<?>>>();
        for (int i = 0; i < segments.getNumGeometries(); i++) {
            final LineString segment = (LineString) segments.getGeometryN(i);
            final TypeDetails typeDetails = ObjectRuleRegistry.getTypeDetails(segment);
            final List<Tuple<?>> object = new ArrayList<Tuple<?>>();
            final Iterator<IObjectType> iter = typeDetails.iterator();
            while (iter.hasNext()) {
                final IObjectType type = iter.next();
                if (typeDetails.getTypeAffinity(type) > 0.0) {
                    final Polygon polygon = ObjectRuleRegistry.getPredictedPolygon(segment, type);
                    @SuppressWarnings("rawtypes")
                    final Tuple<?> tuple = new Tuple(3, false);
                    tuple.setAttribute(0, polygon);
                    tuple.setAttribute(1, type.toString());
                    tuple.setAttribute(2, typeDetails.getTypeAffinity(type));
                    object.add(tuple);
                }
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
