package de.uniol.inf.is.odysseus.condition.rest.datatypes;

public class AreaChartVisualizationInformation extends VisualizationInformation {

	private int maxElements;
	private String timeAttribute;
	private boolean showSymbols;

	public int getMaxElements() {
		return maxElements;
	}

	/**
	 * The maximum number of elements which is shown in the graph. Only used if
	 * > 0
	 * 
	 * @param maxElements
	 */
	public void setMaxElements(int maxElements) {
		this.maxElements = maxElements;
	}

	public String getTimeAttribute() {
		return timeAttribute;
	}

	public void setTimeAttribute(String timeAttribute) {
		this.timeAttribute = timeAttribute;
	}

	public boolean isShowSymbols() {
		return showSymbols;
	}

	/**
	 * If set to true, a small circle is plotted at every data point in the area
	 * chart. If the density of data points is very high, this can hide the
	 * actual data line. Therefore you can switch it off and on here. Default is
	 * off.
	 * 
	 * @param showSymbols
	 */
	public void setShowSymbols(boolean showSymbols) {
		this.showSymbols = showSymbols;
	}

}
