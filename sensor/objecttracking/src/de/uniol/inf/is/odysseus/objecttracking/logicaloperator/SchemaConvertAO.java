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
package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaExtended;


/**
 * Converts SDFSchema to SDFSchemaExtended
 * 
 * @author Andr� Bolles
 */
public class SchemaConvertAO extends RenameAO{

	private static final long serialVersionUID = 4218605858465342011L;
	
	public SchemaConvertAO() {
		super();
	}

	public SchemaConvertAO(AbstractLogicalOperator po) {
		super(po);
		outputSchema = new SDFSchemaExtended(po.getOutputSchema().getAttributes());
	}
	
	public SchemaConvertAO(SchemaConvertAO ao){
		super(ao);
		outputSchema = new SDFSchemaExtended(ao.outputSchema.getAttributes());
	}
	
	@Override
	public void setOutputSchema(SDFSchema outputSchema) {
		this.outputSchema = new SDFSchemaExtended(outputSchema.clone().getAttributes()); // clone() is necessary
	}
	
	@Override
	public SDFSchema getOutputSchema() {
		if(this.outputSchema == null){
			this.outputSchema = new SDFSchemaExtended(this.getSubscribedToSource(0).getSchema().clone().getAttributes());
		}
		return outputSchema;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SchemaConvertAO(this);
	}
	

}
