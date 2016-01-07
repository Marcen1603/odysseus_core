package de.uniol.inf.is.odysseus.debsgc2016.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="GCQuery1", minInputPorts=2, maxInputPorts=2,category={LogicalOperatorCategory.APPLICATION}, doc="test for gc 2016")
public class GCQuery1AO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -7308670306275116203L;

	public GCQuery1AO(AbstractLogicalOperator op) {
		super(op);
	}

	public GCQuery1AO() {
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new GCQuery1AO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema r = getInputSchema(0);
		SDFSchema n = getInputSchema(1);
		SDFDatatype newType = SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST, SDFDatatype.TUPLE, n);
		SDFAttribute list = new SDFAttribute(n.getURI(), "List", newType);
		SDFSchema n_neu = SDFSchemaFactory.createNewAddAttribute(list, r);
		return n_neu;
	}
	
}
