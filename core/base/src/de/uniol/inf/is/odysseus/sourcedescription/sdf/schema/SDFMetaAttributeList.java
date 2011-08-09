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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.lang.reflect.Method;

import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class SDFMetaAttributeList extends SDFSchemaElementSet<SDFMetaAttribute> {

	private static final long serialVersionUID = -1194514879108835243L;

	public SDFMetaAttributeList() {
	}

	public SDFMetaAttributeList(SDFMetaAttributeList attributes1) {
		super(attributes1);
	}

	public SDFAttributeList convertToSDFAttributeList(){
		SDFAttributeList list = new SDFAttributeList();
		for(SDFMetaAttribute metaattribute : this){
			Class<?> metaClass = metaattribute.getMetaAttributeClass();
			for(Method m : metaClass.getMethods()){
				SDFDatatype returnSDFType = getAccordingSDFDataType(m.getReturnType());
				if((m.getParameterTypes().length==0) && (returnSDFType!=null)){
					SDFAttribute a = new SDFAttribute(metaClass.getName(), m.getName());
					a.setDatatype(returnSDFType);
					list.add(a);
				}
			}
		}
		
		return list;		
	}

	public static SDFMetaAttributeList union(SDFMetaAttributeList attributes1, SDFMetaAttributeList attributes2) {
		if (attributes1 == null || attributes1.size() == 0) {
			return attributes2;
		}
		if (attributes2 == null || attributes2.size() == 0) {
			return attributes1;
		}
		SDFMetaAttributeList newSet = new SDFMetaAttributeList(attributes1);
		for (int i = 0; i < attributes2.size(); i++) {
			if (!newSet.contains(attributes2.get(i))) {
				newSet.add(attributes2.get(i));
			}
		}
		return newSet;
	}

	private SDFDatatype getAccordingSDFDataType(Class<?> returnType) {
		if (returnType.equals(Integer.class) || returnType.equals(int.class)) {
			return SDFDatatype.INTEGER;			
		}
		if (returnType.equals(Double.class) || returnType.equals(double.class)) {
			return SDFDatatype.DOUBLE;
		}
		if (returnType.equals(Long.class) || returnType.equals(long.class)) {
			return SDFDatatype.LONG;
		}
		if (returnType.equals(PointInTime.class)) {
			return SDFDatatype.POINT_IN_TIME;
		}
		return null;
	}
}
