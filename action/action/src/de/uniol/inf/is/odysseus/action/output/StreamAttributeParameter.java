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
package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.action.exception.AttributeParsingException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * Class representing an attribute of a datastream element.
 * Used as a parameter for
 * @author Simon Flandergan
 *
 */
public class StreamAttributeParameter implements IActionParameter{
	private Class<?> datatype;
	private Object identifier;
	
	public StreamAttributeParameter(Class<?> datatype, Object identifier) {
		this.datatype = datatype;
		this.identifier = identifier;
	}
	
	public StreamAttributeParameter(SDFDatatype datatype, Object identifier) throws AttributeParsingException{
		String datatypeName = datatype.getQualName().toLowerCase();
		//identify datatype
		if(datatypeName.equals("byte")){
			this.datatype = Byte.class;
		}else if(datatypeName.equals("short")){
			this.datatype = Short.class;
		}else if(datatypeName.equals("integer")){
			this.datatype = Integer.class;
		}else if(datatypeName.equals("long")){
			this.datatype = Long.class;
		}else if(datatypeName.equals("float")){
			this.datatype = Float.class;
		}else if(datatypeName.equals("double")){
			this.datatype = Double.class;
		}else if(datatypeName.equals("character")){
			this.datatype = Character.class;
		}else if(datatypeName.equals("string")){
			this.datatype = String.class;
		}else {
			throw new AttributeParsingException(
					"Unknown datatype for attribute <"+
					identifier+">");
		}
		this.identifier = identifier;
	}
	

	@Override
	public Class<?> getParamClass() {
		return datatype;
	}

	@Override
	public ParameterType getType() {
		return ParameterType.Attribute;
	}

	@Override
	public Object getValue() {
		return this.identifier;
	}
}
