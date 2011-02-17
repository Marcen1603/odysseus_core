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
package de.uniol.inf.is.odysseus.logicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * @author Jendrik Poloczek
 */
public class NestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeListExtended outputSchema;
	// passed parameters in a query, so extended list
	// not necessary
	private SDFAttributeList nestingAttributes;
	private SDFAttribute nestAttribute;
	private String nestAttributeName;

	public NestAO() {	    
		super();
	}
	
	public NestAO(NestAO ao) {
		super(ao);
		this.outputSchema = ao.getOutputSchema().clone();
		this.nestingAttributes = ao.getNestingAttributes().clone();
		this.nestAttribute = ao.getNestAttribute().clone();
	}
	
	@Override
	public NestAO clone() {
		return new NestAO(this);
	}
	
	@Override
	public SDFAttributeListExtended getOutputSchema() {
		if(this.outputSchema == null) {
		    SDFAttributeList inputSchema = this.getInputSchema();
			this.outputSchema = calcOutputSchema(inputSchema);
		}
		return outputSchema;
	}

	/**
	 * creating a new SDFAttribute for new nesting attribute 
	 * 
	 * @param nestingAttributeName name of nesting attribute
	 */	
	public void setNestAttributeName(String nestAttributeName) {
		this.nestAttributeName = nestAttributeName;
	}
	
	/**
	 * modify input schema, removing attributes to nest, and 
	 * adding new nesting attribute.
	 */	
	private SDFAttributeListExtended calcOutputSchema(SDFAttributeList inputSchema) {
	    // TODO: Falls inputSchema eine Extended ist, dann müssen noch
		// die Metadaten, also die PredictionFunctions ins Ausgabeschema
		// übernommen werden.
		
		SDFAttributeListExtended outputSchema;
	    SDFAttributeListExtended groupingAttributes;
	    SDFAttribute nestAttribute;
	    
	    groupingAttributes = new SDFAttributeListExtended();

	    for(SDFAttribute attribute : inputSchema) {
	        if(!this.nestingAttributes.contains(attribute)) {
	            groupingAttributes.add(attribute.clone());
	        }
	    }
	    
	    nestAttribute = new SDFAttribute(this.nestAttributeName);	    
	    nestAttribute.setDatatype(SDFDatatypeFactory.getDatatype("Set"));
	    nestAttribute.setSubattributes(this.nestingAttributes);
	    
	    outputSchema = new SDFAttributeListExtended();
	   
	    for(SDFAttribute groupingAttribute : groupingAttributes) {
	        outputSchema.add(groupingAttribute);
	    }
	    
	    outputSchema.add(nestAttribute);	    
	    return outputSchema;
	}
	
	public SDFAttributeList getNestingAttributes() {
		return nestingAttributes;
	}
	
	public SDFAttribute getNestAttribute() {
		return nestAttribute;
	}
	
	public void setNestingAttributes(SDFAttributeList nestingAttributes) {
		this.nestingAttributes = nestingAttributes.clone(); 
	}
}
