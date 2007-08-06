package mg.dynaquest.sourcedescription.sdf.query;

import java.util.ArrayList;

/**
 * Nur eine Hilfsklasse, die eine Menge von gewichteten Attributen verwaltet
 */

public class WAttributeSet {

    /**
	 * @uml.property  name="attributes"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="mg.dynaquest.sourcedescription.sdf.query.WeightedAttribute"
	 */
    private ArrayList<WeightedAttribute> attributes;

	public WAttributeSet() {
		attributes = new ArrayList<WeightedAttribute>();
	}

	public void addWAttribute(WeightedAttribute attribute) {
		attributes.add(attribute);
	}

	public WeightedAttribute getAttribute(int pos) {
		return (WeightedAttribute) attributes.get(pos);
	}

	public int getAttributeCount() {
		return attributes.size();
	}

	public String toString() {
		StringBuffer ret = new StringBuffer("Gewichtete Attribute: ");
		for (int i = 0; i < getAttributeCount(); i++) {
			ret.append(this.getAttribute(i).toString()+" ");
		}
		return ret.toString();
	}

	//  public WeightedAttribute findAttribute(SDFAttribute attribute){
	//    return null;
	//  }

}