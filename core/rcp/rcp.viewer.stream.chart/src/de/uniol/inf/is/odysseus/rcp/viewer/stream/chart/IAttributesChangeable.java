package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface IAttributesChangeable {

	public SDFAttributeList getSchema();
	public void setVisibleAttributes(boolean[] selectedAttributes);
	public void visibleAttributesChanged();
	public boolean[] getVisibleAttributes(); 
}
