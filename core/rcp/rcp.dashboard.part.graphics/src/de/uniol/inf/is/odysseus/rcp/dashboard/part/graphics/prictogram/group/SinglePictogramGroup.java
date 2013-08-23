package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.group;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class SinglePictogramGroup extends AbstractPictogramGroup<Object> {

	@Override
	public String getName() {
		return "Single Pictogram";
	}

	@Override
	public void process(Object value) {
		if (getPictogram(0).isActivated()) {
			getPictogram(0).deactivate();
		} else {
			getPictogram(0).activate();
		}

	}

	@Override
	public Set<SDFDatatype> getAllowedDatatypes() {
		Set<SDFDatatype> dts = new HashSet<>();
		dts.add(SDFDatatype.INTEGER);
		dts.add(SDFDatatype.DOUBLE);
		dts.add(SDFDatatype.FLOAT);
		return dts;
	}

	@Override
	public int getMinimumPictograms() {
		return 1;
	}

	@Override
	public int getMaximumPictograms() {
		return 1;
	}

}
