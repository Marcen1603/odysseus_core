package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "PREDICATEWINDOW", category = { LogicalOperatorCategory.BASE }, doc = "This is a predicated based window, set start and end condition with predicates.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/PredicateWindow", hidden = true)
public class PredicateWindowAO extends AbstractWindowAO implements IStatefulAO{

	private static final long serialVersionUID = 8834015972527486443L;
	
	// With this option, a predicate window works like a session window.
	// A session ends when a heartbeat is received. Than, all stored elements will be transferred. 
	private boolean closeWindowWithHeartbeat = false;

	public PredicateWindowAO(AbstractWindowAO windowPO) {
		super(windowPO);
	}
	
	public PredicateWindowAO(PredicateWindowAO windowAO) {
		super(windowAO);
		closeWindowWithHeartbeat = windowAO.closeWindowWithHeartbeat;
	}

	public PredicateWindowAO() {
		super(WindowType.PREDICATE);
	}

	@Override
	@Parameter(type = PredicateParameter.class, name = "START", optional = true)
	public void setStartCondition(IPredicate<?> startCondition) {
		super.setStartCondition(startCondition);
	}

	@Override
	@Parameter(type = PredicateParameter.class, name = "END", optional = true)
	public void setEndCondition(IPredicate<?> endCondition) {
		super.setEndCondition(endCondition);
	}

	@Override
	@Parameter(name = "PARTITION", type = ResolvedSDFAttributeParameter.class, optional = true, isList = true)
	public void setPartitionBy(List<SDFAttribute> partitionedBy) {
		super.setPartitionBy(partitionedBy);
	}

	@Override
	@Parameter(name = "KEEPENDINGELEMENT", type = BooleanParameter.class, optional = true)
	public void setKeepEndingElement(boolean keepEndElement) {
		super.setKeepEndingElement(keepEndElement);
	}
	
	@Override
	@Parameter(name = "useElementOnlyForStartOrEnd", type = BooleanParameter.class, optional = true)
	public void setUseElementOnlyForStartOrEnd(boolean useElementOnlyForStartOrEnd) {
		super.setUseElementOnlyForStartOrEnd(useElementOnlyForStartOrEnd);
	}

	@Override
	@Parameter(type = BooleanParameter.class, name = "SameStartTime", optional = true)
	public void setSameStarttime(boolean sameStarttime) {
		super.setSameStarttime(sameStarttime);
	}

	@Parameter(type = BooleanParameter.class, name = "CloseWindowWithHeartbeat", optional = true)
	public void setCloseWindowWithHeartbeat(boolean closeWindowWithHeartbeat) {
		this.closeWindowWithHeartbeat = closeWindowWithHeartbeat;
	}
	
	@Parameter(type = TimeParameter.class, name = "maxWindowTime", optional = true)
	public void setMaxWindowTime(TimeValueItem size) {
		super.setWindowSizeString(size);
	}
	
	public boolean getCloseWindowWithHeartbeat() {
		return closeWindowWithHeartbeat;
	}

	@Override
	public PredicateWindowAO clone() {
		return new PredicateWindowAO(this);
	}


}
