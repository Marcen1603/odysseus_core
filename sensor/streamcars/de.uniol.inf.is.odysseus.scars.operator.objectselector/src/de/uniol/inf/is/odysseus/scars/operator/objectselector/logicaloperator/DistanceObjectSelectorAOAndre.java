package de.uniol.inf.is.odysseus.scars.operator.objectselector.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.scars.base.SDFObjectRelationalExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@SuppressWarnings("rawtypes")
public class DistanceObjectSelectorAOAndre extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private IAttributeResolver attrRes;
	private String trackedObjectList;
	private HashMap<IPredicate, SDFObjectRelationalExpression> solutions;

	public DistanceObjectSelectorAOAndre(IAttributeResolver attrRes) {
		this.attrRes = attrRes;
	}

	public DistanceObjectSelectorAOAndre(DistanceObjectSelectorAOAndre distanceObjectSelectorAO) {
		super(distanceObjectSelectorAO);
		this.trackedObjectList = distanceObjectSelectorAO.trackedObjectList;
		this.attrRes = distanceObjectSelectorAO.attrRes;
		this.solutions = distanceObjectSelectorAO.solutions;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public DistanceObjectSelectorAOAndre clone() {
		return new DistanceObjectSelectorAOAndre(this);
	}

	public String getTrackedObjectList() {
		return trackedObjectList;
	}

	public void setTrackedObjectList(String trackedObjectList) {
		this.trackedObjectList = trackedObjectList;
	}
	
	public void setSolutions(HashMap<IPredicate, SDFObjectRelationalExpression> sols){
		this.solutions = sols;
	}
	
	public HashMap<IPredicate, SDFObjectRelationalExpression> getSolutions(){
		return this.solutions;
	}

}
