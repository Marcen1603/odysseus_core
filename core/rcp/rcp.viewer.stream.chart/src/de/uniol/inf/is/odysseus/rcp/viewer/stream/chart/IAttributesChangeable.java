package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface IAttributesChangeable {

	public void chartSettingsChanged();
	public SDFAttributeList getSchema();
	public SDFAttributeList getAllowedSchema();
	public SDFAttributeList getInitialSchema();
	public SDFAttributeList getVisibleSchema();
	public void setVisibleSchema(SDFAttributeList schema);
	/**
	 * Should return null if everything is ok or a string containing the error message
	 * @param selectAttributes
	 * @return
	 */
	public String isValidSelection(SDFAttributeList selectAttributes);
}
