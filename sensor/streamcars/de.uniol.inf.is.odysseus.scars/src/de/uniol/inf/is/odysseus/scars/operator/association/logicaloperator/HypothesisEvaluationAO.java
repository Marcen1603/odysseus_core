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
package de.uniol.inf.is.odysseus.scars.operator.association.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Logical Operator for the rating of connections within the association process.
 *
 * new = left; old = right
 *
 * @author Volker Janz
 *
 * @param <M>
 */
public class HypothesisEvaluationAO<M extends IProbability> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private String oldObjListPath;
	private String newObjListPath;
	private HashMap<String, String> algorithmParameter;
	private HashMap<String, String> measurementPairs;

	private String functionID;

	public HypothesisEvaluationAO() {
		super();
	}

	@SuppressWarnings("unchecked")
	public HypothesisEvaluationAO(HypothesisEvaluationAO<M> copy) {
		super(copy);

		this.oldObjListPath = copy.getOldObjListPathC();
		this.newObjListPath = copy.getNewObjListPathC();
		this.algorithmParameter = (HashMap<String, String>) copy.getAlgorithmParameter().clone();
		this.measurementPairs = (HashMap<String, String>) copy.getMeasurementPairs().clone();
		this.functionID = copy.getFunctionID();
	}

	@Override
	public HypothesisEvaluationAO<M> clone(){
		return new HypothesisEvaluationAO<M>(this);
	}

	public String getOldObjListPathC() {
		return this.oldObjListPath;
	}

	public String getNewObjListPathC() {
		return this.newObjListPath;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	public void initPaths(String oldObjListPath, String newObjListPath) {
		this.oldObjListPath = oldObjListPath;
		this.newObjListPath = newObjListPath;
	}

	public String getNewObjListPath() {
		return this.newObjListPath;
	}

	public String getOldObjListPath() {
		return this.oldObjListPath;
	}

	public void setAlgorithmParameter(HashMap<String, String> newAlgoParameter) {
		this.algorithmParameter = newAlgoParameter;
	}

	public HashMap<String, String> getAlgorithmParameter() {
		return this.algorithmParameter;
	}

	public void setMeasurementPairs(HashMap<String, String> newMeasPairs) {
		this.measurementPairs = newMeasPairs;
	}

	public HashMap<String, String> getMeasurementPairs() {
		return this.measurementPairs;
	}

	public String getFunctionID() {
		return this.functionID;
	}

	public void setFunctionID(String funcID) {
		this.functionID = funcID;
	}

}
