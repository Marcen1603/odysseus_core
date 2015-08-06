package de.uniol.inf.is.odysseus.iql.basic.typing.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmArrayType;
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
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLClasspathTypeProviderFactory;

@SuppressWarnings("restriction")
public abstract class AbstractIQLTypeUtils implements IIQLTypeUtils {
	

	@Inject
	protected IQLClasspathTypeProviderFactory typeProviderFactory;
	
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
		JvmType type = typeProviderFactory.findOrCreateTypeProvider(EcoreUtil2.getResourceSet(context)).findTypeByName(name);
		if (type != null) {
			return createTypeRef(type);
		}
		return null;
	}
	
	@Override
	public JvmTypeReference createTypeRef(JvmType typeRef) {
		IQLSimpleTypeRef simpleTypeRef = BasicIQLFactory.eINSTANCE.createIQLSimpleTypeRef();
		IQLSimpleType simpleType = BasicIQLFactory.eINSTANCE.createIQLSimpleType();		
		simpleType.setType(typeRef);
		simpleTypeRef.setType(simpleType);
		return simpleTypeRef;
	}
	
	@Override
	public Class<?> getJavaType(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
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
		return getLongName(typeRef, array, true);
	}
	
	@Override
	public String getLongName(JvmTypeReference typeRef, boolean array, boolean parameterized) {
		try {
			if (typeRef instanceof IQLSimpleTypeRef) {
				return getLongName(typeRef.getType(), array);
			} else if (typeRef instanceof IQLArrayTypeRef) {
				return getLongName(typeRef.getType(), array);		
			} else if (parameterized) {
				return typeRef.getIdentifier();
			} else {
				return getLongName(typeRef.getType(), array);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@Override
	public String getLongName(JvmType type, boolean array) {
		try {
			if (type instanceof IQLSimpleType) {
				IQLSimpleType simpleType = (IQLSimpleType) type;
				return getLongName(simpleType.getType(), array);
			} else if (type instanceof IQLArrayType && array) {
				IQLArrayType arrayType = (IQLArrayType) type;				
				StringBuilder b = new StringBuilder();
				b.append(getLongName(arrayType.getType(), array));
				for (int i = 0; i < arrayType.getDimensions().size(); i++) {
					b.append("[]");
				}
				return b.toString();	
			} else if (type instanceof IQLArrayType) {
				IQLArrayType arrayType = (IQLArrayType) type;	
				return getLongName(arrayType.getType(), array);
			} else if (type instanceof JvmArrayType && !array) {
				JvmArrayType arrayType = (JvmArrayType) type;	
				return getLongName(arrayType.getComponentType(), array);
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
				return type.getIdentifier();
			} else {
				return type.getIdentifier();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
		
	}
	
	@Override
	public String getShortName(JvmTypeReference typeRef, boolean array) {
		try {
			if (typeRef instanceof IQLSimpleTypeRef) {
				return getShortName(typeRef.getType(), array);
			} else if (typeRef instanceof IQLArrayTypeRef) {
				return getShortName(typeRef.getType(), array);			
			} else {
				return getShortName(typeRef.getType(), array);			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@Override
	public String getShortName(JvmType type, boolean array) {
		try {
			if (type instanceof IQLSimpleType) {
				IQLSimpleType simpleType = (IQLSimpleType) type;
				return getShortName(simpleType.getType(), array);
			} else if (type instanceof IQLArrayType && array) {				
				IQLArrayType arrayType = (IQLArrayType) type;
				StringBuilder b = new StringBuilder();
				b.append(getShortName(arrayType.getType(), array));
				for (int i = 0; i < arrayType.getDimensions().size(); i++) {
					b.append("[]");
				}
				return b.toString();
			} else if (type instanceof IQLArrayType) {	
				IQLArrayType arrayType = (IQLArrayType) type;	
				return getShortName(arrayType.getType(), array);
				
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
		
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
	
	protected JvmType getInnerType(JvmType type, boolean array) {
		if (type instanceof IQLSimpleType) {
			IQLSimpleType simpleType = (IQLSimpleType) type;
			return getInnerType(simpleType.getType(), array);
		} else if (type instanceof IQLArrayType && array) {
			return type;
		} else if (type instanceof JvmArrayType && array) {
			return type;
		} else if (type instanceof IQLArrayType && !array) {
			IQLArrayType arrayType = (IQLArrayType) type;
			return getInnerType(arrayType.getType(), array);
		} else if (type instanceof JvmArrayType && !array) {
			JvmArrayType arrayType = (JvmArrayType) type;
			return getInnerType(arrayType.getComponentType(), array);
		}else {
			return type;
		}
	}
	
	
	@Override
	public boolean isByte(JvmTypeReference typeRef) {
		String name = getLongName(typeRef, true);		
		return name.equals(Long.class.getCanonicalName()) || name.equals("byte");
	}
	
	@Override
	public boolean isShort(JvmTypeReference typeRef) {
		String name = getLongName(typeRef, true);		
		return name.equals(Long.class.getCanonicalName()) || name.equals("short");
	}
	
	@Override
	public boolean isInt(JvmTypeReference typeRef) {
		String name = getLongName(typeRef, true);		
		return name.equals(Long.class.getCanonicalName()) || name.equals("int");
	}

	@Override
	public boolean isLong(JvmTypeReference typeRef) {
		String name = getLongName(typeRef, true);		
		return name.equals(Long.class.getCanonicalName()) || name.equals("long");
	}

	@Override
	public boolean isFloat(JvmTypeReference typeRef) {
		String name = getLongName(typeRef, true);		
		return name.equals(Float.class.getCanonicalName()) || name.equals("float");
	}

	@Override
	public boolean isDouble(JvmTypeReference typeRef) {
		String name = getLongName(typeRef, true);		
		return name.equals(Double.class.getCanonicalName()) || name.equals("double");
	}

	@Override
	public boolean isByte(JvmTypeReference typeRef, boolean wrapper) {
		String name = getLongName(typeRef, true);		
		return (wrapper && name.equals(Byte.class.getCanonicalName())) || (!wrapper && name.equals("byte"));
	}
	
	@Override
	public boolean isShort(JvmTypeReference typeRef, boolean wrapper) {
		String name = getLongName(typeRef, true);		
		return (wrapper && name.equals(Short.class.getCanonicalName())) ||  (!wrapper && name.equals("short"));
	}
	
	@Override
	public boolean isInt(JvmTypeReference typeRef, boolean wrapper) {
		String name = getLongName(typeRef, true);		
		return (wrapper && name.equals(Integer.class.getCanonicalName())) ||  (!wrapper && name.equals("int"));
	}

	@Override
	public boolean isLong(JvmTypeReference typeRef, boolean wrapper) {
		String name = getLongName(typeRef, true);		
		return (wrapper && name.equals(Long.class.getCanonicalName())) ||  (!wrapper && name.equals("long"));
	}

	@Override
	public boolean isFloat(JvmTypeReference typeRef, boolean wrapper) {
		String name = getLongName(typeRef, true);		
		return (wrapper && name.equals(Float.class.getCanonicalName())) ||  (!wrapper && name.equals("float"));
	}

	@Override
	public boolean isDouble(JvmTypeReference typeRef, boolean wrapper) {
		String name = getLongName(typeRef, true);		
		return (wrapper && name.equals(Double.class.getCanonicalName())) ||  (!wrapper && name.equals("double"));
	}
	
	@Override
	public boolean isBoolean(JvmTypeReference typeRef) {
		String name = getLongName(typeRef, true);		
		return name.equals(Boolean.class.getCanonicalName()) || name.equals("boolean");
	}
	
	@Override
	public boolean isVoid(JvmTypeReference typeRef) {
		String name = getLongName(typeRef, true);		
		return name.equals(Void.class.getCanonicalName()) || name.equals("void");
	}
	
	@Override
	public boolean isString(JvmTypeReference typeRef) {
		String name = getLongName(typeRef, true);		
		return name.equals(String.class.getCanonicalName());
	}	
	
	@Override
	public boolean isMap(JvmTypeReference typeRef) {
		if (isUserDefinedType(typeRef, false)) {
			return false;
		} else {
			Class<?> c = getJavaType(getLongName(typeRef, true));
			if (c != null) {
				return Map.class.isAssignableFrom(c);
			} else {
				return false;
			}
		}
	}
	
	@Override
	public boolean isList(JvmTypeReference typeRef) {
		if (isUserDefinedType(typeRef, true)) {
			return false;
		} else if (getInnerType(typeRef, true) instanceof IQLArrayType) {
			return true;
		} else if (getInnerType(typeRef, true) instanceof JvmArrayType) {
			return true;
		} else {
			Class<?> c = getJavaType(getLongName(typeRef, true));
			if (c != null) {
				return List.class.isAssignableFrom(c);
			} else {
				return false;
			}
		}
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
	public boolean isClonable(JvmTypeReference typeRef) {
		if (isUserDefinedType(typeRef, false)) {
			return false;
		} else {
			Class<?> c = getJavaType(getLongName(typeRef, true));
			if (c != null) {
				try {
					Method m = c.getMethod("clone");
					return Modifier.isPublic(m.getModifiers());
				} catch (NoSuchMethodException | SecurityException e) {
					//e.printStackTrace();
					return false;
				}	
			} else {
				return false;
			}
		}
	}
	
	@Override
	public boolean isUserDefinedType(JvmTypeReference typeRef, boolean array) {
		return isUserDefinedType(getInnerType(typeRef, array), array);
	}
	
	@Override
	public boolean isUserDefinedType(JvmType type, boolean array) {
		return getInnerType(type, array) instanceof IQLClass | getInnerType(type, array) instanceof IQLInterface;
	}
	
	@Override
	public boolean isSetter(JvmOperation method) {
		boolean name = method.getSimpleName().startsWith("set");		
		boolean returnType = method.getReturnType() == null || getShortName(method.getReturnType(), true).equals("void");
		boolean parameters = method.getParameters().size() == 1;
		return name && returnType && parameters;
	}

	@Override
	public boolean isGetter(JvmOperation method) {
		boolean name =  method.getSimpleName().startsWith("get") || method.getSimpleName().startsWith("is");
		boolean returnType = method.getReturnType() != null;
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
