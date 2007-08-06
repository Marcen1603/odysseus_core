package mg.dynaquest.sourcedescription.sdf.description;

import java.util.ArrayList;
import mg.dynaquest.sourcedescription.sdf.description.SDFAttributeAttributeBindingPair;

public class SDFAttributeAttributeBindingPairList {

    /**
	 * @uml.property  name="attributeAttributeBindingPairs"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="mg.dynaquest.sourcedescription.sdf.description.SDFAttributeAttributeBindingPair"
	 */
    private ArrayList<SDFAttributeAttributeBindingPair> attributeAttributeBindingPairs;
    
	public SDFAttributeAttributeBindingPairList(SDFAttributeAttributeBindingPairList elements) {
        attributeAttributeBindingPairs = new ArrayList<SDFAttributeAttributeBindingPair>();
        attributeAttributeBindingPairs.addAll(elements.attributeAttributeBindingPairs);
    }

    public SDFAttributeAttributeBindingPairList() { 
        attributeAttributeBindingPairs = new ArrayList<SDFAttributeAttributeBindingPair>();
    }

    public void addAttributeAttributeBindingPair(SDFAttributeAttributeBindingPair aaPair) {
		attributeAttributeBindingPairs.add(aaPair);
	}

	public SDFAttributeAttributeBindingPair getAttributeAttributeBindingPair(int pos) {
		return (SDFAttributeAttributeBindingPair) attributeAttributeBindingPairs.get(pos);
	}

	public int getAttributeAttributeBindingPairCount() {
		return this.attributeAttributeBindingPairs.size();
	}

    
    public void removeAttributeAttributeBindingPair(int pos) 
    {
    	attributeAttributeBindingPairs.remove(pos);
	}

	
	public String toString() {
		StringBuffer ret = new StringBuffer("[");
		for (int i = 0; i < attributeAttributeBindingPairs.size(); i++) {
			ret.append(getAttributeAttributeBindingPair(i).getURI(true) + ",");
		}
		return ret.toString() + "]";
	}

}