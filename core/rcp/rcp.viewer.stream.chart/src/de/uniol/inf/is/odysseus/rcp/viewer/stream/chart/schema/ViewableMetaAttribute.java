package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema;

import java.lang.reflect.Method;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttribute;

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
			return SDFDatatypeFactory.getDatatype("Integer");
		}
		if (returnType.equals(Double.class) || returnType.equals(double.class)) {
			return SDFDatatypeFactory.getDatatype("Double");
		}
		if (returnType.equals(Long.class) || returnType.equals(long.class)) {
			return SDFDatatypeFactory.getDatatype("Long");
		}
		if (returnType.equals(PointInTime.class)) {
			return SDFDatatypeFactory.getDatatype("PointInTime");
		}
		return new SDFDatatype(getName());
	}
	
	@Override
	public Object evaluate(int index, RelationalTuple<? extends IMetaAttribute> tuple) {
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
