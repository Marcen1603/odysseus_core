package de.uniol.inf.is.odysseus.iql.basic.typing.utils;


import javax.inject.Inject;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmArrayType;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmLowerBound;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmPrimitiveType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeConstraint;
import org.eclipse.xtext.common.types.JvmTypeParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmUpperBound;




import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLClasspathTypeProviderFactory;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;

@SuppressWarnings("restriction")
public abstract class AbstractIQLTypeUtils implements IIQLTypeUtils {
	

	@Inject
	protected IQLClasspathTypeProviderFactory typeProviderFactory;

	@Inject
	protected IQLQualifiedNameConverter converter;
	
	
	@Override
	public JvmTypeReference getWrapper(JvmTypeReference valueType, Notifier context) {
		if (isByte(valueType)) {
			return createTypeRef(Byte.class, context);
		} else if (isShort(valueType)) {
			return createTypeRef(Short.class, context);
		} else if (isInt(valueType)) {
			return createTypeRef(Integer.class, context);
		} else if (isLong(valueType)) {
			return createTypeRef(Long.class, context);
		} else if (isFloat(valueType)) {
			return createTypeRef(Float.class, context);
		} else if (isDouble(valueType)) {
			return createTypeRef(Double.class, context);
		} else if (isBoolean(valueType)) {
			return createTypeRef(Boolean.class, context);
		} else if (isCharacter(valueType)) {
			return createTypeRef(Character.class, context);
		} else {
			return null;
		}
	}
	
	@Override
	public JvmTypeReference createTypeRef(Class<?> javaType, Notifier context) {
		JvmType type = typeProviderFactory.findOrCreateTypeProvider(EcoreUtil2.getResourceSet(context)).findTypeByName(javaType.getCanonicalName());
		if (type != null) {
			return createTypeRef(type);
		}
		return null;
	}
	
	
	@Override
	public JvmTypeReference createTypeRef(String name, Notifier context) {
		JvmType type = typeProviderFactory.findOrCreateTypeProvider(EcoreUtil2.getResourceSet(context)).findTypeByName(converter.toJavaString(name));
		if (type != null) {
			return createTypeRef(type);
		}
		return null;
	}
	
	@Override
	public JvmTypeReference createTypeRef(JvmType type) {
		IQLSimpleTypeRef simpleTypeRef = BasicIQLFactory.eINSTANCE.createIQLSimpleTypeRef();
		simpleTypeRef.setType(type);
		return simpleTypeRef;
	}

	
	@Override
	public boolean isPrimitive(JvmTypeReference parameterType) {
		return isPrimitive(getInnerType(parameterType, true));
	}
	
	@Override
	public boolean isPrimitive(JvmType type) {
		return type instanceof JvmPrimitiveType;
	}		

	@Override
	public String getLongName(JvmTypeReference typeRef, boolean array) {
		if (typeRef instanceof IQLSimpleTypeRef) {
			return getLongName(typeRef.getType(), array);
		} else if (typeRef instanceof IQLArrayTypeRef) {
			return getLongName(typeRef.getType(), array);		
		} else {
			return getLongName(typeRef.getType(), array);
		}
	}
	
	@Override
	public String getLongName(JvmType type, boolean array) {
		String result = "";
		if (type instanceof IQLArrayType && array) {
			IQLArrayType arrayType = (IQLArrayType) type;				
			StringBuilder b = new StringBuilder();
			b.append(getLongName(arrayType.getComponentType(), array));
			for (int i = 0; i < arrayType.getDimensions().size(); i++) {
				b.append("[]");
			}
			result = b.toString();	
		} else if (type instanceof IQLArrayType) {
			IQLArrayType arrayType = (IQLArrayType) type;	
			result = getLongName(arrayType.getComponentType(), array);
		} else if (type instanceof JvmArrayType && !array) {
			JvmArrayType arrayType = (JvmArrayType) type;	
			result = getLongName(arrayType.getComponentType(), array);
		} else if (type instanceof JvmTypeParameter) {
			JvmTypeParameter typeParameter = (JvmTypeParameter) type;
			for (JvmTypeConstraint constraint : typeParameter.getConstraints()) {
				if (constraint instanceof JvmUpperBound) {
					JvmUpperBound upperBound = (JvmUpperBound) constraint;
					return getLongName(upperBound.getTypeReference(), array);
				} else if (constraint instanceof JvmLowerBound) {
					JvmLowerBound lowerBound = (JvmLowerBound) constraint;
					return getLongName(lowerBound.getTypeReference(), array);
				}
			}
			result = type.getIdentifier();
		} else if (isUserDefinedType(type, array)) {				
			result = getLongNameOfUserDefinedType((JvmGenericType) type);
		} else {
			result = type.getIdentifier();
		}
		return converter.toIQLString(result);
		
	}
	
