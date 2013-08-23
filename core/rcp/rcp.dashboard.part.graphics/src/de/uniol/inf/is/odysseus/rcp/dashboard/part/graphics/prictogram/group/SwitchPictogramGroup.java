package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.group;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.IPictogram;

public class SwitchPictogramGroup extends AbstractPictogramGroup<Boolean> {
	
	private static final int ON = 0;
	private static final int OFF = 1;
	
	@Override
	public void process(Boolean value) {
		if (value.booleanValue()) {
			getPictogram(ON).activate();
			getPictogram(OFF).deactivate();
		} else {
			getPictogram(OFF).activate();
			getPictogram(ON).deactivate();
		}
	}

	@Override
	public Set<SDFDatatype> getAllowedDatatypes() {
		Set<SDFDatatype> set = new HashSet<>();
		set.add(SDFDatatype.BOOLEAN);
		return set;
	}

	@Override
	public String getName() {
		return "Switch Pictograms";
	}

	@Override
	public void addPictogram(IPictogram pg) {
		if (getPictograms().isEmpty()) {
			super.addPictogram(pg);
		} else {
			if (getPictograms().size()==1) {
				super.addPictogram(pg);
			} else {
				throw new IllegalArgumentException("Only 2 pictograms allows");
			}
		}

	}

	@Override
	public int getMinimumPictograms() {
		return 2;
	}

	@Override
	public int getMaximumPictograms() {
		return 2;
	}
}
