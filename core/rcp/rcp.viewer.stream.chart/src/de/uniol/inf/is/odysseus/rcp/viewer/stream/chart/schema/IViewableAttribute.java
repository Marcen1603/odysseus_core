package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public interface IViewableAttribute{

	public String getName();
	public SDFDatatype getSDFDatatype();
	public Object evaluate(int index, RelationalTuple<? extends IMetaAttribute> tuple);
}
