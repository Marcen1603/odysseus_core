package de.uniol.inf.is.odysseus.iql.basic.typing.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmArrayType;
import org.eclipse.xtext.common.types.JvmPrimitiveType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLDefaultTypes;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;
import de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint.IIQLTypingEntryPoint;




@SuppressWarnings("restriction")
public abstract class AbstractIQLTypeFactory implements IIQLTypeFactory{

	@Inject
	private XtextResourceSet systemResourceSet;

	@Inject
	protected TypeReferences typeReferences;
	
	@Inject
	IIQLTypingEntryPoint entryPoint;	
	
	private Map<Resource, Collection<JvmType>> resourceTypes = new HashMap<>();
	
	private Map<String, IQLFile> systemFiles = new HashMap<>();
	private Map<String, IQLSystemType> systemTypes= new HashMap<>();


	@Override
	public ResourceSet getSystemResourceSet() {
		return systemResourceSet;
	}
	
	protected boolean isPrimitive(JvmTypeReference parameterType) {
		return isPrimitive(getInnerType(parameterType, true));
	}
	
	protected boolean isPrimitive(JvmType  type) {
		return type instanceof JvmPrimitiveType;
	}
	
	@Override
	public Collection<JvmType> getVisibleTypes(Resource context) {
		Collection<JvmType> result = resourceTypes.get(context);
		if (result == null) {
			result = createVisibleTypes(context);
			resourceTypes.put(context, result);
		}
		return result;
	}
	
	@Override
	public Collection<String> getImplicitImports() {
		Collection<String> implicitImports = createImplicitImports();
		return implicitImports;
	}


