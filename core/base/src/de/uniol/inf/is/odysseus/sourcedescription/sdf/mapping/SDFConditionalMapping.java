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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDataschema;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

public class SDFConditionalMapping extends SDFSchemaMapping {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5188343859024323802L;

	/**
	 * @uml.property  name="inEntity"
	 * @uml.associationEnd  
	 */
    SDFEntity inEntity = null;

    /**
	 * @uml.property  name="outEntity"
	 * @uml.associationEnd  
	 */
    SDFEntity outEntity = null;

    /**
	 * @uml.property  name="mappingPredicate"
	 * @uml.associationEnd  
	 */
    SDFPredicate mappingPredicate = null;


	protected SDFConditionalMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI, localSchema, globalSchema);
	}

    /**
     * @param inEntity
     * 
     * @uml.property name="inEntity"
     */
    public void setInEntity(SDFEntity inEntity) {
        this.inEntity = inEntity;
    }

    /**
     * 
     * @uml.property name="inEntity"
     */
    public SDFEntity getInEntity() {
        return inEntity;
    }

    /**
     * 
     * @uml.property name="outEntity"
     */
    public void setOutEntity(SDFEntity outEntity) {
        this.outEntity = outEntity;
    }

    /**
     * 
     * @uml.property name="outEntity"
     */
    public SDFEntity getOutEntity() {
        return outEntity;
    }

    /**
     * 
     * @uml.property name="mappingPredicate"
     */
    public void setMappingPredicate(SDFPredicate mappingPredicate) {
        this.mappingPredicate = mappingPredicate;
    }

    /**
     * 
     * @uml.property name="mappingPredicate"
     */
    public SDFPredicate getMappingPredicate() {
        return mappingPredicate;
    }

}