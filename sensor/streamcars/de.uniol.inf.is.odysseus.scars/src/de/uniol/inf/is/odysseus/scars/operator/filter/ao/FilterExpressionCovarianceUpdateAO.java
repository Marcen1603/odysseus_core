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

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

public class FilterExpressionCovarianceUpdateAO <M extends IProbabilityGainConnectionContainer> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private String expressionString;
	private String scannedListPath;
	private String predictedListPath;
	
	public FilterExpressionCovarianceUpdateAO() {
		super();

	}
	
	public FilterExpressionCovarianceUpdateAO(FilterExpressionCovarianceUpdateAO<M> copy) {
		super(copy);
		this.setExpressionString(copy.getExpressionString());
		this.setScannedListPath(copy.getScannedListPath());
		this.setPredictedListPath(copy.getPredictedListPath());
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FilterExpressionCovarianceUpdateAO<M>(this);
	}
	
	@Override
	public SDFSchema getOutputSchema() {
		return this.getInputSchema();
	}

	/**
	 * @param expressionString the expressionString to set
	 */
	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

	/**
	 * @return the expressionString
	 */
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

