package de.uniol.inf.is.odysseus.wrapper.urg;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.wrapper.urg.sdf.schema.SDFUrgDatatype;

public class UrgDatatypeProvider implements IDatatypeProvider {

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFUrgDatatype.URG_SCANN);
		return ret;
	}
}
