package cm.configuration;

public class AreaChartVisualizationInformation extends VisualizationInformation {

	private int maxElements;

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

}
