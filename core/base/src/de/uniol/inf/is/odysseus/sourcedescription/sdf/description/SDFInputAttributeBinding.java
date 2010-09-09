package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;


import java.util.Collections;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFCompareOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFCompareOperatorList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFConstantList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;

public class SDFInputAttributeBinding extends SDFAttributeBindung {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2488783959099974152L;

	/**
	 * @uml.property  name="necessity"
	 * @uml.associationEnd  
	 */
    private SDFNecessityState necessity = null;

    private SDFCompareOperatorList compareOpSet = new SDFCompareOperatorList();

    /**
	 * @uml.property  name="setSelectionType"
	 * @uml.associationEnd  
	 */
    private SDFConstantSetSelectionType setSelectionType = null;

    /**
	 * @uml.property  name="constantSet"
	 * @uml.associationEnd  
	 */
    private SDFConstantList constantSet = null;

    /**
	 * @uml.property  name="requiredAttributes"
	 * @uml.associationEnd  
	 */
    private SDFAttributeList requiredAttributes = null;


	public SDFInputAttributeBinding(String URI) {
		super(URI);
	}
	
	protected SDFInputAttributeBinding(){
	    super("");
	}

    /**
     * 
     * @uml.property name="necessity"
     */
    public void setNecessity(SDFNecessityState necessity) {
        this.necessity = necessity;
    }

    /**
     * 
     * @uml.property name="necessity"
     */
    public SDFNecessityState getNecessity() {
        return necessity;
    }

    /**
     * 
     * @uml.property name="compareOp"
     */
    public void addCompareOp(SDFCompareOperator compareOp) {
        compareOpSet.add(compareOp);
    }

    public void setCompareOps(SDFCompareOperatorList ops){
    	compareOpSet.clear();
    	compareOpSet.addAll(ops);
    }
    
    /**
     * 
     * @uml.property name="compareOp"
     */
    public SDFCompareOperatorList getCompareOps() {
        return compareOpSet;
    }

    /**
     * 
     * @uml.property name="setSelectionType"
     */
    public void setSetSelectionType(SDFConstantSetSelectionType setSelectionType) {
        this.setSelectionType = setSelectionType;
    }

    /**
     * 
     * @uml.property name="setSelectionType"
     */
    public SDFConstantSetSelectionType getSetSelectionType() {
        return setSelectionType;
    }

    /**
     * 
     * @uml.property name="constantSet"
     */
    public void setConstantSet(SDFConstantList constantSet) {
        this.constantSet = constantSet;
    }

    /**
     * 
     * @uml.property name="constantSet"
     */
    public SDFConstantList getConstantSet() {
        return constantSet;
    }

    /**
     * 
     * @uml.property name="requiredAttributes"
     */
    public void setRequiredAttributes(SDFAttributeList requiredAttributes) {
        this.requiredAttributes = requiredAttributes;
    }

    /**
     * 
     * @uml.property name="requiredAttributes"
     */
    public SDFAttributeList getRequiredAttributes() {
        return requiredAttributes;
    }

	// Auf eine AttributeBinding ein neues anwenden
	// Wenn noch nicht belegt, wird der Wert gesetzt,
	// wenn belegt, wird maximal verschaerft
	public void applyAttributeBinding(SDFNecessityState necessity) {
		if (this.necessity == null) {
			this.setNecessity(necessity);
		} else {
			// Nur veraendern, wenn optional.
			// Wenn notwendig muss es so bleiben
			if (this.necessity.isOptional()) {
				this.setNecessity(necessity);
			}
		}
	}

	// Das geht
	public void applyAttributeBinding(SDFCompareOperator compareOp)
			throws AttributeBindingUnapplyableException {
		throw new RuntimeException("Not implemented");
	}

	public void applyAttributeBinding(SDFConstantSetSelectionType setSelectionType) {
		throw new RuntimeException("Not implemented");
	}

	public void applyAttributeBinding(SDFConstantList constantSet) {
		throw new RuntimeException("Not implemented");
	}

