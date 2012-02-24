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
package de.uniol.inf.is.odysseus.scars.operator.filter.ao;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

public class FilterExpressionGainAO <M extends IProbabilityGain> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	private String expressionString;
	private String scannedListPath;
	private String predictedListPath;
	private List<String> restrictedPredVariables;
	private List<String> restrictedScanVariables;

	public FilterExpressionGainAO() {
		super();
	}
	
	public FilterExpressionGainAO(FilterExpressionGainAO<M> copy) {
		super(copy);
		this.setPredictedListPath(copy.getPredictedListPath());
		this.setScannedListPath(copy.getScannedListPath());
		this.setExpressionString(copy.getExpressionString());
		this.setRestrictedPredVariables(new ArrayList<String>(copy.restrictedPredVariables));
		this.setRestrictedScanVariables(new ArrayList<String>(copy.restrictedScanVariables));
	}


	@Override
	public AbstractLogicalOperator clone() {
		return new FilterExpressionGainAO<M>(this);
	}
		
	@Override
	public SDFSchema getOutputSchema() {
		return this.getInputSchema();
	}

	// Getter & Setter
	
	public void setRestrictedPredVariables(List<String> restrictedVariables) {
		this.restrictedPredVariables = restrictedVariables;
	}
	
	public void setRestrictedScanVariables(List<String> restrictedVarialbes) {
		this.restrictedScanVariables = restrictedVarialbes;
	}
	
	public List<String> getRestrictedPredVariables() {
		return restrictedPredVariables;
	} 
	
	public List<String> getRestrictedScanVarialbes() {
		return restrictedScanVariables;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

	public String getExpressionString() {
		return expressionString;
	}

	public String getScannedListPath() {
		return scannedListPath;
	}

	public void setScannedListPath(String scannedListPath) {
		this.scannedListPath = scannedListPath;
	}

	public String getPredictedListPath() {
		return predictedListPath;
	}

	public void setPredictedListPath(String predictedListPath) {
		this.predictedListPath = predictedListPath;
	}
}

