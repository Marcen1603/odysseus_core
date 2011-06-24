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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import java.lang.reflect.Method;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class ViewableMetaAttribute implements IViewableAttribute {

	public SDFMetaAttribute metaAttribute;
	public Method method;

	public ViewableMetaAttribute(SDFMetaAttribute attribute, Method m) {
		this.metaAttribute = attribute;
		this.method = m;
	}

	@Override
	public String getName() {
		return metaAttribute.getAttributeName() + "." + method.getName();
	}

	@Override
	public SDFDatatype getSDFDatatype() {
		return getAccordingSDFDataType(method.getReturnType());
	}

	private SDFDatatype getAccordingSDFDataType(Class<?> returnType) {
		if (returnType.equals(Integer.class) || returnType.equals(int.class)) {
			return GlobalState.getActiveDatadictionary().getDatatype("Integer");
		}
		if (returnType.equals(Double.class) || returnType.equals(double.class)) {
			return GlobalState.getActiveDatadictionary().getDatatype("Double");
		}
		if (returnType.equals(Long.class) || returnType.equals(long.class)) {
			return GlobalState.getActiveDatadictionary().getDatatype("Long");
		}
		if (returnType.equals(PointInTime.class)) {
			return GlobalState.getActiveDatadictionary().getDatatype("PointInTime");
		}
		return new SDFDatatype(getName());
	}
	
	@Override
	public Object evaluate(RelationalTuple<? extends IMetaAttribute> tuple) {
		try {
			Object value = method.invoke(tuple.getMetadata());							
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
