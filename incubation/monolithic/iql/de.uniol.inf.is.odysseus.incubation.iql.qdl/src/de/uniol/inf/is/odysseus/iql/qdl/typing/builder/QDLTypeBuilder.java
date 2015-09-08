package de.uniol.inf.is.odysseus.iql.qdl.typing.builder;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.TypesFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.AbstractIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.DefaultQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.DefaultQDLSource;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class QDLTypeBuilder extends AbstractIQLTypeBuilder<IQDLTypeDictionary, IQDLTypeUtils> implements IQDLTypeBuilder {
	public static String[] OPERATORS_NAMESPACE = new String[]{"operators"};
	private static final String[] SOURCES_NAMESPACE = new String[]{"sources"};
	
	
	@Inject
	public QDLTypeBuilder(IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}

	@Override
	public void createSource(ILogicalOperator source) {
		String name = getSourceName(source);
		JvmTypeReference superClass = typeUtils.createTypeRef(DefaultQDLSource.class, typeDictionary.getSystemResourceSet());

		IQLClass sourceClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		sourceClass.setSimpleName(firstCharUpperCase(name));
		sourceClass.setPackageName(createQualifiedName(SOURCES_NAMESPACE));
		sourceClass.setExtendedClass(superClass);

		for (SDFAttribute attr : source.getOutputSchema().getAttributes()) {
			IQLAttribute attribute= BasicIQLFactory.eINSTANCE.createIQLAttribute();
			attribute.setSimpleName(attr.getAttributeName().toUpperCase());
			attribute.setType(typeUtils.createTypeRef(String.class, typeDictionary.getSystemResourceSet()));
			sourceClass.getMembers().add(attribute);
		}
		IQLAttribute sourceAttr = BasicIQLFactory.eINSTANCE.createIQLAttribute();
		sourceAttr.setSimpleName(firstCharLowerCase(name));
		sourceAttr.setType(typeUtils.createTypeRef(sourceClass));
		sourceClass.getMembers().add(sourceAttr);
		
		typeDictionary.addSystemType(sourceClass, DefaultQDLSource.class);
		typeDictionary.addSource(name, source, sourceClass);
	}
	
	@Override
	public void removeSource(ILogicalOperator source) {
		String name = firstCharUpperCase(source.getName().toLowerCase());
		removeSystemType(SOURCES_NAMESPACE, name);
		typeDictionary.removeSource(getSourceName(source));
	}
	
	private String getSourceName(ILogicalOperator source) {
		return source.getName();
	}
	
	@Override
	public void createOperator(IOperatorBuilder opBuilder) {
		String name = firstCharUpperCase(opBuilder.getName().toLowerCase());		
		String operatorClassName = opBuilder.getOperatorClass().getSimpleName();
		if (operatorClassName.endsWith("AO")) {
			operatorClassName = operatorClassName.substring(0, operatorClassName.length()-2);
		}
		if (name.equalsIgnoreCase(operatorClassName)) {
			name = operatorClassName;
		}
		
		JvmTypeReference superClass = typeUtils.createTypeRef(DefaultQDLOperator.class, typeDictionary.getSystemResourceSet());

		IQLClass opClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		opClass.setSimpleName(name);
		opClass.setPackageName(createQualifiedName(OPERATORS_NAMESPACE));
		opClass.setExtendedClass(superClass);		
		
		Map<String, String[]> parameters = new HashMap<>();
		Map<String, Parameter> parameterTypes = new HashMap<>();

		typeDictionary.removeSystemType(createQualifiedName(OPERATORS_NAMESPACE, name));
		typeDictionary.addSystemType(opClass, DefaultQDLOperator.class);
		typeDictionary.addOperator(opBuilder, opClass, parameters, parameterTypes);
		
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(opBuilder.getOperatorClass(), Object.class);
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return;
		}
			
		Set<Class<?>> parameterValueTypes = new HashSet<>();
		for (PropertyDescriptor curProperty : beanInfo.getPropertyDescriptors()) {
			Method writeMethod = curProperty.getWriteMethod();
			Method readMethod = curProperty.getReadMethod();
			if (readMethod != null && writeMethod != null && writeMethod.isAnnotationPresent(Parameter.class)) {
				Parameter parameterAnnotation = writeMethod.getAnnotation(Parameter.class);
				if (parameterAnnotation.deprecated()) {
					continue;
				}
				String parameterName = parameterAnnotation.name();
				if (Strings.isNullOrEmpty(parameterName)) {
					parameterName = curProperty.getName();
				}
				
				Class<?> parameterValueType = readMethod.getReturnType();
				boolean isList = parameterAnnotation.isList();
				boolean isMap = parameterAnnotation.isMap();
				
				if (isComplexParameterType(parameterValueType)) {
					parameterValueType = Object.class;
				}
				
				parameterTypes.put(parameterName.toLowerCase(), parameterAnnotation);
				parameters.put(parameterName.toLowerCase(), new String[]{curProperty.getName(), readMethod.getName(), writeMethod.getName()});
				parameterValueTypes.add(parameterValueType);
								
				IQLAttribute attribute = createParameterAttribute(parameterName,parameterValueType, isList, isMap);
				if (attribute != null) {
					opClass.getMembers().add(attribute);			
				}
				
				IQLMethod pqlSetter = createParameterSetter(parameterName,parameterValueType, isList, isMap);
				if (pqlSetter != null) {
					opClass.getMembers().add(pqlSetter);
				}
				
				IQLMethod pqlGetter = createParameterGetter(parameterName,parameterValueType, isList, isMap);
				if (pqlGetter != null) {
					opClass.getMembers().add(pqlGetter);
				}
			}				
		}
				
		IQLMethod constructor1 = BasicIQLFactory.eINSTANCE.createIQLMethod();
		constructor1.setSimpleName(name);
		
		JvmFormalParameter constructor1Arg = TypesFactory.eINSTANCE.createJvmFormalParameter();
		constructor1Arg.setName("source");
		constructor1Arg.setParameterType(typeUtils.createTypeRef(IQDLOperator.class, typeDictionary.getSystemResourceSet()));
		constructor1.getParameters().add(constructor1Arg);
		opClass.getMembers().add(constructor1);
		
		IQLMethod constructor2 = BasicIQLFactory.eINSTANCE.createIQLMethod();
		constructor2.setSimpleName(name);
		
		JvmFormalParameter constructor2Arg1 = TypesFactory.eINSTANCE.createJvmFormalParameter();
		constructor2Arg1.setName("source1");
		constructor2Arg1.setParameterType(typeUtils.createTypeRef(IQDLOperator.class, typeDictionary.getSystemResourceSet()));
		constructor2.getParameters().add(constructor2Arg1);
		
		JvmFormalParameter constructor2Arg2 = TypesFactory.eINSTANCE.createJvmFormalParameter();
		constructor2Arg2.setName("source2");
		constructor2Arg2.setParameterType(typeUtils.createTypeRef(IQDLOperator.class, typeDictionary.getSystemResourceSet()));
		constructor2.getParameters().add(constructor2Arg2);
		
		opClass.getMembers().add(constructor2);

		typeDictionary.setParameterValueTypes(parameterValueTypes);
	}
		
	private boolean isComplexParameterType(Class<?> parameterValue) {
		if (parameterValue != null) {
			if (parameterValue == Byte.class) {
				return false;
			} else if (parameterValue == Short.class) {
				return false;
			} else if (parameterValue == Integer.class) {
				return false;
			} else if (parameterValue == Long.class) {
				return false;
			} else if (parameterValue == Float.class) {
				return false;
			} else if (parameterValue == Double.class) {
				return false;
			} else if (parameterValue == Boolean.class) {
				return false;
			} else if (parameterValue == Character.class) {
				return false;
			} else if (parameterValue == String.class) {
				return false;
			} else if (List.class.isAssignableFrom(parameterValue)) {
				return false;
			} else if (Map.class.isAssignableFrom(parameterValue)) {
				return false;
			} else if (parameterValue.isPrimitive()) {
				return false;
			}
		}
		return true;
	}
	
	private IQLAttribute createParameterAttribute(String parameterName, Class<?> parameterValue, boolean isList, boolean isMap) {
		JvmTypeReference typeRef = createType(parameterValue, isList, isMap);
		
		if (typeRef != null) {
			IQLAttribute attribute= BasicIQLFactory.eINSTANCE.createIQLAttribute();
			attribute.setSimpleName(parameterName.toLowerCase());
			attribute.setType(typeRef);
			return attribute;
		}
		return null;
	}
	
	private IQLMethod createParameterSetter(String parameterName, Class<?> parameterValue, boolean isList, boolean isMap) {
		JvmTypeReference typeRef = createType(parameterValue, isList, isMap);

		if (typeRef != null) {
			IQLMethod method = BasicIQLFactory.eINSTANCE.createIQLMethod();
			method.setSimpleName("set"+firstCharUpperCase(parameterName.toLowerCase()));
			
			JvmFormalParameter arg = TypesFactory.eINSTANCE.createJvmFormalParameter();
			arg.setName(parameterName.toLowerCase());
			arg.setParameterType(typeRef);
			method.getParameters().add(arg);
			
			method.setReturnType(typeUtils.createTypeRef(Void.class, typeDictionary.getSystemResourceSet()));
			return method;

		}
		return null;
	}
	
	private IQLMethod createParameterGetter(String parameterName, Class<?> parameterValue, boolean isList, boolean isMap) {
		JvmTypeReference typeRef = createType(parameterValue, isList, isMap);

		if (typeRef != null) {
			IQLMethod method = BasicIQLFactory.eINSTANCE.createIQLMethod();
			if (typeUtils.isBoolean(typeRef)) {
				method.setSimpleName("is"+firstCharUpperCase(parameterName.toLowerCase()));
			} else {
				method.setSimpleName("get"+firstCharUpperCase(parameterName.toLowerCase()));
			}
					
			method.setReturnType(typeRef);
			return method;

		}
		return null;
	}
	
	private JvmTypeReference createType(Class<?> parameterValue, boolean isList, boolean isMap) {
		if (isMap) {
			return typeUtils.createTypeRef(Map.class, typeDictionary.getSystemResourceSet());
		} else if (isMap) {
			return typeUtils.createTypeRef(List.class, typeDictionary.getSystemResourceSet());
		} else {
			JvmTypeReference typeRef = typeUtils.createTypeRef(parameterValue, typeDictionary.getSystemResourceSet());
			return typeRef;
		}
	}
}
