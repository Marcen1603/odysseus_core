package de.uniol.inf.is.odysseus.wrapper.kinect;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.wrapper.kinect.sdf.schema.SDFKinectDatatype;

/**
 * @author Juergen Boger <juergen.boger@offis.de>, Marco Grawunder
 */
public class KinectDatatypeProvider implements IDatatypeProvider{

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFKinectDatatype.KINECT_COLOR_MAP);
		ret.add(SDFKinectDatatype.KINECT_DEPTH_MAP);
		ret.add(SDFKinectDatatype.KINECT_POINT_CLOUD);
		ret.add(SDFKinectDatatype.KINECT_SKELETON_MAP);
		ret.add(SDFKinectDatatype.BUFFERED_IMAGE);
		return ret;
	}

}
