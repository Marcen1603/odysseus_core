package mg.dynaquest.sourcedescription.sdf.description;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

public class SDFOutputPattern extends SDFPattern {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1444364953190428439L;

	public SDFOutputPattern(){
		super();
	}
	
	public SDFOutputPattern(String outputPatternURI) {
		super(outputPatternURI);
	}
	
	public SDFOutputPattern(SDFPattern pattern) {
		super(pattern);
	}
	
    /**
     * @return
     */
    public SDFAttributeList getSortAscAttributes() {
    	SDFAttributeList sortedAttribs = new SDFAttributeList();
		// Zur Zeit gibt es nur eines, für später Schnittstelle trotzdem
		// so. Reihenfolge im Ausgabemuster ist Sortierreihenfolge
		for (int i = 0; i < getAttributeAttributeBindingPairCount(); i++) {
		    SDFOutputAttributeBinding oAttributeBinding = (SDFOutputAttributeBinding) getAttributeAttributeBindingPair(i).getAttributeBinding();
		    if (oAttributeBinding != null){
		        if (oAttributeBinding.isSortedAsc()){
		            sortedAttribs.add(getAttributeAttributeBindingPair(i).getAttribute());
		        }
		        
		    }
		}
		return sortedAttribs;
    }
	
	
    /**
     * 
     */
    public static SDFOutputPattern union(SDFOutputPattern leftOutputPattern, SDFOutputPattern rightOutputPattern) 
    		throws SDFPatternNotCompatible{
        SDFOutputPattern newPattern = new SDFOutputPattern(leftOutputPattern);
        doUnion(rightOutputPattern, newPattern);
        return newPattern;
    }
    
    
}