	protected String getLongNameOfUserDefinedType(JvmGenericType type) {
		return type.getIdentifier();
	}

	
	@Override
	public String getShortName(JvmTypeReference typeRef, boolean array) {
		if (typeRef instanceof IQLSimpleTypeRef) {
			return getShortName(typeRef.getType(), array);
		} else if (typeRef instanceof IQLArrayTypeRef) {
			return getShortName(typeRef.getType(), array);			
		} else {
			return getShortName(typeRef.getType(), array);			
		}
	}
	
	@Override
	public String getShortName(JvmType type, boolean array) {
		if (type instanceof IQLArrayType && array) {				
			IQLArrayType arrayType = (IQLArrayType) type;
			StringBuilder b = new StringBuilder();
			b.append(getShortName(arrayType.getComponentType(), array));
			for (int i = 0; i < arrayType.getDimensions().size(); i++) {
				b.append("[]");
			}
			return b.toString();
		} else if (type instanceof IQLArrayType) {	
			IQLArrayType arrayType = (IQLArrayType) type;	
			return getShortName(arrayType.getComponentType(), array);
			
		} else if (type instanceof JvmArrayType && !array) {	
			JvmArrayType arrayType = (JvmArrayType) type;	
			return getShortName(arrayType.getComponentType(), array);
			
		} else if (type instanceof JvmTypeParameter) {
			JvmTypeParameter typeParameter = (JvmTypeParameter) type;
			for (JvmTypeConstraint constraint : typeParameter.getConstraints()) {
				if (constraint instanceof JvmUpperBound) {
					JvmUpperBound upperBound = (JvmUpperBound) constraint;
					return getShortName(upperBound.getTypeReference(), array);
				} else if (constraint instanceof JvmLowerBound) {
					JvmLowerBound lowerBound = (JvmLowerBound) constraint;
					return getShortName(lowerBound.getTypeReference(), array);
				}
			}
			return type.getSimpleName();
		} else {
			return type.getSimpleName();
		}		
	}
	
	@Override
	public JvmType getInnerType(JvmTypeReference typeRef, boolean array) {
		if (typeRef instanceof IQLSimpleTypeRef) {
			IQLSimpleTypeRef simpleTypeRef = (IQLSimpleTypeRef) typeRef;
			return getInnerType(simpleTypeRef.getType(), array);
		} else if (typeRef instanceof IQLArrayTypeRef) {
			IQLArrayTypeRef arrayTypeRef = (IQLArrayTypeRef) typeRef;
			return getInnerType(arrayTypeRef.getType(), array);
		} else {
			return getInnerType(typeRef.getType(), array);
		}
	}
	
	@Override
	public int getArraySize(JvmType type) {
		if (type instanceof JvmArrayType) {
			return ((JvmArrayType) type).getDimensions();
		} else if (type instanceof IQLArrayType) {
			return ((IQLArrayType) type).getDimensions().size();
		} else {
			return 0;
		}
	}
	
	@Override
	public int getArrayDim(JvmTypeReference typeRef) {
		return getArraySize(getInnerType(typeRef, true));
	}
	
	protected JvmType getInnerType(JvmType type, boolean array) {
		 if (type instanceof IQLArrayType && array) {
			return type;
		} else if (type instanceof JvmArrayType && array) {
			return type;
		} else if (type instanceof IQLArrayType && !array) {
			IQLArrayType arrayType = (IQLArrayType) type;
			return getInnerType(arrayType.getComponentType(), array);
		} else if (type instanceof JvmArrayType && !array) {
			JvmArrayType arrayType = (JvmArrayType) type;
			return getInnerType(arrayType.getComponentType(), array);
		} else if (type instanceof JvmTypeParameter) {
			JvmTypeParameter typeParameter = (JvmTypeParameter) type;
			for (JvmTypeConstraint constraint : typeParameter.getConstraints()) {
				if (constraint instanceof JvmUpperBound) {
					JvmUpperBound upperBound = (JvmUpperBound) constraint;
					return getInnerType(upperBound.getTypeReference(), array);
				} else if (constraint instanceof JvmLowerBound) {
					JvmLowerBound lowerBound = (JvmLowerBound) constraint;
					return getInnerType(lowerBound.getTypeReference(), array);
				}
			}
			return type;
		}else {
			return type;
		}
	}
	
	
	@Override
	public boolean isByte(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(Byte.class.getCanonicalName()) || name.equals("byte");
	}
	
	@Override
	public boolean isShort(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(Short.class.getCanonicalName()) || name.equals("short");
	}
	
	@Override
	public boolean isInt(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(Integer.class.getCanonicalName()) || name.equals("int");
	}

	@Override
	public boolean isLong(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(Long.class.getCanonicalName()) || name.equals("long");
	}

