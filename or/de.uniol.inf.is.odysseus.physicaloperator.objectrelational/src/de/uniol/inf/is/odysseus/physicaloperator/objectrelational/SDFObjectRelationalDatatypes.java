package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class SDFObjectRelationalDatatypes extends SDFDatatypes {

    public static String Set = baseURI + "Set";
    
    public static boolean isSet(SDFDatatype type) {
        return type.getURI(false).equals("Set");
    }
}
