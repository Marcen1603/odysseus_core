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
package de.uniol.inf.is.odysseus.scars.operator.evaluation.ao;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class EvaluationAO<M extends IProbability> extends AbstractLogicalOperator{

	private static final long serialVersionUID = 7650711998042333164L;

	private String associationObjListPath;
	private String filteringObjListPath;
	private String brokerObjListPath;

	private double threshold;

	public EvaluationAO() {
		super();
	}

	public EvaluationAO(EvaluationAO<M> copy) {
		super(copy);
		this.associationObjListPath = copy.associationObjListPath;
		this.filteringObjListPath = copy.filteringObjListPath;
		this.brokerObjListPath = copy.brokerObjListPath;
		this.threshold = copy.getThreshold();
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		return getInputSchema(2);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new EvaluationAO<M>(this);
	}

	public void initPaths(String associationObjListPath, String filteringObjListPaths, String brokerObjListPath) {
		this.associationObjListPath = associationObjListPath;
		this.filteringObjListPath = filteringObjListPaths;
		this.brokerObjListPath = brokerObjListPath;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getAssociationObjListPath() {
		return associationObjListPath;
	}

	public String getFilteringObjListPath() {
		return filteringObjListPath;
	}

	public String getBrokerObjListPath() {
		return brokerObjListPath;
	}

}
