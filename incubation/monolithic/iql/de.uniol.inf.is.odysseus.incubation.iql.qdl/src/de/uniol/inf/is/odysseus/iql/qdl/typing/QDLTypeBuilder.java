package de.uniol.inf.is.odysseus.iql.qdl.typing;


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
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.TypesFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.AbstractIQLTypeBuilder;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.source.IQDLSource;

public class QDLTypeBuilder extends AbstractIQLTypeBuilder<QDLTypeFactory> {
	public static String[] OPERATORS_NAMESPACE = new String[]{"operators"};
	private static final String[] SOURCES_NAMESPACE = new String[]{"sources"};
	
	@Inject
	private IIQLLookUp lookUp;
	
	@Inject
	public QDLTypeBuilder(QDLTypeFactory typeFactory) {
		super(typeFactory);
	}

	
	public void addSource(ILogicalOperator source) {
		String name = firstCharUpperCase(source.getName().toLowerCase());
		JvmTypeReference superClass = typeFactory.getTypeRef(StreamAO.class);
		JvmTypeReference sourceInterface = typeFactory.getTypeRef(IQDLSource.class);

		IQLClass sourceClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		sourceClass.setSimpleName(name);
		sourceClass.setPackageName(createQualifiedName(SOURCES_NAMESPACE));
		sourceClass.getExtendedInterfaces().add(sourceInterface);
		sourceClass.setExtendedClass(superClass);

		for (SDFAttribute attr : source.getOutputSchema().getAttributes()) {
			IQLAttribute attribute= BasicIQLFactory.eINSTANCE.createIQLAttribute();
			attribute.setSimpleName(attr.getAttributeName().toUpperCase());
			attribute.setType(typeFactory.getTypeRef(String.class));
			sourceClass.getMembers().add(attribute);
		}	
		typeFactory.addSystemType(sourceClass, StreamAO.class);
		typeFactory.addSource(source);
	}
	
	public void removeSource(ILogicalOperator source) {
		String name = firstCharUpperCase(source.getName().toLowerCase());
		removeSystemType(SOURCES_NAMESPACE, name);
		typeFactory.removeSource(source);

	}
	
	public void addOperator(IOperatorBuilder opBuilder) {
		String name = firstCharUpperCase(opBuilder.getName().toLowerCase());
		JvmTypeReference opInterface1 = typeFactory.getTypeRef(ILogicalOperator.class);
		JvmTypeReference opInterface2 = typeFactory.getTypeRef(IQDLOperator.class);

		IQLClass opClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		opClass.setSimpleName(name);
		opClass.setPackageName(createQualifiedName(OPERATORS_NAMESPACE));
		opClass.getExtendedInterfaces().add(opInterface1);
		opClass.getExtendedInterfaces().add(opInterface2);
		
		
		Map<String, String[]> parameters = new HashMap<>();

		typeFactory.removeSystemType(createQualifiedName(OPERATORS_NAMESPACE, name));
		typeFactory.addSystemType(opClass, opBuilder.getOperatorClass());
		typeFactory.addOperator(opBuilder, parameters);
		
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
				
				
				parameters.put(parameterName.toLowerCase(), new String[]{curProperty.getName(), readMethod.getName(), writeMethod.getName()});
				
				Class<?> parameterValueType = curProperty.getPropertyType();
				parameterValueTypes.add(parameterValueType);
								
				IQLAttribute attribute = createParameterAttribute(parameterName,parameterValueType);
				if (attribute != null) {
					opClass.getMembers().add(attribute);			
				}
				
				if (!hasMethod(opInterface1, "set"+parameterName)) {
					IQLMethod pqlSetter = createParameterSetter(parameterName,parameterValueType);
					if (pqlSetter != null) {
						opClass.getMembers().add(pqlSetter);
					}
				}
				
				if (!hasMethod(opInterface1, "get"+parameterName) && !hasMethod(opInterface1, "is"+parameterName)) {
					IQLMethod pqlGetter = createParameterGetter(parameterName,parameterValueType);
					if (pqlGetter != null) {
						opClass.getMembers().add(pqlGetter);
					}
				}
			}				
		}	
		typeFactory.setParameterValueTypes(parameterValueTypes);
	}
	
	private boolean hasMethod(JvmTypeReference typeRef, String methodName) {
		for (JvmOperation op : lookUp.getPublicMethods(typeRef)) {
			if (op.getSimpleName().equalsIgnoreCase(methodName)) {
				return true;
			}
		}
		return false;
	}

	
	private IQLAttribute createParameterAttribute(String parameterName, Class<?> parameterValue) {
		JvmTypeReference typeRef = typeFactory.getTypeRef(parameterValue);
		
		if (typeRef != null) {
			IQLAttribute attribute= BasicIQLFactory.eINSTANCE.createIQLAttribute();
			attribute.setSimpleName(parameterName.toLowerCase());
			attribute.setType(typeRef);
			return attribute;
		}
		return null;
	}
	
	private IQLMethod createParameterSetter(String parameterName, Class<?> parameterValue) {
		JvmTypeReference typeRef = typeFactory.getTypeRef(parameterValue);

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
	
	private IQLMethod createParameterGetter(String parameterName, Class<?> parameterValue) {
		JvmTypeReference typeRef = typeFactory.getTypeRef(parameterValue);

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
}
