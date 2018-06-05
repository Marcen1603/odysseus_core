/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

public class PieChart3D extends PieChart {

	@Override
	protected JFreeChart createChart() {

		JFreeChart chart = ChartFactory.createPieChart3D(getTitle(), dataset, true, true, false);
		return chart;
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX + ".piechart3d";
	}
}
