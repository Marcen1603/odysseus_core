/** Copyright [2011] [The Odysseus Team]
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
package measure.windperformancercp.views.performance;

import measure.windperformancercp.controller.Connector;
import measure.windperformancercp.views.result.ActiveQueriesPresenter;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.ScatterPlotChart;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

//public class FunctionPlotterView extends ViewPart {
public class FunctionPlotterView extends ScatterPlotChart {
	public static final String ID = "measure.windperformancercp.functionPlotterView";
	private ActiveQueriesPresenter presenter;
	
	public void connectToQuery(String queryId){
		IQuery actQuery = Connector.getInstance().getQueries().get(queryId);
	//	this.createConnection(Connector.getInstance().getQueries().get(queryId).getRoots().get(0));
		init(actQuery.getRoots().get(0));
		for(SDFAttribute att : actQuery.getRoots().get(0).getOutputSchema()){
			if(att.getAttributeName().equals("final_WSpeed")){
				setXValue("final_WSpeed");
			}
			if(att.getAttributeName().equals("final_Power")){
				setYValue("final_Power");
			}
			if(att.getAttributeName().equals("FPVbin_id")){
				setXValue("FPVbin_id");
			}
			if(att.getAttributeName().equals("FPPbin_id")){
				setYValue("FPPbin_id");
			}
		}
			
	}
	
	
	@Override
	public void createPartControl(Composite parent) {
		presenter = ActiveQueriesPresenter.getInstance(this);
		super.createPartControl(parent);
	}

}
