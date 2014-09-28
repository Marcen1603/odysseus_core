package de.uniol.inf.is.odysseus.imagejcv.common;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

/**
 * @author Kristian Bruns
 */
public class ImageJCVDatatypeProvider implements IDatatypeProvider {
	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFImageJCVDatatype.IMAGEJCV);
		return ret;
	}
}
