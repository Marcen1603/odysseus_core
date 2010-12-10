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
		return null;
	}
}