	@Override
	public boolean isFloat(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(Float.class.getCanonicalName()) || name.equals("float");
	}
	
	@Override
	public boolean isCharacter(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(Character.class.getCanonicalName()) || name.equals("char");
	}

	@Override
	public boolean isDouble(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(Double.class.getCanonicalName()) || name.equals("double");
	}

	@Override
	public boolean isByte(JvmTypeReference typeRef, boolean wrapper) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return (wrapper && name.equals(Byte.class.getCanonicalName())) || (!wrapper && name.equals("byte"));
	}
	
	@Override
	public boolean isCharacter(JvmTypeReference typeRef, boolean wrapper) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return (wrapper && name.equals(Character.class.getCanonicalName())) || (!wrapper && name.equals("char"));
	}
	
	
	@Override
	public boolean isShort(JvmTypeReference typeRef, boolean wrapper) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return (wrapper && name.equals(Short.class.getCanonicalName())) ||  (!wrapper && name.equals("short"));
	}
	
	@Override
	public boolean isInt(JvmTypeReference typeRef, boolean wrapper) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return (wrapper && name.equals(Integer.class.getCanonicalName())) ||  (!wrapper && name.equals("int"));
	}

	@Override
	public boolean isLong(JvmTypeReference typeRef, boolean wrapper) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return (wrapper && name.equals(Long.class.getCanonicalName())) ||  (!wrapper && name.equals("long"));
	}

	@Override
	public boolean isFloat(JvmTypeReference typeRef, boolean wrapper) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return (wrapper && name.equals(Float.class.getCanonicalName())) ||  (!wrapper && name.equals("float"));
	}

	@Override
	public boolean isDouble(JvmTypeReference typeRef, boolean wrapper) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return (wrapper && name.equals(Double.class.getCanonicalName())) ||  (!wrapper && name.equals("double"));
	}
	
	@Override
	public boolean isBoolean(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(Boolean.class.getCanonicalName()) || name.equals("boolean");
	}
	
	@Override
	public boolean isVoid(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(Void.class.getCanonicalName()) || name.equals("void");
	}
	
	@Override
	public boolean isString(JvmTypeReference typeRef) {
		String name = converter.toJavaString(getLongName(typeRef, true));		
		return name.equals(String.class.getCanonicalName());
	}	
	
	
	@Override
	public boolean isArray(JvmTypeReference typeRef) {
		if (getInnerType(typeRef, true) instanceof JvmArrayType) {
			return true;
		} else if (getInnerType(typeRef, true) instanceof IQLArrayType) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public JvmTypeReference getComponentType(JvmTypeReference arrayTypeRef) {
		if (getInnerType(arrayTypeRef, true) instanceof JvmArrayType) {
			return createTypeRef(((JvmArrayType)getInnerType(arrayTypeRef, true)).getComponentType());
		} else if (getInnerType(arrayTypeRef, true) instanceof IQLArrayType) {
			return createTypeRef(((IQLArrayType)getInnerType(arrayTypeRef, true)).getComponentType());
		} else {
			return null;
		}
	}
	
	@Override
	public boolean isUserDefinedType(JvmTypeReference typeRef, boolean array) {
		return isUserDefinedType(getInnerType(typeRef, array), array);
	}
	
	@Override
	public boolean isUserDefinedType(JvmType type, boolean array) {
		JvmType innerType = getInnerType(type, array);
		if (innerType instanceof JvmGenericType) {
			JvmGenericType genericType = (JvmGenericType) innerType;
			if (genericType.getPackageName() == null) {
				return innerType instanceof IQLClass | innerType instanceof IQLInterface; 
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isSetter(JvmOperation method) {
		boolean name = method.getSimpleName().startsWith("set");		
		boolean returnType = method.getReturnType() == null || getShortName(method.getReturnType(), true).equalsIgnoreCase("void");
		boolean parameters = method.getParameters().size() == 1;
		return name && returnType && parameters;
	}

	@Override
	public boolean isGetter(JvmOperation method) {
		boolean name =  method.getSimpleName().startsWith("get") || method.getSimpleName().startsWith("is");
		boolean returnType = method.getReturnType() != null && !getShortName(method.getReturnType(), true).equalsIgnoreCase("void");;
		boolean parameters = method.getParameters().size() == 0;
		return name && returnType && parameters;
	}
	


	@Override
	public String getNameWithoutSetterPrefix(JvmOperation method) {		
		return method.getSimpleName().substring(3);
	}

	@Override
	public String getNameWithoutGetterPrefix(JvmOperation method) {
		if (method.getSimpleName().startsWith("get")) {
			return method.getSimpleName().substring(3);
		} else {
			return method.getSimpleName().substring(2);
		}
	}

}