	public Collection<String> createImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		for (JvmType systemType : getSystemTypes()) {
			implicitImports.add(getLongName(systemType, false));
		}
		implicitImports.addAll(IQLDefaultTypes.getImplicitImports());		
		return implicitImports;
	}
	
	
	
	@Override
	public Collection<IQLFile> getSystemFiles() {
		return systemFiles.values();
	}
	

	protected abstract IQLFile createCleanSystemFile();
	
	@Override
	public IQLSystemType addSystemType(IQLTypeDef typeDef, Class<?> javaType) {
		resourceTypes.clear();
		IQLSystemType systemType=  new IQLSystemType(typeDef, javaType);
		systemTypes.put(getLongName(typeDef, false), systemType);		
		String packageName = typeDef.getPackageName();
		IQLFile systemFile = systemFiles.get(packageName);
		if (systemFile == null) {
			systemFile = createCleanSystemFile();		
			systemFile.setName(packageName);
			systemFiles.put(packageName, systemFile);
		}
		systemFile.getElements().add(typeDef);	
		return systemType;
	}
	
	@Override
	public void removeSystemType(String name) {
		IQLSystemType systemType = systemTypes.remove(name);
		if (systemType != null) {
			String packageName = systemType.getIqlTypeDef().getPackageName();
			IQLFile systemFile = systemFiles.get(packageName);
			if (systemFile != null) {
				systemFile.getElements().remove(systemType.getIqlTypeDef());
				if (systemFile.getElements().size() == 0) {
					systemFiles.remove(systemFile);
				}
			}
		}
	}
	
	
	
	public Collection<JvmType> createVisibleTypes(Resource context) {
		Collection<JvmType> result = new HashSet<>();
		result.addAll(getPrimitives(context));
		result.addAll(getSystemTypes());
		
		for (Class<?> c : IQLDefaultTypes.getVisibleTypes()) {
			result.add(typeReferences.findDeclaredType(c.getCanonicalName(), context));
		}
		return result;
	}	
		
	private Collection<JvmType> getPrimitives(Resource context) {
		Collection<JvmType> result = new HashSet<>();
		result.add(typeReferences.findDeclaredType("byte", context));
		result.add(typeReferences.findDeclaredType("short", context));
		result.add(typeReferences.findDeclaredType("int", context));
		result.add(typeReferences.findDeclaredType("long", context));
		result.add(typeReferences.findDeclaredType("float", context));
		result.add(typeReferences.findDeclaredType("double", context));
		result.add(typeReferences.findDeclaredType("boolean", context));
		result.add(typeReferences.findDeclaredType("char", context));
		result.add(typeReferences.findDeclaredType(Byte.class, context));
		result.add(typeReferences.findDeclaredType(Short.class, context));
		result.add(typeReferences.findDeclaredType(Integer.class, context));
		result.add(typeReferences.findDeclaredType(Long.class, context));
		result.add(typeReferences.findDeclaredType(Float.class, context));
		result.add(typeReferences.findDeclaredType(Double.class, context));
		result.add(typeReferences.findDeclaredType(Boolean.class, context));
		result.add(typeReferences.findDeclaredType(Character.class, context));
		return result;
	}
	
	private Collection<JvmType> getSystemTypes() {
		Collection<JvmType> types = new HashSet<>();
		for (IQLSystemType systemType : systemTypes.values()) {
			types.add(systemType.getIqlTypeDef());
		}
		return types;
	}
	

	@Override
	public boolean isSystemFile(String fileName) {
		return getSystemFile(fileName)!= null;
	}

	@Override
	public IQLFile getSystemFile(String fileName) {
		return systemFiles.get(fileName);
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
	public boolean isImportNeeded(JvmType type, String text) {
		if (isSystemType(type, false)) {
			return true;
		} else if (isUserDefinedType(type, false)) {
			return false;
		} else if (isPrimitive(type)) {
			return false;
		}else {
			try {
				Class.forName(text);
				return false;
			} catch (Exception e) {
				return true;
			}
		}
	}

	@Override
	public String getImportName(JvmType type) {		
		String name = getLongName(type, false);
		IQLSystemType systemType = systemTypes.get(name);
		if (systemType != null) {
			Class<?> javaType = systemType.getJavaType();
			return javaType.getCanonicalName();
		} else {
			return name;
		}		
	}

	@Override
	public String getSimpleName(JvmType type, String text, boolean wrapper, boolean array) {
		String qualifiedName = getLongName(type, array);	
		IQLSystemType systemType = systemTypes.get(qualifiedName);
		if (systemType != null) {
			if (!isImportNeeded(type, text)) {
				return text;
			} else {
				Class<?> javaType = systemType.getJavaType();
				return javaType.getSimpleName();
			}
		} else {
			Class<?> javaType = getClass(qualifiedName);
			if (wrapper && type instanceof JvmPrimitiveType) {
				return toWrapper(qualifiedName);
			} else if (javaType != null) {
				if (!isImportNeeded(type, text)) {
					return text;
				} else {
					return javaType.getSimpleName();
				}
			} else if (text != null){
				return text;
			} else {
				return qualifiedName;
			}
		}
	}
	
	
	protected Class<?> getClass(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	
	protected String toWrapper(String name) {
		if (name.equals("byte")) {
			return Byte.class.getSimpleName();
		} else if (name.equals("short")) {
			return Short.class.getSimpleName();
		} else if (name.equals("int")) {
			return Integer.class.getSimpleName();
		} else if (name.equals("long")) {
			return Long.class.getSimpleName();
		} else if (name.equals("float")) {
			return Float.class.getSimpleName();
		} else if (name.equals("double")) {
			return Double.class.getSimpleName();
		} else if (name.equals("char")) {
			return Character.class.getSimpleName();
		} else if (name.equals("boolean")) {
			return Boolean.class.getSimpleName();
		} else {
			return name;
		}
	}	
	
	
	@Override
	public JvmTypeReference getTypeRef(Class<?> javaType) {
		JvmType type = typeReferences.findDeclaredType(javaType, systemResourceSet);
		if (type != null) {
			return getTypeRef(type);
		}
		return null;
	}
	
	
	@Override
	public JvmTypeReference getTypeRef(String name) {
		JvmType type = typeReferences.findDeclaredType(name, systemResourceSet);
		if (type != null) {
			return getTypeRef(type);
		}
		return null;
	}
	
	@Override
	public JvmTypeReference getTypeRef(JvmType typeRef) {
		IQLSimpleTypeRef simpleTypeRef = BasicIQLFactory.eINSTANCE.createIQLSimpleTypeRef();
		IQLSimpleType simpleType = BasicIQLFactory.eINSTANCE.createIQLSimpleType();		
		simpleType.setType(typeRef);
		simpleTypeRef.setType(simpleType);
		return simpleTypeRef;
	}
	
	
	@Override
	public Collection<Bundle> getDependencies(IQLFile file) {
		Collection<Bundle> bundles = new HashSet<>();
		bundles.add(Platform.getBundle("de.uniol.inf.is.odysseus.iql.basic"));
		
		for (JvmTypeReference type : EcoreUtil2.getAllContentsOfType(file, JvmTypeReference.class)) {
			addDependancy(type, bundles);
		}		
		return bundles;
	}
	
	private void addDependancy(JvmTypeReference typeRef, Collection<Bundle> bundles) {
		if (isSystemType(typeRef, false)) {
			IQLSystemType systemType = getSystemType(typeRef, false);
			Bundle bundle = FrameworkUtil.getBundle(systemType.getJavaType());
			if (bundle != null) {
				bundles.add(bundle);
			}
		} else if (!isUserDefinedType(typeRef, false)) {
			try {
				Class<?> c = Class.forName(getLongName(typeRef, false));
				Bundle bundle = FrameworkUtil.getBundle(c);
				if (bundle != null) {
					bundles.add(bundle);
				}
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}
		}
	}
	
	protected boolean isSystemType(JvmType type, boolean array) {
		return getSystemType(type, array) != null;
	}
	
	protected boolean isSystemType(JvmTypeReference typeRef, boolean array) {
		return getSystemType(typeRef, array) != null;
	}
	
	protected IQLSystemType getSystemType(JvmType type, boolean array) {
		String name = getLongName(type, array);
		return systemTypes.get(name);
	}
	
	protected IQLSystemType getSystemType(JvmTypeReference typeRef, boolean array) {
		return getSystemType(getInnerType(typeRef, array), array);				
	}
	
	
	@Override
	public boolean isMap(JvmTypeReference typeRef) {
		if (isSystemType(typeRef, true)) {
			return false;
		} else if (isUserDefinedType(typeRef, false)) {
			return false;
		} else {
			Class<?> c = getClass(getLongName(typeRef, true));
			if (c != null) {
				return Map.class.isAssignableFrom(c);
			} else {
				return false;
			}
		}
	}
	
	@Override
	public boolean isList(JvmTypeReference typeRef) {
		if (isSystemType(typeRef, true)) {
			return false;
		} else if (isUserDefinedType(typeRef, true)) {
			return false;
		} else if (getInnerType(typeRef, true) instanceof IQLArrayType) {
			return true;
		} else if (getInnerType(typeRef, true) instanceof JvmArrayType) {
			return true;
		} else {
			Class<?> c = getClass(getLongName(typeRef, true));
			if (c != null) {
				return List.class.isAssignableFrom(c);
			} else {
				return false;
			}
		}
	}
	
	@Override
	public boolean isArray(JvmTypeReference typeRef) {
		if (getInnerType(typeRef, true) instanceof IQLArrayType) {
			return true;
		} else if (getInnerType(typeRef, true) instanceof JvmArrayType) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public boolean isClonable(JvmTypeReference typeRef) {
		if (isSystemType(typeRef, true)) {
			return false;
		} else if (isUserDefinedType(typeRef, false)) {
			return false;
		} else {
			Class<?> c = getClass(getLongName(typeRef, true));
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
		return getInnerType(type, array) instanceof IQLTypeDef;
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
				return typeRef.getSimpleName();
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
				
			}else {
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
	
	private JvmType getInnerType(JvmType type, boolean array) {
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

}

