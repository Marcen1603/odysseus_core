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
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.TypesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.AbstractIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.qdl.types.compiler.OperatorSystemTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.types.compiler.SourceSystemTypeCompiler;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.QDLPipeOperatorImpl;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.QDLSinkOperatorImpl;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.QDLSourceImpl;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.Operator;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class QDLTypeBuilder extends AbstractIQLTypeBuilder<IQDLTypeDictionary, IQDLTypeUtils> implements IQDLTypeBuilder {
	public static String[] OPERATORS_NAMESPACE = new String[]{"operators"};
	private static final String[] SOURCES_NAMESPACE = new String[]{"sources"};
	
	@Inject
	private IQLQualifiedNameConverter converter;
	
	private static final Logger LOG = LoggerFactory.getLogger(QDLTypeBuilder.class);

	
	@Inject
	public QDLTypeBuilder(IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}

	@Override
	public void createSource(String sourceName, ILogicalOperator source) {
		String name = getSourceName(sourceName);
		JvmTypeReference superClass = typeUtils.createTypeRef(QDLSourceImpl.class, typeDictionary.getSystemResourceSet());

		IQLClass sourceClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		sourceClass.setSimpleName(firstCharUpperCase(name));
		sourceClass.setPackageName(createQualifiedName(SOURCES_NAMESPACE));
		sourceClass.setExtendedClass(superClass);

		for (SDFAttribute attr : source.getOutputSchema().getAttributes()) {
			IQLAttribute attribute= BasicIQLFactory.eINSTANCE.createIQLAttribute();
			attribute.setSimpleName(attr.getAttributeName().toUpperCase());
			attribute.setType(typeUtils.createTypeRef(String.class, typeDictionary.getSystemResourceSet()));
			attribute.setVisibility(JvmVisibility.PUBLIC);
			sourceClass.getMembers().add(attribute);
		}
		IQLAttribute sourceAttr = BasicIQLFactory.eINSTANCE.createIQLAttribute();
		sourceAttr.setSimpleName(firstCharLowerCase(name));
		sourceAttr.setVisibility(JvmVisibility.PUBLIC);
		sourceAttr.setType(typeUtils.createTypeRef(sourceClass));
		sourceClass.getMembers().add(sourceAttr);
		
		IQLSystemType systemType = new IQLSystemType(sourceClass, QDLSourceImpl.class);
		typeDictionary.addSystemType(systemType, new SourceSystemTypeCompiler(typeUtils, converter));
		typeDictionary.addSource(name, source, sourceClass);
	}
	
	@Override
	public void removeSource(String sourceName,ILogicalOperator source) {
		String name = getSourceName(sourceName);
		removeSystemType(SOURCES_NAMESPACE, name);
		typeDictionary.removeSource(name);
	}
	
	private String getSourceName(String name) {
		if (name.contains(".")) {
			String[] splits = name.split("\\.");
			return splits[splits.length-1];
		} else {
			return name;
		}
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
		
		Class<?> superJavaClass = null;
		if (opBuilder.getMaxInputOperatorCount() == 0) {
			superJavaClass = QDLSinkOperatorImpl.class;
		} else {
			superJavaClass = QDLPipeOperatorImpl.class;
		}
		
		JvmTypeReference superClass = typeUtils.createTypeRef(superJavaClass, typeDictionary.getSystemResourceSet());

		IQLClass opClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		opClass.setSimpleName(name);
		opClass.setPackageName(createQualifiedName(OPERATORS_NAMESPACE));
		opClass.setExtendedClass(superClass);		
		
		Map<String, String[]> parameters = new HashMap<>();
		Map<String, Parameter> parameterTypes = new HashMap<>();

		typeDictionary.removeSystemType(createQualifiedName(OPERATORS_NAMESPACE, name));
		
		IQLSystemType systemType = new IQLSystemType(opClass, superJavaClass);
		typeDictionary.addSystemType(systemType, new OperatorSystemTypeCompiler(typeDictionary, typeUtils));
		
		typeDictionary.addOperator(opBuilder, opClass, parameters, parameterTypes);
		
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(opBuilder.getOperatorClass(), Object.class);
		} catch (IntrospectionException e) {
			LOG.error("error while creating operator", e);
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
				
				parameterTypes.put(parameterName.toLowerCase(), parameterAnnotation);
				parameters.put(parameterName.toLowerCase(), new String[]{curProperty.getName(), readMethod.getName(), writeMethod.getName()});
				
				parameterValueTypes.add(parameterValueType);
								
						
				IQLMethod pqlSetter = createParameterSetter(parameterName,parameterValueType, isList, isMap);
				if (pqlSetter != null) {
					opClass.getMembers().add(pqlSetter);
				}
				
				IQLMethod pqlGetter = createParameterGetter(parameterName,parameterValueType, isList, isMap);
				if (pqlGetter != null) {
					opClass.getMembers().add(pqlGetter);
				}
				
				if (!isList && !isMap && isComplexParameterType(parameterValueType)) {
					parameterValueType = Object.class;
					
					parameterValueTypes.add(parameterValueType);
				
					IQLMethod pqlSetter1 = createParameterSetter(parameterName,parameterValueType, isList, isMap);
					if (pqlSetter1 != null) {
						opClass.getMembers().add(pqlSetter1);
					}
					
					IQLMethod pqlGetter1 = createParameterGetter(parameterName,parameterValueType, isList, isMap);
					if (pqlGetter1 != null) {
						opClass.getMembers().add(pqlGetter1);
					}
				}
			}				
		}
				
		IQLMethod constructor1 = BasicIQLFactory.eINSTANCE.createIQLMethod();
		constructor1.setSimpleName(name);
		constructor1.setVisibility(JvmVisibility.PUBLIC);
		
		JvmFormalParameter constructor1Arg = TypesFactory.eINSTANCE.createJvmFormalParameter();
		constructor1Arg.setName("source");
		constructor1Arg.setParameterType(typeUtils.createTypeRef(Operator.class, typeDictionary.getSystemResourceSet()));
		constructor1.getParameters().add(constructor1Arg);
		opClass.getMembers().add(constructor1);
		
		IQLMethod constructor2 = BasicIQLFactory.eINSTANCE.createIQLMethod();
		constructor2.setSimpleName(name);
		constructor2.setVisibility(JvmVisibility.PUBLIC);

		JvmFormalParameter constructor2Arg1 = TypesFactory.eINSTANCE.createJvmFormalParameter();
		constructor2Arg1.setName("source1");
		constructor2Arg1.setParameterType(typeUtils.createTypeRef(Operator.class, typeDictionary.getSystemResourceSet()));
		constructor2.getParameters().add(constructor2Arg1);
		
		JvmFormalParameter constructor2Arg2 = TypesFactory.eINSTANCE.createJvmFormalParameter();
		constructor2Arg2.setName("source2");
		constructor2Arg2.setParameterType(typeUtils.createTypeRef(Operator.class, typeDictionary.getSystemResourceSet()));
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
			} else if (parameterValue.isPrimitive()) {
				return false;
			}
		}
		return true;
	}
	
	private IQLMethod createParameterSetter(String parameterName, Class<?> parameterValue, boolean isList, boolean isMap) {
		JvmTypeReference typeRef = createType(parameterValue, isList, isMap);

		if (typeRef != null) {
			IQLMethod method = BasicIQLFactory.eINSTANCE.createIQLMethod();
			method.setSimpleName("set"+firstCharUpperCase(parameterName.toLowerCase()));
			method.setVisibility(JvmVisibility.PUBLIC);

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
			method.setVisibility(JvmVisibility.PUBLIC);					
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
