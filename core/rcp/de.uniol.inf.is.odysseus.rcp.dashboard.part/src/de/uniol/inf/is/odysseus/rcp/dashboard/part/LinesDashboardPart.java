package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;

public class LinesDashboardPart extends AbstractDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(LinesDashboardPart.class);

	private static final String TITLE_SETTING = "Title";
	private static final String X_AXIS_LABEL_SETTING = "X-Axis label";
	private static final String Y_AXIS_LABEL_SETTING = "Y-Axis label";
	private static final String X_AXIS_SETTING = "X-Axis";
	private static final String Y_AXIS_SETTING = "Y-Axis";
	private static final String ATTRIBUTE_SEPARATOR = ",";
	private static final String MAX_DATA_COUNT = "Value count";

	private JFreeChart chart;
	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	private String[] xAttributes;
	private String[] yAttributes;
	private final Map<String, Integer> accessDescriptors = Maps.newHashMap();

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		chart = ChartFactory.createLineChart(getSettingValue(TITLE_SETTING, "Line Chart"), getSettingValue(X_AXIS_LABEL_SETTING, "X-Axis"), getSettingValue(Y_AXIS_LABEL_SETTING, "Y-Axis"), dataset,
				PlotOrientation.VERTICAL, true, true, false);

		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);

		ChartComposite chartComposite = new ChartComposite(parent, SWT.NONE, this.chart, true);
		chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);

		xAttributes = checkAndSplit((String) getSettingValue(X_AXIS_SETTING, null));
		yAttributes = checkAndSplit((String) getSettingValue(Y_AXIS_SETTING, null));

		checkAttributeLists();

		accessDescriptors.putAll(determineAttributeAccess(xAttributes, physicalRoots));
		accessDescriptors.putAll(determineAttributeAccess(yAttributes, physicalRoots));
		
		dataset.clear();
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple)) {
			LOG.error("Lines DashboardPart only applyable for Tuples!");
			return;
		}

		final Object[] xAxisValues = determineValues((Tuple<?>) element, xAttributes);
		final Object[] yAxisValues = determineValues((Tuple<?>) element, yAttributes);

		for (int i = 0; i < xAxisValues.length; i++) {
			if (xAxisValues[i] != null && yAxisValues[i] != null) {
				addDataValue((Number) yAxisValues[i], yAttributes[i], (Comparable<?>) xAxisValues[i]);
			}
		}
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {

	}

	private void addDataValue(final Number value, final Comparable<?> xAxis, final Comparable<?> yAxis) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				dataset.addValue(value, xAxis, yAxis);
				if( dataset.getColumnCount() > getSettingValue(MAX_DATA_COUNT, 50)) {
					dataset.removeColumn(0);
				}
			}
		});
	}

	private <T> T getSettingValue(String settingName, T defValue) {
		Configuration config = getConfiguration();
		if (!config.exists(settingName)) {
			return defValue;
		}

		T value = config.get(settingName);
		if (value instanceof String) {
			return !Strings.isNullOrEmpty((String) value) ? value : defValue;
		}
		return value != null ? value : defValue;
	}

	private void checkAttributeLists() throws Exception {
		if (xAttributes.length == 0) {
			throw new Exception("At least one attribute for x-axis must be specified!");
		}

		if (yAttributes.length == 0) {
			throw new Exception("At least one attribute for y-axis must be specified!");
		}

		if (xAttributes.length != yAttributes.length) {
			throw new Exception("Number of attributes for x- and y-axis must be equal!");
		}
	}

	private Object[] determineValues(Tuple<?> element, String[] attributes) {
		Object[] results = new Number[attributes.length];

		for (int i = 0; i < attributes.length; i++) {
			results[i] = element.getAttribute(accessDescriptors.get(attributes[i]));
		}

		return results;
	}

	private static String[] checkAndSplit(String attributeList) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(attributeList), "AttributeList must not be null or empty!");

		String[] attributes = attributeList.split(ATTRIBUTE_SEPARATOR);
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = attributes[i].trim();
		}

		return attributes;
	}

	private static Map<String, Integer> determineAttributeAccess(String[] attributes, List<IPhysicalOperator> operators) throws Exception {
		Map<String, Integer> descriptors = Maps.newHashMap();

		for (String attribute : attributes) {
			boolean found = false;
			for (IPhysicalOperator operator : operators) {
				SDFSchema schema = operator.getOutputSchema();

				List<SDFAttribute> sdfAttrs = schema.getAttributes();
				for (int i = 0; i < sdfAttrs.size(); i++) {
					if (attribute.equals(sdfAttrs.get(i).getAttributeName())) {
						descriptors.put(attribute, i);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}

			if (!found) {
				throw new Exception("Could not find attribute " + attribute + "!");
			}
		}

		return descriptors;
	}
}
