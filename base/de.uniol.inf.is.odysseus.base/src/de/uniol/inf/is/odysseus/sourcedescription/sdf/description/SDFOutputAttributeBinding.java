package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;

public class SDFOutputAttributeBinding extends SDFAttributeBindung {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6633867899787070249L;

	/**
	 * @uml.property  name="idAttribute"
	 */
    boolean idAttribute = false;

    /**
	 * @uml.property  name="sortedAsc"
	 */
    boolean sortedAsc = false;

    /**
	 * @uml.property  name="sortedDesc"
	 */
    boolean sortedDesc = false;

    /**
     * @return Returns the idAttribute.
     * 
     * @uml.property name="idAttribute"
     */
    public synchronized boolean isIdAttribute() {
        return idAttribute;
    }

    /**
     * @param idAttribute The idAttribute to set.
     * 
     * @uml.property name="idAttribute"
     */
    public synchronized void setIdAttribute(boolean idAttribute) {
        this.idAttribute = idAttribute;
    }

    /**
     * @return Returns the sortedAsc.
     * 
     * @uml.property name="sortedAsc"
     */
    public synchronized boolean isSortedAsc() {
        return sortedAsc;
    }

    /**
     * @param sortedAsc The sortedAsc to set.
     * 
     * @uml.property name="sortedAsc"
     */
    public synchronized void setSortedAsc(boolean sortedAsc) {
        this.sortedAsc = sortedAsc;
    }

    /**
     * @return Returns the sortedDesc.
     * 
     * @uml.property name="sortedDesc"
     */
    public synchronized boolean isSortedDesc() {
        return sortedDesc;
    }

    /**
     * @param sortedDesc The sortedDesc to set.
     * 
     * @uml.property name="sortedDesc"
     */
    public synchronized void setSortedDesc(boolean sortedDesc) {
        this.sortedDesc = sortedDesc;
    }

	protected SDFOutputAttributeBinding(String URI) {
		super(URI);       
	}
	
	protected SDFOutputAttributeBinding() {
	    super("");
	}

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAttributeBindung#merge(de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAttributeBindung, de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAttributeBindung)
     */
    public SDFAttributeBindung createMerge(SDFAttributeBindung leftAttributeBinding, SDFAttributeBindung rightAttributeBinding)
            throws SDFAttributeBindingNotMergeableException {       
        return merge((SDFOutputAttributeBinding) leftAttributeBinding, (SDFOutputAttributeBinding) rightAttributeBinding);
    }
    
    static public SDFAttributeBindung merge(SDFOutputAttributeBinding leftAttributeBinding, SDFOutputAttributeBinding rightAttributeBinding){
        SDFOutputAttributeBinding newAttributeBinding = SDFOutputAttributeBindingFactory.getOutputAttributeBinding(SDFIntensionalDescriptions.None);
        
        // ID-Attribute 
        if (leftAttributeBinding.idAttribute && rightAttributeBinding.idAttribute){
            newAttributeBinding.setIdAttribute(true);
        }
        
        // Sortierung
        if (leftAttributeBinding.sortedAsc && rightAttributeBinding.sortedAsc){
            newAttributeBinding.setSortedAsc(true);
        }

        if (leftAttributeBinding.sortedDesc && leftAttributeBinding.sortedDesc){
            newAttributeBinding.setSortedDesc(true);
        }
        
        return newAttributeBinding;
        
    }

}