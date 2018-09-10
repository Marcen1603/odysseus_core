package de.uniol.inf.is.odysseus.wrapper.inertiacube;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.sdf.schema.SDFInertiaCubeDatatype;

public class InertiaCubeDatatypeProvider implements IDatatypeProvider{

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> types = new ArrayList<>();
		types.add(SDFInertiaCubeDatatype.YAW_PITCH_ROLL);
		return types;
	}
}
