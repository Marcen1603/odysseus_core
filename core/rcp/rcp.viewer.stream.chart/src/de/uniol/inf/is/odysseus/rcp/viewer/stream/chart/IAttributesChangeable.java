package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.util.List;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public interface IAttributesChangeable<T> {

	public void chartSettingsChanged();	
	/**
	 * Should return null if everything is ok or a string containing the error message
	 * @param selectAttributes
	 * @return
	 */
	public String isValidSelection(List<IViewableAttribute> selectAttributes);
	public List<IViewableAttribute> getViewableAttributes();
	public List<IViewableAttribute> getChoosenAttributes();
	public void setChoosenAttributes(List<IViewableAttribute> choosenAttributes);	
}
