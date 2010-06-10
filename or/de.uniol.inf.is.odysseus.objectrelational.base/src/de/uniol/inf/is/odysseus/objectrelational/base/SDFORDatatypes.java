package de.uniol.inf.is.odysseus.objectrelational.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class SDFORDatatypes extends SDFDatatypes {

    public static String Set = baseURI + "Set";
    
    public static boolean isSet(SDFDatatype type) {
        return type.getURI(false).equals("Set");
    }
}
