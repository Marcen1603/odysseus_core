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
package de.uniol.inf.is.odysseus.scars.operator.association.ao;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Volker Janz
 *
 * @param <M>
 */
public class HypothesisExpressionEvaluationAO<M extends IProbability> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private String predObjListPath;
	private String scanObjListPath;
	private String expressionString;

	public HypothesisExpressionEvaluationAO() {
		super();
	}

	public HypothesisExpressionEvaluationAO(HypothesisExpressionEvaluationAO<M> copy) {
		super(copy);
		this.predObjListPath = copy.predObjListPath;
		this.scanObjListPath = copy.scanObjListPath;
		this.expressionString = copy.expressionString;
	}

	@Override
	public HypothesisExpressionEvaluationAO<M> clone(){
		return new HypothesisExpressionEvaluationAO<M>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	public void initPaths(String predObjListPath, String scanObjListPath) {
		this.predObjListPath = predObjListPath;
		this.scanObjListPath = scanObjListPath;
	}

	public String getPredObjListPath() {
		return predObjListPath;
	}

	public void setPredObjListPath(String predObjListPath) {
		this.predObjListPath = predObjListPath;
	}

	public String getScanObjListPath() {
		return scanObjListPath;
	}

	public void setScanObjListPath(String scanObjListPath) {
		this.scanObjListPath = scanObjListPath;
	}

	public String getExpressionString() {
		return expressionString;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
