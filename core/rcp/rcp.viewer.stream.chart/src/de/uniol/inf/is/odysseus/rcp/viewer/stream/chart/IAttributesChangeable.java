package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface IAttributesChangeable extends IChartPropertiesChanged {

	public SDFAttributeList getSchema();
	public void setVisibleAttributes(boolean[] selectedAttributes);	
	public boolean[] getVisibleAttributes(); 
	/**
	 * Should return null if everything is ok or a string containing the error message
	 * @param selectAttributes
	 * @return
	 */
	public String isValidSelection(boolean[] selectAttributes);
}
