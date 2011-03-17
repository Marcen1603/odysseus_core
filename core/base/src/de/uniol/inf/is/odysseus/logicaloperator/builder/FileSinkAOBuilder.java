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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.FileSinkAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class FileSinkAOBuilder extends AbstractOperatorBuilder {

	private final DirectParameter<String> fileName = new DirectParameter<String>(
			"FILE", REQUIREMENT.MANDATORY);
	private final DirectParameter<String> sinktype = new DirectParameter<String>(
			"FILETYPE", REQUIREMENT.OPTIONAL); 
	private final DirectParameter<Long> writeAfterElements = new DirectParameter<Long>("CACHESIZE", REQUIREMENT.OPTIONAL);
	
	public FileSinkAOBuilder() {
		super(0, Integer.MAX_VALUE);
		setParameters(fileName, sinktype, writeAfterElements);
	}

	private static final long serialVersionUID = 167513434840811081L;

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return new FileSinkAO(this.fileName.getValue(), this.sinktype.getValue(),
				this.writeAfterElements.getValue()!=null?this.writeAfterElements.getValue():-1);
	}

}
