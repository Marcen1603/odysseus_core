package de.uniol.inf.is.odysseus.priority;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@SuppressWarnings("unchecked")
public class PostPriorisationAO<T> extends UnaryLogicalOp {

	private static final long serialVersionUID = 4238316182706318260L;

	private boolean isActive = true;

	private List predicates;
	
	private IPostPriorisationFunctionality postPriorisationFunctionality;
	private PriorityPO physicalRoot;
	
	public PriorityPO getPhysicalRoot() {
		return physicalRoot;
	}

	public void setPhysicalRoot(PriorityPO physicalRoot) {
		this.physicalRoot = physicalRoot;
	}

	public PostPriorisationAO() {
		predicates = new ArrayList();
	}
	
	public PostPriorisationAO(PostPriorisationAO<T> postPriorisationAO) {
		this.isActive = postPriorisationAO.isActive;
		this.predicates = postPriorisationAO.predicates;
		this.postPriorisationFunctionality = postPriorisationAO.postPriorisationFunctionality;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result;
		if(predicates != null) {
			result = prime * result + predicates.size();
		}
		return result;
	}	
	
	@Override	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostPriorisationAO other = (PostPriorisationAO) obj;
		if (predicates != other.predicates)
			return false;
		if (postPriorisationFunctionality != other.postPriorisationFunctionality) {
			return false;
		}
		if(isActive != other.isActive) {
			return false;
		}
		return true;
	}	
	
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
	
	@Override
	public PostPriorisationAO clone() {
		return new PostPriorisationAO (this);
	}	
	
}
