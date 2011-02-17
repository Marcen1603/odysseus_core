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
/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 */
public class ProjectAO extends UnaryLogicalOp implements OutputSchemaSettable{
	private static final long serialVersionUID = 5487345119018834806L;

	private SDFAttributeList outputSchema = null;
	
	public ProjectAO() {
		super();
	}

	public ProjectAO(ProjectAO ao){
		super(ao);
		if (ao.outputSchema != null) this.outputSchema = new SDFAttributeList(ao.outputSchema);
	}
	

	public @Override
	ProjectAO clone() {
		return new ProjectAO(this);
	}
	
	public int[] determineRestrictList(){
		return calcRestrictList(this.getInputSchema(), this.getOutputSchema());
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}
	
	public static int[] calcRestrictList(SDFAttributeList in, SDFAttributeList out){
		int[] ret = new int[out.size()];
		int i=0;
		for (SDFAttribute a:out){
			int j = 0;
			int k = i;
			for(SDFAttribute b:in){
				if (b.equals(a)){
					ret[i++] = j;
				}
				++j;
			}
			if (k ==i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
		if(port==0){
			setOutputSchema(outputSchema);
		}else{
			throw new IllegalArgumentException("no such port: " + port);
		}
		
	}

}
