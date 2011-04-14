package de.uniol.inf.is.odysseus.sparql.parser.helper;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AmgigiousAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * This attribute resolver is the same as the DirectAttributeResolver
 * apart from one difference:
 * 
 * The method getAttribute does not look for ambigious attributes. This
 * is, because in SPARQL attributes with the same name always have the
 * same value. In SPARQL only equi joins are allowed.
 * 
 * @author André Bolles
 *
 */
public class SPARQLDirectAttributeResolver extends DirectAttributeResolver{

    public SPARQLDirectAttributeResolver(SDFAttributeList schema) {
        super(schema);
    }

    public SPARQLDirectAttributeResolver(
            DirectAttributeResolver directAttributeResolver) {
        super(directAttributeResolver);
    }
    
    /**
     * Returns the first attribute with the name "name".
     */
    @Override
	public SDFAttribute getAttribute(String name)
            throws AmgigiousAttributeException, NoSuchAttributeException {
        String[] parts = name.split("\\.", 2);
        SDFAttribute found = null;
        for (SDFAttribute attr : schema) {
            if (parts.length == 1) {
                if ((attr).getAttributeName().equals(name)) {
                    return attr;
                }
            }
            else {
                if (attr.getPointURI().equals(name)) {
                    return attr;
                }
            }
        }
        if (found == null) {
            throw new NoSuchAttributeException(name);
        }
        return found;
    }

}
