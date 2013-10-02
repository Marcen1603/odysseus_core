package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "ElEMENTWINDOW", category={LogicalOperatorCategory.BASE}, doc="This is an element based window.", hidden=true)
public class ElementWindowAO extends AbstractWindowWithWidthAO {

	private static final long serialVersionUID = -1206019698493473257L;

	@Parameter(name = "PARTITION", type = ResolvedSDFAttributeParameter.class, optional = true, isList = true)
	public void setPartitionBy(List<SDFAttribute> partitionedBy) {
		super.setPartitionBy(partitionedBy);
	}

	public ElementWindowAO(ElementWindowAO windowPO) {
		super(windowPO);
	}

	public ElementWindowAO() {
		super(WindowType.TUPLE);
	}
	
	@Override
	public ElementWindowAO clone() {
		return new ElementWindowAO(this);
	}


}
