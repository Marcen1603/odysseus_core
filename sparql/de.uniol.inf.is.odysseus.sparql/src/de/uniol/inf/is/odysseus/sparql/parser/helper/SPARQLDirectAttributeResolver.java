package de.uniol.inf.is.odysseus.sparql.parser.helper;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AmgigiousAttributeException;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.NoSuchAttributeException;

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

    /**
	 * 
	 */
	private static final long serialVersionUID = 3220948295115144771L;

	public SPARQLDirectAttributeResolver(SDFSchema schema) {
        super(schema);
    }

    public SPARQLDirectAttributeResolver(
            DirectAttributeResolver directAttributeResolver) {
        super(directAttributeResolver);
    }
    
    /**
     * Returns the first attribute with the name "name". Source
     * information will be completely ignored.
     */
    @Override
	public SDFAttribute getAttribute(String name)
            throws AmgigiousAttributeException, NoSuchAttributeException {
    	String[] parts = name.split("\\.", 2);
    	SDFAttribute found = null;
        for (SDFAttribute attr : schema) {
        	if (parts.length == 1) {
	        	if ((attr).getAttributeName().equals(name)) {
	                found = attr;
	            	break;
	            }
        	}
        	// sourceName.attrName
        	else{
	        	if ((attr).getPointURI().equals(name)) {
	                found = attr;
	            	break;
	            }
        	}
        }
        if (found == null) {
        	System.err.println("ERROR, no such attribute " + name);
        	throw new NoSuchAttributeException(name);
            
        }
        return found;
    }
    
    public SPARQLDirectAttributeResolver clone(){
    	return new SPARQLDirectAttributeResolver(this);
    }

}
