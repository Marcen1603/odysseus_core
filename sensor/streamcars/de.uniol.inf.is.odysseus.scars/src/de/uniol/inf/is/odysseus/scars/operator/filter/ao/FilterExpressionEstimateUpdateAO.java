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

import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

public class FilterExpressionEstimateUpdateAO <M extends IProbabilityGainConnectionContainer> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	// path to new and old objects
	private String oldObjListPath;
	private String newObjListPath;
	
	private Map<String, String> expressions;
	
	public FilterExpressionEstimateUpdateAO()
	{
		super();
	}
	
	public FilterExpressionEstimateUpdateAO(FilterExpressionEstimateUpdateAO<M> copy) {
		super(copy);
		this.setOldObjListPath(copy.getOldObjListPath());
		this.setNewObjListPath(copy.getNewObjListPath());
		this.setExpressions(copy.getExpressions());
	}

	
	@Override
	public AbstractLogicalOperator clone() {
		return new FilterExpressionEstimateUpdateAO<M>(this);
	}
		
	
	// Getter & Setter

	public String getOldObjListPath() {
		return oldObjListPath;
	}

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}
	
	public String getNewObjListPath() {
		return newObjListPath;
	}

	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	/**
	 * @param expressionString the expressionString to set
	 */
	public void setExpressions(Map<String, String> functionList) {
		this.expressions = functionList;
	}

	/**
	 * @return the expressionString
	 */
	public Map<String, String> getExpressions() {
		return expressions;
	}

}

