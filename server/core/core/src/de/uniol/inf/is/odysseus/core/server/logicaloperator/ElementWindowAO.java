package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "ELEMENTWINDOW", category = { LogicalOperatorCategory.BASE }, doc = "This is an element based window.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/ElementWindow", hidden = true)
public class ElementWindowAO extends AbstractWindowAO implements IStatefulAO{

	private static final long serialVersionUID = -1206019698493473257L;

	@Override
	@Parameter(name = "PARTITION", type = ResolvedSDFAttributeParameter.class, optional = true, isList = true)
	public void setPartitionBy(List<SDFAttribute> partitionedBy) {
		super.setPartitionBy(partitionedBy);
	}

	@Parameter(type = LongParameter.class, name = "SIZE", optional = false)
	public void setWindowSizeE(Long windowSize) {
		TimeValueItem windowSizeToSet = new TimeValueItem(windowSize, getBaseTimeUnit());
		super.setWindowSize(windowSizeToSet);
	}
	
	@Parameter(type = LongParameter.class, name = "SLIDE", optional = true)
	public void setWindowSlideE(Long slide) {
		TimeValueItem windowSizeToSet = new TimeValueItem(slide, getBaseTimeUnit());
		super.setWindowSlide(windowSizeToSet);
	}

	@Parameter(type = LongParameter.class, name = "ADVANCE", optional = true)
	public void setWindowAdvanceE(Long windowAdvance) {
		TimeValueItem windowSizeToSet = new TimeValueItem(windowAdvance, getBaseTimeUnit());
		super.setWindowAdvance(windowSizeToSet);
	}
	
	public Long getWindowSizeE() {
		TimeValueItem item =  super.getWindowSize();
		if (item != null) {
			return item.getTime();
		} else {
			return null;
		}
	}
	
	public Long getWindowAdvanceE() {
		TimeValueItem item =  super.getWindowAdvance();
		if (item != null) {
			return item.getTime();
		} else {
			return null;
		}
	}

	public Long getWindowSlideE() {		
		TimeValueItem item =  super.getWindowSlide();
		if (item != null) {
			return item.getTime();
		} else {
			return null;
		}
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
