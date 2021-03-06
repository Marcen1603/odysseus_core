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
package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.util.Collection;
import java.util.Collections;

public class SDFMetaAttributeList extends SDFSchemaElementSet<SDFMetaAttribute> {

	private static final long serialVersionUID = -1194514879108835243L;

	public SDFMetaAttributeList() {
	}

	public SDFMetaAttributeList(String uri, SDFMetaAttributeList attributes1) {
		super(uri,attributes1);
	}

	public SDFMetaAttributeList(String uri, Collection<SDFMetaAttribute> attributes1) {
		super(uri, attributes1);
	}

	
//	public SDFSchema convertToSDFSchema(){
//		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
//		for(SDFMetaAttribute metaattribute : this){
//			Class<?> metaClass = metaattribute.getMetaAttributeClass();
//			for(Method m : metaClass.getMethods()){
//				SDFDatatype returnSDFType = getAccordingSDFDataType(m.getReturnType());
//				if((m.getParameterTypes().length==0) && (returnSDFType!=null)){
//					SDFAttribute a = new SDFAttribute(metaClass.getName(), m.getName(), returnSDFType);
//					attrs.add(a);
//				}
//			}
//		}
//		
//		return new SDFSchema("", attrs);		
//	}

	public static SDFMetaAttributeList union(SDFMetaAttributeList attributes1, SDFMetaAttributeList attributes2) {
		if (attributes1 == null || attributes1.size() == 0) {
			return attributes2;
		}
		if (attributes2 == null || attributes2.size() == 0) {
			return attributes1;
		}
		// TODO: Change name to union
		SDFMetaAttributeList newSet = new SDFMetaAttributeList("",attributes1);
		for (int i = 0; i < attributes2.size(); i++) {
			if (!newSet.contains(attributes2.get(i))) {
				newSet.elements.add(attributes2.get(i));
			}
		}
		return newSet;
	}

//	private static SDFDatatype getAccordingSDFDataType(Class<?> returnType) {
//		if (returnType.equals(Integer.class) || returnType.equals(int.class)) {
//			return SDFDatatype.INTEGER;			
//		}
//		if (returnType.equals(Double.class) || returnType.equals(double.class)) {
//			return SDFDatatype.DOUBLE;
//		}
//		if (returnType.equals(Long.class) || returnType.equals(long.class)) {
//			return SDFDatatype.LONG;
//		}
//		if (returnType.equals(PointInTime.class)) {
//			return SDFDatatype.POINT_IN_TIME;
//		}
//		return null;
//	}
	
    public Collection<SDFMetaAttribute> getAttributes(){
    	return Collections.unmodifiableCollection(elements);
    }
}
