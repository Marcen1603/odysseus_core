package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

public abstract class AbstractPartionedWindowAO extends AbstractWindowAO {

	private static final long serialVersionUID = -2661905171333286810L;

	public AbstractPartionedWindowAO(WindowType windowType) {
		super(windowType);
	}

	public AbstractPartionedWindowAO(AbstractWindowAO windowAO) {
		super(windowAO);
	}

	public AbstractPartionedWindowAO() {
	}
	
	@Override
	@Parameter(name = "PARTITION", type = ResolvedSDFAttributeParameter.class, optional = true, isList = true , doc="Group elements by list of attributes.")
	public void setPartitionBy(List<SDFAttribute> partitionedBy) {
		super.setPartitionBy(partitionedBy);
	}
	
	@Override
	@Parameter(name = "drainAtDone", type = BooleanParameter.class, optional = true, doc = "Drain all elements in buffers when stream ends. Default is false")
	public void setDrainAtDone(boolean drainAtDone) {
		super.setDrainAtDone(drainAtDone);
	}

}
