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
package de.uniol.inf.is.odysseus.scars.operator.bouncer.ao;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class TemporaryDataBouncerAO<M extends IProbability> extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	private String objListPath;
	private double threshold;

	private String operator;

	public TemporaryDataBouncerAO() {
		super();
	}

	public TemporaryDataBouncerAO(TemporaryDataBouncerAO<M> copy) {
		super(copy);
		this.objListPath = copy.objListPath;
		this.threshold = copy.threshold;
		this.operator = copy.operator;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TemporaryDataBouncerAO<M>(this);
	}

	public String getObjListPath() {
		return objListPath;
	}

	public void setObjListPath(String objListPath) {
		this.objListPath = objListPath;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
