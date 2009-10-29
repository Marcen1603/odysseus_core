package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Ist nur eine Hilfsklasse um den obersten Knoten eines Plans eindeutig
 * bestimmen zu koennen.
 * 
 * @author Marco Grawunder
 * 
 */
public class TopAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 6533111765567598018L;
	private ISink<?> physicalInput;

	public TopAO(TopAO po) {
		super(po);
		this.physicalInput = po.physicalInput;
	}

	public TopAO() {
		super();
	}

	public @Override
	TopAO clone() {
		return new TopAO(this);
	}

	public void setPhysicalInputPO(ISink<?> physical) {
		this.physicalInput = physical;
	}

	public IPhysicalOperator getPhysicalInput() {
		if (physicalInput != null) {
			return physicalInput;
		}
		return getPhysSubscriptionsTo().iterator().next().getTarget();
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

}
