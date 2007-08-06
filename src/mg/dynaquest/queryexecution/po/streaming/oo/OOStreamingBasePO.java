package mg.dynaquest.queryexecution.po.streaming.oo;

import java.lang.reflect.Method;
import java.util.Map;

import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;

public abstract class OOStreamingBasePO extends UnaryPlanOperator {

	public OOStreamingBasePO() {
		super();
	}
	
	public OOStreamingBasePO(AlgebraPO algebraPO) {
		super(algebraPO);
	}
	
	public OOStreamingBasePO(OOStreamingBasePO operator) {
		super(operator);
	}
	/**
	 * Get the value of a (nested) attribute of an Object.
	 * The attribute is specified by its name. Nested Attributes
	 * have to be separated by a '.' (e.g. "foo.bar").
	 * The attributes have to be accessible by a get method which conforms
	 * to the Java Coding Conventions. (e.g. for "foo.bar" => getFoo().getBar())
	 * The attribute has to be of type String or Number.
	 */
	protected Object getAttributeValue(SDFAttribute attribute, Map<String, Object> objectMap) throws Exception {
		String str = attribute.getURI(false);
		String[] attributes = str.split("\\.");
		Object object = objectMap.get(attributes[0]);
		if (object == null) {
			throw new NullPointerException("invalid attribute");
		}
		
		for (int i = 1; i < attributes.length; ++i) {
			String curAttribute = attributes[i];
			StringBuffer b = new StringBuffer("get");
			b.append(curAttribute);
			b.setCharAt(3, Character.toUpperCase(curAttribute.charAt(0)));
			Method m = object.getClass().getMethod(b.toString(), new Class[0]);
			object = m.invoke(object);
		}
	
		return object;
//		if (object instanceof Number) {
//			// TODO uri erstellen
//			return new SDFNumberConstant("TODOCreateURI", ((Number) object)
//					.doubleValue());
//		}
//	
//		if (object instanceof String) {
//			// TODO uri erstellen
//			return new SDFStringConstant("TODOCreateURI", (String) object);
//		}
//	
//		throw new IllegalArgumentException("Attribute is not of type Number or String");
	}

	protected SDFConstant getSDFAttributeValue(SDFAttribute curAttribute, Map<String, Object> cargo) throws Exception {
		Object o = getAttributeValue(curAttribute, cargo);
		if (o instanceof Number) {
			return new SDFNumberConstant("TODOURI", ((Number)o).doubleValue());
		}
		
		if (o instanceof String) {
			return new SDFStringConstant("TODOURIS", ((String)o));
		}
		
		throw new Exception("attribute is not of type String or Number");
	}

}