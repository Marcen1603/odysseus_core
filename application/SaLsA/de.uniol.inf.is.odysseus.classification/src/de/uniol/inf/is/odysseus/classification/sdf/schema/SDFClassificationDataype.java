package de.uniol.inf.is.odysseus.classification.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SDFClassificationDataype extends SDFDatatype {
    /**
     * 
     */
    private static final long       serialVersionUID     = -7124737402682903059L;
    public static final SDFDatatype CLASSIFICATION       = new SDFClassificationDataype("Classification",
                                                                 SDFDatatype.KindOfDatatype.TUPLE, new SDFSchema("",
                                                                         new SDFAttribute(null, "polygon",
                                                                                 SDFSpatialDatatype.SPATIAL_POLYGON),
                                                                         new SDFAttribute(null, "class",
                                                                                 SDFDatatype.STRING), new SDFAttribute(
                                                                                 null, "affinity", SDFDatatype.DOUBLE)));
    public static final SDFDatatype CLASSIFY_OBJECT      = new SDFClassificationDataype("ClassifyObject",
                                                                 SDFDatatype.KindOfDatatype.MULTI_VALUE,
                                                                 SDFClassificationDataype.CLASSIFICATION);
    public static final SDFDatatype CLASSIFY_OBJECT_LIST = new SDFClassificationDataype("ClassifyObjectList",
                                                                 SDFDatatype.KindOfDatatype.MULTI_VALUE,
                                                                 SDFClassificationDataype.CLASSIFY_OBJECT);

    public SDFClassificationDataype(final String URI) {
        super(URI);
    }

    public SDFClassificationDataype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    public SDFClassificationDataype(final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
        super(datatypeName, type, schema);
    }

    public SDFClassificationDataype(final String datatypeName, final KindOfDatatype type, final SDFDatatype subType) {
        super(datatypeName, type, subType);
    }

    @Override
    public boolean compatibleTo(final SDFDatatype other) {
        if (other instanceof SDFClassificationDataype) {
            @SuppressWarnings("unused")
            final SDFClassificationDataype otherClassification = (SDFClassificationDataype) other;
            return true;
        }
        return super.compatibleTo(other);
    }
}
