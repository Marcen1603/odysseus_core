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
package de.uniol.inf.is.odysseus.scars.operator.xmlprofiler.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class XMLProfilerAO<M extends IProbability> extends UnaryLogicalOp {

	String operatorName;
	String fileName;

	private static final long serialVersionUID = 1L;

	public XMLProfilerAO() {
		super();
	}

	public XMLProfilerAO(XMLProfilerAO<M> clone) {
		super(clone);
		this.operatorName = clone.operatorName;
		this.fileName = clone.fileName;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new XMLProfilerAO<M>(this);
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
