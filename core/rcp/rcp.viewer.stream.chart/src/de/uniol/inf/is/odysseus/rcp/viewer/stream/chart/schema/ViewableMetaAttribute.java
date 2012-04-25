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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class ViewableMetaAttribute extends AbstractViewableAttribute {

	final public SDFMetaAttribute metaAttribute;
	final public Method method;

	public ViewableMetaAttribute(SDFMetaAttribute attribute, Method m, int port) {
		super(port);
		this.metaAttribute = attribute;
		this.method = m;
	}

	@Override
	public String getName() {
		return metaAttribute.getAttributeName() + "." + method.getName()+"("+getPort()+")";
	}

	@Override
	public SDFDatatype getSDFDatatype() {
		return getAccordingSDFDataType(method.getReturnType());
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
		return new SDFDatatype(getName());
	}
	
	@Override
	public Object evaluate(Tuple<? extends IMetaAttribute> tuple) {
		try {
			Object value = method.invoke(tuple.getMetadata());							
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
