package de.uniol.inf.is.odysseus.salsa.util;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public final class TupleConverter {
    /**
     * @param data
     * @param datatype
     * @return
     */
    public static Object convert(final Object data, final SDFDatatype datatype) {
        if (datatype.getQualName().equalsIgnoreCase("Double")) {
            if (data == null) {
                return 0.0;
            }
            return Double.parseDouble(data.toString());
        }
        else if (datatype.getQualName().equalsIgnoreCase("Boolean")) {
            if (data == null) {
                return false;
            }
            return Boolean.parseBoolean(data.toString());
        }
        else {
            return data != null ? data.toString() : null;
        }
    }

    private TupleConverter() {
    }
}
