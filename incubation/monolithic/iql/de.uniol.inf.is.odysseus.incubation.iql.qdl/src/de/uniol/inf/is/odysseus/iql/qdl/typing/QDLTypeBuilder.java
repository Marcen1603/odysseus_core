package de.uniol.inf.is.odysseus.iql.qdl.typing;


import java.awt.List;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.TypesFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.typing.ParameterFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.AbstractIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.DefaultQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.DefaultQDLSource;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;

public class QDLTypeBuilder extends AbstractIQLTypeBuilder<QDLTypeFactory> {
	public static String[] OPERATORS_NAMESPACE = new String[]{"operators"};
	private static final String[] SOURCES_NAMESPACE = new String[]{"sources"};
	
	
	@Inject
	public QDLTypeBuilder(QDLTypeFactory typeFactory) {
		super(typeFactory);
	}

	
	public void addSource(ILogicalOperator source) {
		String name = getSourceName(source);
		JvmTypeReference superClass = typeFactory.getTypeRef(DefaultQDLSource.class);

		IQLClass sourceClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		sourceClass.setSimpleName(firstCharUpperCase(name));
		sourceClass.setPackageName(createQualifiedName(SOURCES_NAMESPACE));
		sourceClass.setExtendedClass(superClass);

		for (SDFAttribute attr : source.getOutputSchema().getAttributes()) {
			IQLAttribute attribute= BasicIQLFactory.eINSTANCE.createIQLAttribute();
			attribute.setSimpleName(attr.getAttributeName().toUpperCase());
			attribute.setType(typeFactory.getTypeRef(String.class));
			sourceClass.getMembers().add(attribute);
		}
		IQLAttribute sourceAttr = BasicIQLFactory.eINSTANCE.createIQLAttribute();
		sourceAttr.setSimpleName(firstCharLowerCase(name));
		sourceAttr.setType(typeFactory.getTypeRef(sourceClass));
		sourceClass.getMembers().add(sourceAttr);
		
		typeFactory.addSystemType(sourceClass, DefaultQDLSource.class);
		typeFactory.addSource(name, source, sourceClass);
	}
	
	public void removeSource(ILogicalOperator source) {
		String name = firstCharUpperCase(source.getName().toLowerCase());
		removeSystemType(SOURCES_NAMESPACE, name);
		typeFactory.removeSource(getSourceName(source));
	}
	
	private String getSourceName(ILogicalOperator source) {
		return source.getName();
	}
	
	@SuppressWarnings({ "unchecked" })
	public void addOperator(IOperatorBuilder opBuilder) {
		String name = firstCharUpperCase(opBuilder.getName().toLowerCase());
		JvmTypeReference superClass = typeFactory.getTypeRef(DefaultQDLOperator.class);

		IQLClass opClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		opClass.setSimpleName(name);
		opClass.setPackageName(createQualifiedName(OPERATORS_NAMESPACE));
		opClass.setExtendedClass(superClass);		
		
		Map<String, String[]> parameters = new HashMap<>();
		Map<String, Parameter> parameterTypes = new HashMap<>();

		typeFactory.removeSystemType(createQualifiedName(OPERATORS_NAMESPACE, name));
		typeFactory.addSystemType(opClass, DefaultQDLOperator.class);
		typeFactory.addOperator(opBuilder, opClass, parameters, parameterTypes);
		
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
				
				Class<? extends IParameter<?>> parameterType = (Class<? extends IParameter<?>>) parameterAnnotation.type();
				Class<?> parameterValueType = null;
				boolean isList = parameterAnnotation.isList();
				boolean isMap = parameterAnnotation.isMap();
				
				if (ParameterFactory.isComplexParameterType(parameterType)) {
					parameterValueType = Object.class;
				} else {
					parameterValueType = ParameterFactory.getParameterValue(parameterType);
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
		constructor1Arg.setParameterType(typeFactory.getTypeRef(IQDLOperator.class));
		constructor1.getParameters().add(constructor1Arg);
		opClass.getMembers().add(constructor1);
		
		IQLMethod constructor2 = BasicIQLFactory.eINSTANCE.createIQLMethod();
		constructor2.setSimpleName(name);
		
		JvmFormalParameter constructor2Arg1 = TypesFactory.eINSTANCE.createJvmFormalParameter();
		constructor2Arg1.setName("source1");
		constructor2Arg1.setParameterType(typeFactory.getTypeRef(IQDLOperator.class));
		constructor2.getParameters().add(constructor2Arg1);
		
		JvmFormalParameter constructor2Arg2 = TypesFactory.eINSTANCE.createJvmFormalParameter();
		constructor2Arg2.setName("source2");
		constructor2Arg2.setParameterType(typeFactory.getTypeRef(IQDLOperator.class));
		constructor2.getParameters().add(constructor2Arg2);
		
		opClass.getMembers().add(constructor2);

		typeFactory.setParameterValueTypes(parameterValueTypes);
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
			
			method.setReturnType(typeFactory.getTypeRef(Void.class));
			return method;

		}
		return null;
	}
	
	private IQLMethod createParameterGetter(String parameterName, Class<?> parameterValue, boolean isList, boolean isMap) {
		JvmTypeReference typeRef = createType(parameterValue, isList, isMap);

		if (typeRef != null) {
			IQLMethod method = BasicIQLFactory.eINSTANCE.createIQLMethod();
			if (typeFactory.isBoolean(typeRef)) {
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
			return typeFactory.getTypeRef(Map.class);
		} else if (isMap) {
			return typeFactory.getTypeRef(List.class);
		} else {
			JvmTypeReference typeRef = typeFactory.getTypeRef(parameterValue);
			return typeRef;
		}
	}
}
