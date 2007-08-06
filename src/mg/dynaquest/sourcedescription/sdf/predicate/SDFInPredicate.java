package mg.dynaquest.sourcedescription.sdf.predicate;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class SDFInPredicate extends SDFSimplePredicate {

    /**
	 * @uml.property  name="elements"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFConstantList elements;

	protected SDFInPredicate(String URI, SDFAttribute attribute,
			SDFConstantList elements) {
		super(URI, attribute);
		this.elements = elements;
	}

	protected SDFConstant getObjectElement(int index) {
		return this.elements.getConstant(index);
	}

	public int getNoOfElements() {
		return this.elements.size();
	}

	public String toString() {
		return super.toString() + " IN (" + elements.toString() + ")";
	}
	
	
    /* (non-Javadoc)
     * @see mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate#getValue()
     */
    public SDFConstant getValue() {
        throw new NotImplementedException();
    }

	@Override
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		xmlRetValue.append(indent);
		xmlRetValue.append("<inPredicate>\n");
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("<attribute>");
		xmlRetValue.append(getAttribute().getURI(false));
		xmlRetValue.append("</attribute>\n");
		
		xmlRetValue.append(indent);
		xmlRetValue.append(indent);
		xmlRetValue.append("<constantList>");
		elements.getXMLRepresentation(indent+indent+indent, xmlRetValue);
		xmlRetValue.append("</constantList>\n");
		
		xmlRetValue.append(indent);
		xmlRetValue.append("</numberInPredicate>\n");
		
	}
    
    
}