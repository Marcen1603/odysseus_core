/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;

public class LineChartDashboardPart extends AbstractChartDashboardPart {

	private static final String TITLE_SETTING = "Title";
	private static final String X_AXIS_LABEL_SETTING = "X-Axis label";
	private static final String Y_AXIS_LABEL_SETTING = "Y-Axis label";
	private static final String X_AXIS_SETTING = "X-Axis";
	private static final String Y_AXIS_SETTING = "Y-Axis";
	private static final String ATTRIBUTE_SEPARATOR = ",";
	private static final String MAX_DATA_COUNT = "Value count";

	private String[] xAttributes;
	private String[] yAttributes;
	private final Map<String, Integer> accessDescriptors = Maps.newHashMap();

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {
	}

	@Override
	protected Dataset createDataset() {
		return new DefaultCategoryDataset();
	}

	@Override
	protected JFreeChart createChart() {
		return ChartFactory.createLineChart(getSettingValue(TITLE_SETTING, "Line Chart"), getSettingValue(X_AXIS_LABEL_SETTING, "X-Axis"), getSettingValue(Y_AXIS_LABEL_SETTING, "Y-Axis"),
				(DefaultCategoryDataset) getDataset(), PlotOrientation.VERTICAL, true, true, false);
	}

	@Override
	protected void decorateChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
	}

	@Override
	protected void startChart(List<IPhysicalOperator> physicalRoots) throws Exception {
		xAttributes = checkAndSplit((String) getSettingValue(X_AXIS_SETTING, null));
		yAttributes = checkAndSplit((String) getSettingValue(Y_AXIS_SETTING, null));

		checkAttributeLists();

		accessDescriptors.putAll(determineAttributeAccess(xAttributes, physicalRoots));
		accessDescriptors.putAll(determineAttributeAccess(yAttributes, physicalRoots));

		((DefaultCategoryDataset) getDataset()).clear();
	}

	@Override
	protected void addStreamElementToChart(Tuple<?> element, int port) {
		final Object[] xAxisValues = determineValues((Tuple<?>) element, xAttributes);
		final Object[] yAxisValues = determineValues((Tuple<?>) element, yAttributes);

		for (int i = 0; i < xAxisValues.length; i++) {
			if (xAxisValues[i] != null && yAxisValues[i] != null) {
				addDataValue((Number) yAxisValues[i], yAttributes[i], (Comparable<?>) xAxisValues[i]);
			}
		}
	}

	@Override
	protected void addPunctuationToChart(PointInTime punctuation, int port) {

	}

	private void addDataValue(final Number value, final Comparable<?> xAxis, final Comparable<?> yAxis) {
		DefaultCategoryDataset dataset = (DefaultCategoryDataset) getDataset();
		dataset.addValue(value, xAxis, yAxis);
		if (dataset.getColumnCount() > getSettingValue(MAX_DATA_COUNT, 50)) {
			dataset.removeColumn(0);
		}
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
