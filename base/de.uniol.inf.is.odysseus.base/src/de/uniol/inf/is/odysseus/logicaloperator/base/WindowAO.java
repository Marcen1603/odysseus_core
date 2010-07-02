package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class WindowAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 349442832133715634L;

	private WindowType windowType = null;

	private long windowSize = 1;

	private long windowAdvance = -1;
	
	private SDFAttribute windowOn;

	public SDFAttribute getWindowOn() {
		return windowOn;
	}

	public void setWindowOn(SDFAttribute windowOn) {
		this.windowOn = windowOn;
	}

	private List<SDFAttribute> partitionedBy;

	public long getWindowAdvance() {
		return windowAdvance;
	}

	public long getWindowSlide() {
		return windowAdvance;
	}

	public void setWindowSlide(Long slide) {
		this.windowAdvance = slide;
	}

	public void setWindowSlide(String size){
		this.windowAdvance = Long.parseLong(size);
	}

	
	public void setWindowAdvance(Long windowAdvance) {
		this.windowAdvance = windowAdvance;
	}

	public void setWindowAdvance(String size){
		this.windowAdvance = Long.parseLong(size);
	}

	
	public WindowAO(WindowType windowType) {
		super();
		this.windowType = windowType;
	}

	public WindowAO(WindowAO windowPO) {
		super(windowPO);
		this.windowSize = windowPO.windowSize;
		this.windowAdvance = windowPO.windowAdvance;
		this.partitionedBy = windowPO.partitionedBy;
		this.windowOn = windowPO.windowOn;
		this.windowType = windowPO.windowType;
	}

	public WindowAO() {
		super();
	}

	@Override
	public WindowAO clone() {
		return new WindowAO(this);
	}

	public long getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(Long windowSize) {
		this.windowSize = windowSize;
	}
	
	public void setWindowSize(String size){
		this.windowSize = Long.parseLong(size);
	}

	
	public WindowType getWindowType() {
		return windowType;
	}

	public void setWindowType(WindowType windowType) {
		this.windowType = windowType;
	}

	@Override
	public String getName() {
		return WindowAO.class.getSimpleName();
	}

	public List<SDFAttribute> getPartitionBy() {
		return partitionedBy;
	}

	public boolean isPartitioned() {
		return this.partitionedBy != null;
	}

	public void setPartitionBy(List<SDFAttribute> partitionedBy) {
		this.partitionedBy = partitionedBy;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((partitionedBy == null) ? 0 : partitionedBy.hashCode());
		result = prime * result
				+ (int) (windowAdvance ^ (windowAdvance >>> 32));
		result = prime * result
				+ ((windowOn == null) ? 0 : windowOn.hashCode());
		result = prime * result + (int) (windowSize ^ (windowSize >>> 32));
		result = prime * result
				+ ((windowType == null) ? 0 : windowType.hashCode());
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
		WindowAO other = (WindowAO) obj;
		if (partitionedBy == null) {
			if (other.partitionedBy != null)
				return false;
		} else if (!partitionedBy.equals(other.partitionedBy))
			return false;
		if (windowAdvance != other.windowAdvance)
			return false;
		if (windowOn == null) {
			if (other.windowOn != null)
				return false;
		} else if (!windowOn.equals(other.windowOn))
			return false;
		if (windowSize != other.windowSize)
			return false;
		if (windowType == null) {
			if (other.windowType != null)
				return false;
		} else if (!windowType.equals(other.windowType))
			return false;
		return true;
	}
	

}