	public void applyAttributeBinding(SDFAttributeList dependsOn) {
		throw new RuntimeException("Not implemented");
	}

//	public void applyAttributeBinding(SDFInputAttributeBinding bindingToApply)
//			throws AttributeBindingUnapplyableException {
//		// Geht nur wenn die Operatorenlisten uebereinstimmen oder beide null sind
//		if (!this.compareOpList.containsAll(bindingToApply.compareOpList) ||
//				!bindingToApply.compareOpList.containsAll(compareOpList)){
//			throw new AttributeBindingUnapplyableException(
//					"AttributeBinding not Applyable. Operators do not fit");
//		}
//		if (bindingToApply.getNecessity() != null)
//			this.applyAttributeBinding(bindingToApply.getNecessity());
//	}

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAttributeBindung#merge(de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAttributeBindung, de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAttributeBindung)
     */
    @Override
	public SDFAttributeBindung createMerge(SDFAttributeBindung leftAttributeBinding, SDFAttributeBindung rightAttributeBinding) 
    	throws SDFAttributeBindingNotMergeableException{
        return merge((SDFInputAttributeBinding)leftAttributeBinding ,(SDFInputAttributeBinding)rightAttributeBinding);
    }
    
    public SDFAttributeBindung merge(SDFInputAttributeBinding leftAttributeBinding, SDFInputAttributeBinding rightAttributeBinding)
    	throws SDFAttributeBindingNotMergeableException{
        SDFInputAttributeBinding newAttributeBinding = new SDFInputAttributeBinding();
         
        // Notwendigkeit
        // Nur wenn beide Optional sind, ist auch das neue Elemente optional
        if (leftAttributeBinding.getNecessity().isOptional() && rightAttributeBinding.getNecessity().isOptional()){
            newAttributeBinding.setNecessity(new SDFNecessityState(SDFIntensionalDescriptions.Optional));
        }else{
            newAttributeBinding.setNecessity(new SDFNecessityState(SDFIntensionalDescriptions.Required));
        }
        
        // Vergleichsoperator
        if (Collections.disjoint(leftAttributeBinding.getCompareOps(), rightAttributeBinding.getCompareOps())){          
        	// Schnittmenge bilden
        	SDFCompareOperatorList join = new SDFCompareOperatorList(leftAttributeBinding.getCompareOps());
        	join.retainAll(rightAttributeBinding.getCompareOps());
        	newAttributeBinding.setCompareOps(join);
        }else{
            throw new SDFAttributeBindingNotMergeableException("Compareoperators are not mergable");
        }
        
        // Konstantenselektionsbedingung
        if (leftAttributeBinding.getSetSelectionType().equals(rightAttributeBinding.getSetSelectionType())){
            newAttributeBinding.setSetSelectionType(leftAttributeBinding.getSetSelectionType());
        }else{
            throw new SDFAttributeBindingNotMergeableException("Selectiontypes are not equal");
        }
        
        //Konstantenschnitt bilden (es muss fuer beide Attribute gelten!)
        newAttributeBinding.setConstantSet(SDFConstantList.intersection(leftAttributeBinding.getConstantSet(), rightAttributeBinding.getConstantSet()));
       
        // Abhaengige Attribute, hier die Vereinigung bilden
        newAttributeBinding.setRequiredAttributes(SDFAttributeList.union(leftAttributeBinding.getRequiredAttributes(), rightAttributeBinding.getRequiredAttributes())); 
        
        return newAttributeBinding;

    }

	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		if (this.necessity.isOptional()) {
			ret.append("o");
		} else {
			ret.append("r");
		}
		if (this.compareOpSet != null) {
			ret.append("[" + this.compareOpSet.toString() + "]");
		}
		if (this.getSetSelectionType() != null) {
			ret.append(this.getSetSelectionType().toString());
		}
		if (this.getConstantSet() != null) {
			ret.append(this.getConstantSet().toString());
		}
		if (this.getRequiredAttributes() != null) {
			ret.append(this.getRequiredAttributes().toString());
		}
		return ret.toString();
	}
}