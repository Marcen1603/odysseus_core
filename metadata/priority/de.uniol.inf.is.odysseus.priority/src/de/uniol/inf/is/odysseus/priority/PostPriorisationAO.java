package de.uniol.inf.is.odysseus.priority;

import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@SuppressWarnings("unchecked")
public class PostPriorisationAO<T> extends UnaryLogicalOp {

	private static final long serialVersionUID = 4238316182706318260L;

	private boolean isActive = true;

	private List predicates;
	
	private IPostPriorisationFunctionality postPriorisationFunctionality;
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}	
	
	private byte defaultPriority;
	
	public byte getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(byte defaultPriority) {
		this.defaultPriority = defaultPriority;
	}	
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

	public void setPredicates(List predicates) {
		this.predicates = predicates;
	}

	public List getPredicates() {
		return predicates;
	}

	public void setPostPriorisationFunctionality(
			IPostPriorisationFunctionality postPriorisationFunctionality) {
		this.postPriorisationFunctionality = postPriorisationFunctionality;
	}

	public IPostPriorisationFunctionality getPostPriorisationFunctionality() {
		return postPriorisationFunctionality;
	}	
	
}
