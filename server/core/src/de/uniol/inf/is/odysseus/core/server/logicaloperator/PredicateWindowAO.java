package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "PREDICATEWINDOW", category = {
		LogicalOperatorCategory.BASE }, doc = "This is a predicated based window, set start and end condition with predicates.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/PredicateWindow", hidden = true)
public class PredicateWindowAO extends AbstractPartionedWindowAO implements IStatefulAO {

	private static final long serialVersionUID = 8834015972527486443L;

	// With this option, a predicate window works like a session window.
	// A session ends when a heartbeat is received. Than, all stored elements will
	// be transferred.
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
	@Parameter(type = PredicateParameter.class, name = "CLEAR", optional = true)
	public void setClearCondition(IPredicate<?> clearCondition) {
		super.setClearCondition(clearCondition);
	}

	@Override
	@Parameter(type = PredicateParameter.class, name = "ADVANCEWHEN", optional = true)
	public void setAdvanceCondition(IPredicate<?> advanceCondition) {
		super.setAdvanceCondition(advanceCondition);
	}
	
	@Override
	@Parameter(type = IntegerParameter.class, name="AdvanceSize", optional = true, doc="If AdvanceWhen is set and evaluates to true, remove AdvanceSize elements from the beginning of current window. Remove no element for 0. If value if below 0, all elements are removed.")
	public void setAdvanceSize(int advanceSize) {
		super.setAdvanceSize(advanceSize);
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

	@Parameter(type = BooleanParameter.class, name = "nesting", optional = true, doc = "If set to true, the output will contain a single nested element for alle buffer elements. Default is false.")
	public void setNesting(boolean nesting) {
		super.setNesting(nesting);
	}
	
	@Parameter(type = BooleanParameter.class, name = "keepTimeOrder", optional = true, doc = "Set to false to allow out of order processing of results. Default is true")
	@Override
	public void setKeepTimeOrder(boolean keepTimeOrder) {
		super.setKeepTimeOrder(keepTimeOrder);
	}

	@Parameter(type = TimeParameter.class, name = "maxWindowTime", optional = true)
	public void setMaxWindowTime(TimeValueItem size) {
		super.setWindowSizeString(size);
	}
	
	@Parameter(type = BooleanParameter.class, name = "outputIfMaxWindowTime", optional = true, doc = "A window can close by condition or when maxWindowTime is reached. Set to false to avoid writing in case of maxWindowTime (default is true)")
	@Override
	public void setOutputIfMaxWindowTime(boolean outputIfMaxWindowTime) {
		super.setOutputIfMaxWindowTime(outputIfMaxWindowTime);
	}
	
	@Parameter(type = IntegerParameter.class, name="maxWindowTimeOutputPort", optional = true, doc="A special output port can be defined to allow to write in cases where maxWindowTime is reached to this port. Default is 0, i.e. the default output port.")
	@Override
	public void setMaxWindowTimeOutputPort(int maxWindowTimeOutputPort) {
		super.setMaxWindowTimeOutputPort(maxWindowTimeOutputPort);
	}
	
	@Parameter(type = TimeParameter.class, name="CloseWindowAfterNoUpdatesFor", optional = true, doc="A window can be closed if it get no updates for a distinct time.")
	@Override
	public void setCloseWindowAfterNoUpdateTime(TimeValueItem closeWindowAfterNoUpdateTime) {
		super.setCloseWindowAfterNoUpdateTime(closeWindowAfterNoUpdateTime);
	}
	
	@Parameter(type = IntegerParameter.class, name="CloseWindowAfterNoUpdateTimePort", optional = true, doc="Allow to set a different output port for cases, where the window closes in cases of no updates for da distinct time (CloseWindowAfterNoUpdatesFor). Only treated if CloseWindowAfterNoUpdatesFor>0, default is 0.")
	@Override
	public void setCloseWindowAfterNoUpdateTimePort(int closeWindowAfterNoUpdateTimePort) {
		super.setCloseWindowAfterNoUpdateTimePort(closeWindowAfterNoUpdateTimePort);
	}

	public boolean getCloseWindowWithHeartbeat() {
		return closeWindowWithHeartbeat;
	}
	
	

	@Override
	public PredicateWindowAO clone() {
		return new PredicateWindowAO(this);
	}
	
	@Override
	final public SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema outputSchema;
		if (isNesting()) {
			// TODO: This cannot be done generic ... FIXME! --> RelationalPredicateWindowAO
			SDFSchema inputSchema = getInputSchema();
			if (inputSchema.getType().isAssignableFrom(Tuple.class)) {
				SDFAttribute firstAttribute = inputSchema.getAttribute(0);
				SDFDatatype outputType = SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST_TUPLE, inputSchema);
				SDFAttribute out = new SDFAttribute(firstAttribute.getSourceName(), "nested", outputType);
				Collection<SDFAttribute> attributes = new ArrayList<>();
				attributes.add(out);
				outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
			}else {
				throw new IllegalArgumentException("Nesting can currently only be done with tuples!");
			}
		} else {
			outputSchema = super.getOutputSchemaIntern(pos);
		}
		
		if (isKeepTimeOrder()) {			
			return  outputSchema;
		}else {
			return SDFSchemaFactory.createNewWithOutOfOrder(true, outputSchema);
		}
	}

}
