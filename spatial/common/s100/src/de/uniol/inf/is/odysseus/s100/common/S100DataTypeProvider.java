package de.uniol.inf.is.odysseus.s100.common;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.s100.common.sdf.schema.SDFS100DataType;

public class S100DataTypeProvider implements IDatatypeProvider 
{
	@Override
	public List<SDFDatatype> getDatatypes() 
	{
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFS100DataType.GM_POINT);
		return ret;
	}
}
